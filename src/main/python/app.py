from http import HTTPStatus
from flask import Flask
from flask_cors import CORS
from recommend3 import top3
from flask import request
from flask import jsonify

app = Flask(__name__)
CORS(app)


@app.route('/getTop3',methods=['GET'])
def get():
    mainTitle = request.args.get('mainTitle', default = 'no', type=str)
    sigungucode = request.args.get('sigungucode', default = 'no', type=str)
    areacode = request.args.get('areacode', default = 'no', type=str)
    recommend=top3(mainTitle,sigungucode,areacode)
    for item in recommend:
        item['id'] = int(item['id'])
    return jsonify(recommend)

if __name__ == '__main__':
    app.run(port=5000)