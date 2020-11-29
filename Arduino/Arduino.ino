

#include <DHT.h>
#include <WiFiEspClient.h>
#include <WiFiEsp.h>
#include <WiFiEspUdp.h>
#include "SoftwareSerial.h"
#include <PubSubClient.h>
#include <AltSoftSerial.h>
#include <TinyGPS.h>
#include <SFE_BMP180.h>
#include "MQ135.h"

#define DHTPIN 7
#define DHTTYPE DHT11
#define PIN_MQ135 A0
#define co2Zero 55
char ssid[] = "cubicua";
char pass[] = "estoesesparta";
TinyGPS gps;
AltSoftSerial serialgps(8,9);
MQ135 sensor = MQ135(PIN_MQ135);

WiFiEspClient wifi;
PubSubClient client (wifi);
SoftwareSerial soft(2, 3);
int status = WL_IDLE_STATUS;
int SMOKEA0=A0;

  

SFE_BMP180 bmp180;

DHT dht(DHTPIN, DHTTYPE);

void setup() {

   Serial.begin(9600);
   serialgps.begin(9600);
   client.setServer("192.168.1.116",1883);
   dht.begin();
   bmp180.begin();
   pinMode(SMOKEA0, INPUT);
  InitWiFi();
  
}



void loop() { 

  double T,P;
  int co2now[10];
  int co2raw=0;
  int co2comp=0;
  int co2ppm=0;
  int zzz=0;
  int grafX=0;
  char temp[10];
  char longi[10]; 
  char lat[10];
  char hum[10];
  char velocidad[10];
  char pres[10];
  char smok[10];
  for(int x=0; x<10;x++){
    co2now[x]=analogRead(A0);
    delay(200);
  }
  for(int x=0; x<10;x++){
    zzz=zzz+co2now[x];
  }
  co2raw=zzz/10;
  co2comp=co2raw-co2Zero;
  co2ppm=map(co2comp,0,1023,400,5000);
  float v = (analogRead(A2)*2);
 
  char* buffer ;
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();
  char buffer1[20];
   client.connect("Arduino");
   char status;
   dtostrf(v,2, 2, velocidad);
  dtostrf(co2ppm,0, 0, smok);
  dtostrf(humidity,0, 0, hum);
  dtostrf(temperature,0, 0, temp);
  status = bmp180.startTemperature(); //Inicio de lectura de temperatura
  if (status != 0)
  {   
    delay(status); //Pausa para que finalice la lectura
    status = bmp180.getTemperature(T); //Obtener la temperatura
    if (status != 0)
    {
      status = bmp180.startPressure(3); //Inicio lectura de presión
      if (status != 0)
      {        
        delay(status); //Pausa para que finalice la lectura        
        status = bmp180.getPressure(P,T); //Obtenemos la presión
        if (status != 0)
        {                  
               dtostrf(P,0, 0, pres);    
        }      
      }      
    }   
  }
  float latitude, longitude;
  while(serialgps.available()>0) 
  {
    int c = serialgps.read();
 
    if(gps.encode(c))  
    {
      
      gps.f_get_position(&latitude, &longitude);
      Serial.print(F("Latitud/Longitud: ")); 
      Serial.print(latitude,5); 
      Serial.print(F(", ")); 
      Serial.println(longitude,5);
    }
  }
  dtostrf(latitude,7, 5, lat);
  dtostrf(longitude,7, 5, longi);
  sprintf(buffer1,"%s %s %s %s %s %s %s",temp,hum,pres,smok,lat,longi,velocidad);
  Serial.print(buffer1);
       client.publish("temperatura", buffer1);
       
  delay(30000);
  status = WiFi.status();
  if ( status != WL_CONNECTED) {
    while ( status != WL_CONNECTED) {
      Serial.print("Attempting to connect to WPA SSID: ");
      Serial.println(ssid);
      // Connect to WPA/WPA2 network
      status = WiFi.begin(ssid, pass);
      delay(500);
    }
    Serial.println("Connected to AP");
  }
 }

 void InitWiFi()
{
  // initialize serial for ESP module
  soft.begin(9600);
  // initialize ESP module
  WiFi.init(&soft);
  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println(F("WiFi shield not present"));
    // don't continue
    while (true);
  }

  Serial.println(F("Connecting to AP ..."));
  // attempt to connect to WiFi network
  while ( status != WL_CONNECTED) {
    Serial.print(F("Attempting to connect to WPA SSID: "));
    Serial.println(ssid);
    // Connect to WPA/WPA2 network
    status = WiFi.begin(ssid,pass);
    delay(500);
  }
  Serial.println(F("Connected to AP"));
}
 



     
