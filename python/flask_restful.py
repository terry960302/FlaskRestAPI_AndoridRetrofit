from flask import Flask
from flask_restful import Resource, Api, reqparse, abort

# Flask 인스턴스 정리
app = Flask(__name__)
api = Api(app)

# 할일 정의
Todos = {
    'todo1': {"task": "exercise"},
    'todo2': {'task': "eat delivery food"},
    'todo3': {'task': 'watch movie'}
}

# 예외처리
def exception(todo_id):
    if todo_id not in Todos:
        abort(404, message="Todos - {0} not exists".format(todo_id))


parser = reqparse.RequestParser()
parser.add_argument('task')


# 할일
class Todo(Resource):

    def get(self, todo_id):
        exception(todo_id)
        return Todos[todo_id]

    def delete(self, todo_id):
        exception(todo_id)
        del Todos[todo_id]
        return "", 204

    def put(self, todo_id):
        args = parser.parse_args()
        task = {'task': args['task']}
        Todos[todo_id] = task
        return task, 201


class TodoList(Resource):
    def get(self):
        return Todos

    def post(self):
        args = parser.parse_args()
        todo_id = "todo{}".format(len(Todos) + 1)
        Todos[todo_id] = {'task': args['task']}
        return Todos[todo_id], 201


api.add_resource(TodoList, '/todos/')
api.add_resource(Todo, '/todos/<string:todo_id>')

if __name__ == '__main__':
    app.run(host="192.168.219.147", port=5000, debug=True)  # 호스트를 내 아이피로 들어가야 커넥션 에러가 음슴

