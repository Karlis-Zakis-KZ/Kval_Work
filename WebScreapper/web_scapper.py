import time
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By


# Defining the path of the executable web driver 
PATH = "../chromedriver.exe"

# Defining options to not open the web browser when scrapping the data
options = Options()
options.add_argument('--headless')
options.add_argument('--disable-gpu')

# Assigning the path and options to the driver
driver = webdriver.Chrome(PATH, chrome_options=options)

# Gets the webpage info 
driver.get("https://nordpool.didnt.work/?vat")


# By Xpath find the table in the webpage, then gets all the rows of the table, loops trough it and prints out the result
table = driver.find_element(By.XPATH, '//*[@id="app"]/table/tbody')
rows = table.find_elements(By.TAG_NAME, "tr")
for row in rows:
    col = row.find_elements(By.TAG_NAME, "td")[0]
    print(col.text)

#Runs every one hour to update the results
time.sleep(3600)