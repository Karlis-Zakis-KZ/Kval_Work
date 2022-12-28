#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <FirebaseArduino.h>

// Set these to run example.
#define FIREBASE_HOST "smarthomeappv3-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "G6HwGPkDGGZ2WJ4IwNu1oQ71Uv6Bk83uoLuoGERm"
#define WIFI_SSID "ALHN-4C4D"
#define WIFI_PASSWORD "0313365827"

String values,sensor_data;

const String urlData = "http://192.168.1.110/meter/0";
const String urlCommands = "http://192.168.1.110/relay/0?";

const String UUID  = "LismajVzPrUOS48HmhzoGpphBAc2";
String path = "/Users/" + UUID;

void setup() {
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  Serial.println();
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {

 bool Sr =false;
 
  while(Serial.available()){
    sensor_data=Serial.readString(); 
    Sr=true;       
  }
  
  delay(1000);

  if(Sr==true){  
    
    values=sensor_data;
    
    int fristCommaIndex = values.indexOf(',');
    
    String humidity = values.substring(0, fristCommaIndex);
    String tempeture = values.substring(fristCommaIndex+1, values.length());

    Firebase.setString(path+"/Humidity",humidity);
    delay(10);
    Firebase.setString(path+"/Tempeture",tempeture);
    delay(1000);
    
    if (Firebase.failed()) {  
        return;
    }
  }
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status
 
    HTTPClient http;  //Declare an object of class HTTPClient
 
    http.begin(urlData);  //Specify request destination
    int httpCode = http.GET();                                  //Send the request
 
    if (httpCode > 0) { //Check the returning code
      String payload = http.getString();   //Get the request response payload
      Serial.println(payload);             //Print the response payload
    }
    http.end();   //Close connection

    String postData = "turn=off";
    delay(5000);
    http.begin(urlCommands);
    delay(2000);
    http.POST(postData);
    delay(2000);
    http.end();
  }
  delay(30000);
}