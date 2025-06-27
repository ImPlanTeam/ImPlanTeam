import pandas as pd
import sqlalchemy
import sklearn
from sqlalchemy import create_engine
from konlpy.tag import Okt
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel

engine = create_engine('mysql+pymysql://root:1111@127.0.0.1:3306/what2do?charset=utf8mb4')

def top3(mainTitle,sigungucode,areacode):
    sql = "select * from tour where areacode=%s and sigungucode=%s"
    df = pd.read_sql(sql, engine, params=(areacode, sigungucode))
    df2=df[['overview','title','firstimage','id']]
    overview_data = df2['overview']
    title_data = df2['title']
    image_data = df2['firstimage']
    id_data = df2['id']
    overview_data.isnull().values.any()
    overview_data = overview_data.fillna('')
    image_data=image_data.fillna('')

    okt=Okt()
    a=[]
    for i in range(overview_data.size):
        a.append(okt.nouns(overview_data[i]))

    overview_data2 = pd.Series(a)
    tf = TfidfVectorizer()
    overview_data3 = overview_data2.apply(lambda words: ' '.join(words))
    tf_matrix=tf.fit_transform(overview_data3)
    cosine_sim = linear_kernel(tf_matrix, tf_matrix)
    find_num = pd.Series(title_data.index, index=df2['title'])



    number_index = find_num[mainTitle]   ## 문자열로 영화의 인덱스명(번호)를 찾는다 .
    aa = list(enumerate(cosine_sim[number_index]))  ##코사인 유사도 결과에서 인덱스의 행을 가져오는것이다.
    data_sort = sorted(aa, key=lambda x : x[1], reverse=True)
    ## aa리스트로 정렬해라 . 기준은 각 튜플의 1번째 값으로 정렬해라. 내림차순이다
    f_data = data_sort[1:4]
    f_data
    recommend = []
    for i in f_data:
        b = {'title':title_data[i[0]],
             'firstimage':image_data[i[0]],
             'id':id_data[i[0]]}
        recommend.append(b)
    print(recommend)
    return recommend  ##클라이언트에게 응답 할 자료
