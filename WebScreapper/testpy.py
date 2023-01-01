
import time
import pyrebase
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from datetime import date


today = date.today()





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

kz = False

database.child("/Users/LismajVzPrUOS48HmhzoGpphBAc2/Devices/Humid").update({"On_Status":kz})