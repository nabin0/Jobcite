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


def scrape_facebook_jobs():
    url = 'https://www.metacareers.com/jobs/?is_leadership=0&is_in_page=0'
    response = requests.get(url)
    soup = BeautifulSoup(response.text, 'lxml')

    # Find job elements and extract relevant information
    job_elements = soup.find_all('div', class_='job-element')
    for job_element in job_elements:
        job_title = job_element.find('h3', class_='job-title').text.strip()
        job_location = job_element.find(
            'div', class_='job-location').text.strip()
        job_description = job_element.find(
            'div', class_='job-description').text.strip()

        # Print or process the extracted job data
        print(f"Title: {job_title}")
        print(f"Location: {job_location}")
        print(f"Description: {job_description}")
        print("--------------------")


# Call the function to initiate the scraping process
scrape_facebook_jobs()
