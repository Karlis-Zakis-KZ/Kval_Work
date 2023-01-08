// Using the DHT11 Libary
#include <dht11.h>

#define dht_apin A0 // Analog Pin sensor is connected to
 
dht11 dhtObject;
 
void setup(){
 
  Serial.begin(9600);
  delay(1000);//Wait before accessing Sensor
 
 //end "setup()"
}
 
void loop(){
  //Start of Program 
 
    dhtObject.read(dht_apin);

    // Creates a String that contains the humidity and temperature 
    String values = (getHumidityValue() +','+ getTemperatureValue());
    delay(1000);

    // removed any buffered previous serial data.
    Serial.flush();
    delay(1000);

    // sent sensors data to serial (sent sensors data to ESP8266)
    Serial.print(values); // 
    delay(2000);
    
  
    delay(5000);//Wait 5 seconds before accessing sensor again.
  
  // end loop()
}

// Function that gets the Humidity and returns it in string
String getHumidityValue(){

  dhtObject.read(dht_apin);
  int humidity = dhtObject.humidity;
  delay(50);
  return String(humidity); 
}

// Function that gets the Temperature and returns it in string
String getTemperatureValue(){
   dhtObject.read(dht_apin);
   int temp = dhtObject.temperature;
   delay(50);
   return String(temp); 
}