#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <FirebaseArduino.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <Time.h>

// Set these to run example.
#define FIREBASE_HOST "smarthomeappv3-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "G6HwGPkDGGZ2WJ4IwNu1oQ71Uv6Bk83uoLuoGERm"
#define WIFI_SSID "ALHN-4C4D"
#define WIFI_PASSWORD "0313365827"

String values,sensor_data;

const String urlData = "/meter/0";
const String urlCommands = "/relay/0?";

const String UUID  = "LismajVzPrUOS48HmhzoGpphBAc2";
const String DeviceID = "Devices/Test1";
const String path = "/Users/" + UUID;

void setup() {
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  //Serial.println();
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

}

void loop() {

  WiFiUDP ntpUDP;
  NTPClient timeClient(ntpUDP, "pool.ntp.org");

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

    Firebase.setString(path+"/Temp"+"/Humidity",humidity);
    delay(1000);
    Firebase.setString(path+"/Temp"+"/Tempeture",tempeture);
    delay(1000);
    
    if (Firebase.failed()) {  
        return;
    }
  }
  if (WiFi.status() == WL_CONNECTED) { //Check WiFi connection status

    FirebaseObject object = Firebase.get(path);

    HTTPClient http;  //Declare an object of class HTTPClient
  
    delay(5000);
    int start = 0;
    String devliceListString = object.getString("DeviceList/DeviceIDS");

    timeClient.begin();
    timeClient.update();

    int end = devliceListString.indexOf(',');
    Serial.println(devliceListString);

    while (end != -1) {
      // Extract the substring between the start and end pointers
      String substring = devliceListString.substring(start, end);
      // Print the substring
      Serial.println(substring);
      // Update the start pointer to the character after the end pointer
      start = end + 1;
      // Update the end pointer to the next comma
      end = devliceListString.indexOf(',', start);

      http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);  //Specify request destination
      Serial.println(object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);
      int httpCode = http.GET();                                  //Send the request

      String payload = "";

      if (httpCode > 0) { //Check the returning code
        payload = http.getString();   //Get the request response payload
        Serial.println(payload);             //Print the response payload
      }
      http.end(); 

      int fristValueStart = payload.indexOf(':');
      int fristValueEnd = payload.indexOf(',');
      String state = payload.substring(fristValueStart+1, fristValueEnd);


      if(object.getString("/Devices/"+substring+"/On_Status") != state){
        if(state == "true"){
          http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);
          http.POST("turn=off");
          http.end();
        }else{
          http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);
          http.POST("turn=on");
          http.end();
        }
        delay(1000);
      }
      String deviceData = "";
      http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlData);
      int httpdata = http.GET();
      if (httpdata > 0) { //Check the returning code
        deviceData = http.getString();   //Get the request response payload
      }
      http.end();

      if(object.getString("/Devices/"+substring+"/Consumed_Energy_AtStart")=="0"){
        int totalStart = deviceData.indexOf("total");
        int totalEnd = deviceData.indexOf('}');
        String totalConsumtion = deviceData.substring(totalStart+7, totalEnd);

        Serial.println(totalConsumtion);
        Firebase.setString(path + "/Devices/"+substring +"/Consumed_Energy_AtStart",totalConsumtion);
      }

      if(object.getString("/Devices/"+substring+"/On_Status") == "true"){

        unsigned long epochTime = timeClient.getEpochTime();
        //Get a time structure
        struct tm *ptm = gmtime ((time_t *)&epochTime); 
      
        int monthDay = ptm->tm_mday;
        Serial.print("Month day: ");
        Serial.println(monthDay);
      
        int currentMonth = ptm->tm_mon+1;
        Serial.print("Month: ");
        Serial.println(currentMonth);
      
        int currentYear = ptm->tm_year+1900;
        Serial.print("Year: ");
        Serial.println(currentYear);


        String currentDate = String(currentYear) + "-" + String(currentMonth) + "-" + String(monthDay);
        Serial.print("Current date: ");
        Serial.println(currentDate);

        if(object.getString("/Devices/"+substring+"/HasPrice") == "true"){
          if(object.getString("/Devices/"+substring+"/PriceSet").toFloat() == Firebase.("/Eletricity Prices/"+currentDate+String(timeClient.getHours()))){
            http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);
            http.POST("turn=off");
            http.end();
          }
        }
        if(object.getString("/Devices/"+substring+"/HasTemp") == "true"){
          if(object.getString("/Devices/"+substring+"/TempSet").toInt() == object.getString("/Temp/Tempeture").toInt()){
            http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);
            http.POST("turn=off");
            http.end();
          }
        }
        if(object.getString("/Devices/"+substring+"/HasHumid") == "true"){
          if(object.getString("/Devices/"+substring+"/HumidSet").toInt() == object.getString("/Temp/Humidity").toInt()){
            http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);
            http.POST("turn=off");
            http.end();
          }
        }
        if(object.getString("/Devices/"+substring+"/HasTime") == "true"){
          if(object.getString("/Devices/"+substring+"/HourSet").toInt() <= timeClient.getHours()){
            if(object.getString("/Devices/"+substring+"/MinSet").toInt() <= timeClient.getMinutes()){
              http.begin("http://"+object.getString("/Devices/"+substring+"/Device_IP")+urlCommands);
              http.POST("turn=off");
              http.end();
            }
          }

        }
      }
    }
  }
  delay(15000);
}