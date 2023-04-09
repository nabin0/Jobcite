import pymysql

conn = pymysql.connect(
    host= 'sql12.freesqldatabase.com',
    database='sql12608851',
    user='sql12608851',
    password='kEHTqt2Ihg',
    charset='utf8mb4',
    cursorclass=pymysql.cursors.DictCursor
)

cursor = conn.cursor()

query = """CREATE TABLE jobs (
    id integer PRIMARY KEY,
    job_title text NOT NULL,
    company_name text NOT NULL,
    experience_required text,
    salary text,
    location text,
    job_posted_on text,
    job_post_link text,
    job_description text,
    job_skills text,
    hiring_company_link text,
    job_basic_info text
)"""

cursor.execute(query)
conn.close()