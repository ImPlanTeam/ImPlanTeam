from flask import Flask, request, jsonify
from flask_cors import CORS
import pymysql
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
import numpy as np

app = Flask(__name__)
CORS(app)

@app.route('/recommend', methods=['POST'])
def recommend():
    data = request.get_json()
    title = data.get('title')
    sigungucode = data.get('sigungucode')

    if not title or not sigungucode:
        return jsonify([])

    conn = pymysql.connect(host='127.0.0.1', user='root', password='1111', db='what2do', charset='utf8')
    sql = "SELECT * FROM tour WHERE sigungucode = %s"
    df1 = pd.read_sql_query(sql, conn, params=(sigungucode,))
    conn.close()  # 연결 닫기

    df2 = df1[['title', 'overview']]
    co_data = df2['overview'].fillna('')
    mo_data = df2['title']

    tf_matrix = TfidfVectorizer(stop_words='english').fit_transform(co_data)
    cosine_sim = linear_kernel(tf_matrix, tf_matrix)
    find_num = pd.Series(mo_data.index, index=df2['title'])

    def top10(title):
        number_idx = find_num.get(title)

        # 중복 title에 대한 예외 처리
        if isinstance(number_idx, pd.Series):
            number_idx = number_idx.iloc[0]

        if number_idx is None or not isinstance(number_idx, (int, np.integer)):
            print("유효하지 않은 인덱스: ", number_idx)
            return []


        sim_scores = list(enumerate(cosine_sim[number_idx]))
        sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)[1:5]

        result = []
        for i, _ in sim_scores:
            row = df1.iloc[i]
            result.append({
                'id': int(row['id']),
                'name': row['title'],
                'city': row['addr1'].split()[0] if isinstance(row['addr1'], str) and row['addr1'].strip() != "" and len(row['addr1'].split()) > 0 else '기타',
                'firstimage2': row['firstimage2']
            })
        return result

    return jsonify(top10(title))

if __name__ == '__main__':
    app.run(port=5000, debug=True)
