import time
import pyrebase
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from datetime import date

today = date.today()

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

pricelist = {}

# By Xpath find the table in the webpage, then gets all the rows of the table, loops trough it and prints out the result
table = driver.find_element(By.XPATH, '//*[@id="app"]/table/tbody')
rows = table.find_elements(By.TAG_NAME, "tr")
for count,row in enumerate(rows):
    timeOfPrice = row.find_element(By.TAG_NAME, "th")
    price = row.find_elements(By.TAG_NAME, "td")[0]
    pricelist[timeOfPrice.text] = {}
    pricelist[timeOfPrice.text]['Price'] = float(price.text)
    pricelist[timeOfPrice.text]['Color'] = price.value_of_css_property("background-color")
    pricelist[timeOfPrice.text]['Time'] = int(str(timeOfPrice.text)[:2])

firebaseConfig = {
  "apiKey": "AIzaSyBlz2ZT4Rew_CdQrXEGmw7xMpkMj1_oiAM",
  "authDomain": "smarthomeappv3.firebaseapp.com",
  "databaseURL": "https://smarthomeappv3-default-rtdb.europe-west1.firebasedatabase.app",
  "projectId": "smarthomeappv3",
  "storageBucket": "smarthomeappv3.appspot.com",
  "messagingSenderId": "1012507563937",
  "appId": "1:1012507563937:web:e09a9de5c730b344cf9de0"
}

firebase = pyrebase.initialize_app(firebaseConfig)
database = firebase.database()

database.child("Eletricity Prices").child(today).update(pricelist)

#Runs every one hour to update the results
#time.sleep(3600)