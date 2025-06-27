from flask import Flask, request, jsonify
from flask_cors import CORS
import pymysql
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)
CORS(app)

# DB 연결 함수
def get_tour_data(sigungu_code, exclude_contentid, areacode):
    conn = pymysql.connect(
        host='localhost',
        user='root',
        password='1111',
        db='what2do',
        charset='utf8',
        cursorclass=pymysql.cursors.DictCursor
    )
    query = """
        SELECT id, contentid, title, overview, firstimage2, areacode
        FROM tour
        WHERE sigungucode = %s AND areacode = %s AND contentid != %s
        AND overview IS NOT NULL AND overview != ''
    """
    with conn.cursor() as cursor:
        cursor.execute(query, (sigungu_code, exclude_contentid, areacode))
        rows = cursor.fetchall()
    conn.close()
    return pd.DataFrame(rows)

# 추천 API
@app.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json()
    overview = data.get("overview", "")
    sigungu_code = data.get("sigungu_code", "")
    areacode = data.get("areacode", "")
    contentid = data.get("content_id", "")

    if not overview or not sigungu_code:
        return jsonify({"error": "Missing data"}), 400

    df = get_tour_data(sigungu_code, areacode, contentid)
    if df.empty:
        return jsonify([])

    corpus = [overview] + df['overview'].tolist()
    tfidf = TfidfVectorizer(stop_words='english')
    tfidf_matrix = tfidf.fit_transform(corpus)
    cosine_sim = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:]).flatten()

    df['score'] = cosine_sim
    top3 = df.nlargest(3, 'score')[['id', 'contentid', 'title', 'firstimage2']]

    return jsonify(top3.to_dict(orient="records"))

if __name__ == "__main__":
    app.run(port=7000, debug=True)