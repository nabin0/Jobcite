import time
import requests
import json
from bs4 import BeautifulSoup
import sqlite3

def db_connection():
    conn = None
    try:
        conn = sqlite3.connect("jobs.sqlite")
    except sqlite3.error as e:
        print(e)
    return conn

def get_html_page(url):
    return requests.get(url).text

def parse_html_page(html_page):
    return BeautifulSoup(html_page, 'lxml')

def get_links_from_main_page(parsed_html):
    jobs = parsed_html.find_all('li', class_ = 'clearfix job-bx wht-shd-bx')
    links = []
    for job in jobs:
        links.append(job.find('a')['href'])
    return links

def get_job_data(job_page_link):
    html_page = get_html_page(job_page_link)
    parsed_html = parse_html_page(html_page)

    job_post_link = job_page_link
    job_header = parsed_html.find('div', 'jd-header wht-shd-bx')
    job_title = job_header.find('h1', 'jd-job-title').text.replace('\n', '').replace('\t', '').replace('\r', '').strip()
    company_name = job_header.find('h2').text.replace('\n', '').replace('\t', '').replace('\r', '').strip()
    
    job_post_date_html = parsed_html.find('ul', class_ = 'top-job-insight').find_all('li')
    job_post_date = job_post_date_html[1].text.replace('\n', '').replace('\t', '').replace('\r', '').strip()

    experience_and_other = job_header.find('ul', class_ = 'top-jd-dtl clearfix')
    experience_and_other_list = experience_and_other.find_all('li')
    unwanted_list = experience_and_other.find_all('i')
    for unwanted in unwanted_list:
        unwanted.extract()

    experience_required = experience_and_other_list[0].text.replace('\n', '').replace('\t', '').replace('\r', '').strip()
    experience_required = ' '.join(experience_required.split())
    salary = experience_and_other_list[1].text.strip()
    location = experience_and_other_list[2].text.replace('\n', '').replace('\t', '').replace('\r', '').strip()

    job_description = parsed_html.find('div', class_ = 'jd-desc job-description-main').text[16:].strip()

    job_skill_section = parsed_html.find('div', class_ = 'jd-sec job-skills clearfix')
    job_skills_html = job_skill_section.find_all('span', class_ = 'jd-skill-tag')
    job_skills = []
    for skill in job_skills_html:
        job_skills.append(skill.text.strip())

    # Job basic info
    job_basic_info_list = parsed_html.find('div', class_ = 'job-basic-info').find('ul').find_all('li')
    job_basic_info = {}
    for job_info in job_basic_info_list:
        try:
            job_info_key = job_info.label.text.strip()
            job_info_value = job_info.span.text.replace('\n', '').replace('\t', '').replace('\r', '').strip()
            job_basic_info[job_info_key] = job_info_value
        except:
            print('some data not found')
    hiring_company_link = None
    try:
        hiring_company_link = parsed_html.find('ul', class_ = 'hirng-comp-oth clearfix').find('span', class_ = 'basic-info-dtl').text
    except:
        print('No company link provided')

    job_data = {
        'job_title' : job_title,
        'company_name': company_name,
        'experience_required': experience_required,
        'salary': salary,
        'location': location,
        'job_posted_on': job_post_date,
        'job_post_link': job_post_link,
        'job_description': job_description,
        'job_skills': job_skills,
        'job_basic_info': job_basic_info,
        'hiring_company_link': hiring_company_link
    }

    # Write data to db
    conn = db_connection()
    cursor = conn.cursor()
    sql = 'INSERT INTO jobs (job_title, company_name, experience_required, salary, location, job_posted_on,job_post_link, job_description, job_skills, hiring_company_link, job_basic_info)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) '
    cursor.execute(sql, (job_title, company_name, experience_required, salary, location, job_post_date, job_post_link, job_description, json.dumps(job_skills), hiring_company_link, json.dumps(job_basic_info)))
    conn.commit()
    print('Job stored in database...\n')
    return job_data

def get_job_results():
    #Get all links for specific jobs 

    jobUrls = [
        "https://www.timesjobs.com/candidate/job-search.html?searchType=Home_Search&from=submit&asKey=OFF&txtKeywords=&cboPresFuncArea=35#",
        "https://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=android&txtLocation=",
        "https://www.timesjobs.com/candidate/job-search.html?from=submit&actualTxtKeywords=android&searchBy=0&rdoOperator=OR&searchType=personalizedSearch&luceneResultSize=25&postWeek=60&txtKeywords=android&pDate=I&sequence=2&startPage=1",
        "https://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=Website&txtLocation=",
        "https://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=Website+Development&txtLocation=",
        "https://www.timesjobs.com/candidate/job-search.html?searchType=personalizedSearch&from=submit&txtKeywords=software&txtLocation=india"
    ]

    for jobs_url in jobUrls:
        html_page = get_html_page(jobs_url)
        parsed_html = parse_html_page(html_page)
        links = get_links_from_main_page(parsed_html)
        # job_data = get_job_data(links[1])

        for link in links:
            get_job_data(link)

def main():
    get_job_results()

    # Automates Scraping
    while True:
        get_job_results()
        time_in_secs = 300
        print(f'Jobs wil be updated in {time_in_secs}.')
        time.sleep(time_in_secs)
    
if __name__ == '__main__':
    main()

