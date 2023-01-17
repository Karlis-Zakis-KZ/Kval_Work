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

      String payload = getCommandData(object.getString("/Devices/"+substring+"/Device_IP"),urlCommands);

      int fristValueStart = payload.indexOf(':');
      int fristValueEnd = payload.indexOf(',');
      String state = payload.substring(fristValueStart+1, fristValueEnd);

      if(object.getString("/Devices/"+substring+"/On_Status") != state){
        if(state == "true"){
          sendCommand(object.getString("/Devices/"+substring+"/Device_IP"),urlCommands,"turn=off");
        }else{
          sendCommand(object.getString("/Devices/"+substring+"/Device_IP"),urlCommands,"turn=on");
        }
        delay(1000);
      }

      String deviceData = getCommandData(object.getString("/Devices/"+substring+"/Device_IP"),urlData);
      int startCurrentConsumption = deviceData.indexOf(':');
      int endCurrentConsumption = deviceData.indexOf(',');
      String CurrentConsumption = deviceData.substring(startCurrentConsumption+1, endCurrentConsumption);
      Firebase.setString(path + "/Devices/"+substring +"/Current_Consumption",CurrentConsumption);


      if(object.getString("/Devices/"+substring+"/On_Status") == "true"){

        int totalStart = deviceData.indexOf("total");
        int totalEnd = deviceData.indexOf('}');
        String totalConsumtion = deviceData.substring(totalStart+7, totalEnd);

        unsigned long epochTime = timeClient.getEpochTime();
        //Get a time structure
        struct tm *ptm = gmtime ((time_t *)&epochTime); 
      
        int monthDay = ptm->tm_mday;
        int currentMonth = ptm->tm_mon+1;
        int currentYear = ptm->tm_year+1900;
        String currentMontString = String(currentMonth);
        String currentDayString = String(monthDay);
        if (currentMonth<10){
          currentMontString = "0"+String(currentMonth);
        }
        if (monthDay<10){
          currentDayString = "0"+String(monthDay);
        }

        String currentDate = String(currentYear) + "-" + currentMontString + "-" + currentDayString;
        String getTime = String(timeClient.getHours())+"-"+String(timeClient.getHours()+1);

        if(object.getString("/Devices/"+substring+"/Last_Calculated_Consumption")=="0"){
          Firebase.setString(path + "/Devices/"+substring +"/Last_Calculated_Consumption",totalConsumtion);
        }else if (timeClient.getMinutes()%10==0){
          int startingNumber = object.getString("/Devices/"+substring+"/Last_Calculated_Consumption").toInt();
          int endingNumber = totalConsumtion.toInt();

          double calculatedPrice = ((endingNumber - startingNumber)/1000)*Firebase.getFloat("/Eletricity Prices/"+currentDate+"/"+getTime+"/Price");
          Serial.println(String(object.getString("/Devices/"+substring +"/Calculated_Price").toDouble()+calculatedPrice));
          Serial.println(calculatedPrice);
        }

        if(object.getString("/Devices/"+substring+"/HasPrice") == "true"){
          if(object.getFloat("/Devices/"+substring+"/PriceSet") <= Firebase.getFloat("/Eletricity Prices/"+currentDate+"/"+getTime+"/Price")){
            sendCommand(object.getString("/Devices/"+substring+"/Device_IP"),urlCommands,"turn=off");
            Firebase.setString(path+"/Devices/"+substring+"/HasPrice","false");
            Firebase.setString(path+"/Devices/"+substring+"/On_Status","false");
          }
        }
        if(object.getString("/Devices/"+substring+"/HasTemp") == "true"){
          if(object.getString("/Devices/"+substring+"/TempSet").toInt() == object.getString("/Temp/Tempeture").toInt()){
            sendCommand(object.getString("/Devices/"+substring+"/Device_IP"),urlCommands,"turn=off");
            Firebase.setString(path+"/Devices/"+substring+"/HasTemp","false");
            Firebase.setString(path+"/Devices/"+substring+"/On_Status","false");
          }
        }
        if(object.getString("/Devices/"+substring+"/HasHumid") == "true"){
          if(object.getString("/Devices/"+substring+"/HumidSet").toInt() == object.getString("/Temp/Humidity").toInt()){
            sendCommand(object.getString("/Devices/"+substring+"/Device_IP"),urlCommands,"turn=off");
            Firebase.setString(path+"/Devices/"+substring+"/HasHumid","false");
            Firebase.setString(path+"/Devices/"+substring+"/On_Status","false");
          }
        }
        if(object.getString("/Devices/"+substring+"/HasTime") == "true"){
          if(object.getString("/Devices/"+substring+"/HourSet").toInt() <= timeClient.getHours()){
            if(object.getString("/Devices/"+substring+"/MinSet").toInt() <= timeClient.getMinutes()){
              sendCommand(object.getString("/Devices/"+substring+"/Device_IP"),urlCommands,"turn=off");
              Firebase.setString(path+"/Devices/"+substring+"/HasTime","false");
              Firebase.setString(path+"/Devices/"+substring+"/On_Status","false");
            }
          }
        }
      }
    }
  }
  delay(15000);
}


void sendCommand(String objectPath, String urlCommands, String task){

  HTTPClient http; 
  http.begin("http://"+objectPath+urlCommands);
  http.POST(task);
  delay(1000);
  http.end();
}


String getCommandData(String link, String command){

  HTTPClient http; 
  http.begin("http://"+link+command);  //Specify request destination
  int httpCode = http.GET();                                  //Send the request
  delay(1000);
  String payload = "";
  if (httpCode > 0) { //Check the returning code
    payload = http.getString();   //Get the request response payload
    Serial.println(payload);             //Print the response payload
  }
  http.end(); 

  return payload;
}
