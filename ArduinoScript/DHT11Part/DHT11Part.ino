#include <dht11.h>


#define dht_apin A0 // Analog Pin sensor is connected to
 
dht11 dhtObject;
 
void setup(){
 
  Serial.begin(9600);
  delay(1000);//Wait before accessing Sensor
 
}//end "setup()"
 
void loop(){
  //Start of Program 
 
    dhtObject.read(dht_apin);

    String values = (getHumidityValue() +','+ getTemperatureValue());
    delay(1000);
    // removed any buffered previous serial data.
    Serial.flush();
    delay(1000);
    // sent sensors data to serial (sent sensors data to ESP8266)
    Serial.print(values);
    delay(2000);
    
  
    delay(5000);//Wait 5 seconds before accessing sensor again.
 
  //Fastest should be once every two seconds.
 
}// end loop(



String getHumidityValue(){

  dhtObject.read(dht_apin);
  //Serial.print(" Humidity in %= ");
  int humidity = dhtObject.humidity;
  //Serial.println(humidity, DEC);
  delay(50);
  return String(humidity); 
  
}


String getTemperatureValue(){

   dhtObject.read(dht_apin);
   //Serial.print(" Temperature(C)= ");
   int temp = dhtObject.temperature;
   //Serial.println(temp, DEC); 
   delay(50);
   return String(temp); 
  
}