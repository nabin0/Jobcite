from flask import Flask, jsonify, request
import sqlite3
import json
import os

app = Flask(__name__)


@app.route('/videos', methods = ['GET'])
def videos():
    try:
        file_path = os.path.join(os.path.dirname(__file__), "videos.json")
        with open(file_path, 'r') as file:
            movies_data = json.load(file)
        return jsonify(movies_data)
    except FileNotFoundError:
        return jsonify({'error': 'File not found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500
    
@app.route('/video/<int:movie_id>')
def get_movie(movie_id):
    try:
        file_path = os.path.join(os.path.dirname(__file__), "videos.json")
        with open(file_path, 'r') as file:
            movies_data = json.load(file)
        for movie in movies_data:
            if movie.get('id') == movie_id:
                return jsonify(movie)
        return jsonify({'error': 'Movie not found'}), 404
    except FileNotFoundError:
        return jsonify({'error': 'File not found'}), 404
    except Exception as e:
        return jsonify({'error': str(e)}), 500


def db_connection():
    conn = None
    try:
        conn = sqlite3.connect(os.path.join(os.path.join(os.path.dirname(__file__), "jobs.sqlite")))
    except sqlite3.error as e:
        print(e)
    return conn

@app.route('/', methods = ['GET'])
def home():
    return f'Hello python', 200

@app.route('/jobs', methods=['GET'])
def jobs():
    conn = db_connection()
    if request.method == 'GET':
        cursor = conn.cursor()
        sql = 'SELECT * FROM jobs'
        cursor.execute(sql)
        jobs = [
            dict(id = row[0],
            job_title = row[1],
            company_name = row[2],
            experience_required = row[3],
            salary = row[4],
            location = row[5],
            job_posted_on = row[6],
            job_post_link = row[7],
            job_description = row[8],
            job_skills = json.loads(row[9]),
            hiring_company_link = row[10],
            job_basic_info = json.loads(row[11])
            )
            for row in cursor.fetchall()
        ]
    if jobs is not None:
        return jsonify(jobs)
    else:
        return "No jobs found", 200

@app.route('/search', methods=['GET'])
def searchJobs():

    query_args = request.args
    queryText = query_args.get("query")
    print(queryText)

    conn = db_connection()
    if(request.method == 'GET'):
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM jobs WHERE job_title LIKE ? OR job_skills LIKE ? ", ( '%' + queryText + '%', '%' + queryText + '%'))

        jobs = [
            dict(id = row[0],
            job_title = row[1],
            company_name = row[2],
            experience_required = row[3],
            salary = row[4],
            location = row[5],
            job_posted_on = row[6],
            job_post_link = row[7],
            job_description = row[8],
            job_skills = json.loads(row[9]),
            hiring_company_link = row[10],
            job_basic_info = json.loads(row[11])
            )
            for row in cursor.fetchall()
        ]

        if jobs is not None:
            return jsonify(jobs)
        else:
            return "No jobs found", 200


if __name__ == '__main__':
    app.run(debug=True)