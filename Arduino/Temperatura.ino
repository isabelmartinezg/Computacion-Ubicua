// Incluimos librería
#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiServer.h>
#include <DHT.h>
#include <Wire.h>    
#include <SFE_BMP180.h>
#include <SoftwareSerial.h>
#include <AltSoftSerial.h>
#include <SPI.h>
#include <MQTT.h>
#include <TinyGPS.h>
#include <WiFiClient.h>

// Definimos el pin digital donde se conecta el sensor
#define DHTPIN 7
#define DHTTYPE DHT11
int SMOKEA0=A0;
SFE_BMP180 bmp180;
SoftwareSerial ESPserial(2,3); 
char ssid[] = "HUAWEI P9 lite";
char pass[] = "12345678";


WiFiClient wifi;
MQTTClient client(wifi);
int statusW;
char broker[] = "tcp://192.168.43.77:1883";

// Topic con el que trabajamos
const char* topicName = "temperatura";
 
//SFE_BMP180 bmp180;
TinyGPS gps;//Declaramos el objeto gps
AltSoftSerial serialgps(8,9);//Declaramos el pin 9 Tx y 8 Rx

//Declaramos la variables para la obtención de datos

int contador = 0;
int tope = 20;
  
// Inicializamos el sensor DHT11
DHT dht(DHTPIN, DHTTYPE);
void setup() {
  // Inicializamos comunicación serie
  Serial.begin(9600);


 
   Serial.print(F("setup end\r\n"));
  serialgps.begin(9600);//Iniciamos el puerto serie del gps
  //Serial.println("");
  Serial.println(F("GPS GY-GPS6MV2 Leantec"));
  Serial.println(F(" ---Buscando senal--- "));
 
  dht.begin();
  pinMode(SMOKEA0, INPUT);

bmp180.begin();
  /*if (bmp180.begin())
    Serial.println("BMP180 iniciado");
  else
  {
    Serial.println("Error al iniciar BMP180");
  }*/
  
}

void loop() { 
  
  int year;
byte month, day, hour, minute, second, hundredths;
unsigned long chars;
unsigned short sentences, failed_checksum; 
  delay(5000);
  if (contador == 0)
 {
  
  int analogSensor = analogRead(SMOKEA0);
  // Leemos la humedad relativa
  float h = dht.readHumidity();
  // Leemos la temperatura en grados centígrados (por defecto)
  float t = dht.readTemperature();
  // Leemos la temperatura en grados Fahrenheit
  float f = dht.readTemperature(true);

  
  char status;
  double T,P;

 
  // Comprobamos si ha habido algún error en la lectura
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println(F("Error obteniendo los datos del sensor DHT11"));
    return;
  }
 
  // Calcular el índice de calor en Fahrenheit
  float hif = dht.computeHeatIndex(f, h);
  // Calcular el índice de calor en grados centígrados
  float hic = dht.computeHeatIndex(t, h, false);

  Serial.print(F("Humedad: "));
  Serial.print(h);
  Serial.print(F(" %"));
  Serial.print(F("Temperatura: "));
  Serial.print(t);
  Serial.print(F(" *C "));
  Serial.print(f);
  Serial.print(F(" *F"));
  Serial.print(F("Indice de calor: "));
  Serial.print(hic);
  Serial.print(F(" *C "));
  Serial.print(hif);
  Serial.print(F(" *F"));
  
  Serial.println();
  
  Serial.print(F("Pin A0: "));
  Serial.print(analogSensor);
  Serial.print(F("Smoke Level:"));
  Serial.println(analogSensor-50);

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
          Serial.print(F("Temperatura: "));
          Serial.print(F(" *C , "));
          Serial.print(F("Presion: "));
          Serial.print(P,2);
          Serial.println(F(" mb"));          
        }      
      }      
    }   
  }
}
contador = contador +1;
if (contador == tope) {contador =0; }
  while(serialgps.available()>0) 
  {
    int c = serialgps.read();
 
    if(gps.encode(c))  
    {
      float latitude, longitude;
      gps.f_get_position(&latitude, &longitude);
      Serial.print(F("Latitud/Longitud: ")); 
      Serial.print(latitude,5); 
      Serial.print(F(", ")); 
      Serial.println(longitude,5);


  gps.crack_datetime(&year,&month,&day,&hour,&minute,&second,&hundredths);
      Serial.print("Fecha: "); Serial.print(day, DEC); Serial.print(F("/")); 
      Serial.print(month, DEC); Serial.print(F("/")); Serial.print(year);
      Serial.print(" Hora: "); Serial.print(hour + 1, DEC); Serial.print(F(":")); 
      Serial.print(minute, DEC); Serial.print(F(":")); Serial.print(second, DEC); 
      Serial.print(F(".")); Serial.println(hundredths, DEC);
      Serial.print(F("Altitud (metros): "));
      Serial.println(gps.f_altitude()); 
      Serial.print(F("Rumbo (grados): ")); Serial.println(gps.f_course()); 
      Serial.print(F("Velocidad(kmph): "));
      Serial.println(gps.f_speed_kmph());
      Serial.print(F("Satelites: ")); Serial.println(gps.satellites());
      Serial.println();
      gps.stats(&chars, &sentences, &failed_checksum);  
    }
  }
}

