//
// Copyright 2015 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

// FirebaseDemo_ESP8266 is a sample that demo the different functions
// of the FirebaseArduino API.

#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>


// Set these to run example.
#define FIREBASE_HOST "smarthomeappv3-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "G6HwGPkDGGZ2WJ4IwNu1oQ71Uv6Bk83uoLuoGERm"
#define WIFI_SSID "ALHN-4C4D"
#define WIFI_PASSWORD "0313365827"

String values,sensor_data;

void setup() {
  Serial.begin(9600);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  //Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    //Serial.print(".");
    delay(500);
  }
  Serial.println();
  //Serial.print("connected: ");
  //Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {

 bool Sr =false;
 
  while(Serial.available()){
    
        //get sensor data from serial put in sensor_data
        sensor_data=Serial.readString(); 
        Sr=true;    
        
    }
  
  delay(1000);

  if(Sr==true){  
    
    values=sensor_data;
    
    //get comma indexes from values variable
    int fristCommaIndex = values.indexOf(',');
    
    //get sensors data from values variable by  spliting by commas and put in to variables  
    String humidity = values.substring(0, fristCommaIndex);
    String tempeture = values.substring(fristCommaIndex+1, values.length());

    //store ultrasonic sensor data as string in firebase 
    Firebase.setString("Humidity",humidity);
    delay(10);
    //store IR sensor 1 data as string in firebase 
    Firebase.setString("Tempeture",tempeture);

    delay(1000);
    
    if (Firebase.failed()) {  
        return;
    }
    
  }
}