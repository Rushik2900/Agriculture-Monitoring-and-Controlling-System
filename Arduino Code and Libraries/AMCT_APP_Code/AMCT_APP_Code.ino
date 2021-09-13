#include <ESP8266WiFi.h>                 
#include <FirebaseArduino.h>      
#include <DHT.h> 
#include <NTPClient.h>
#include <WiFiUdp.h>     
#include <SoftwareSerial.h>
        
 
#define FIREBASE_HOST "ezagro-3669a-default-rtdb.firebaseio.com"      
#define FIREBASE_AUTH "tEfOM8EU8uUxfHPWzuOL4GrgtQ6xIA1JRHxuofEv"            
const char *ssid     = "Rushik";
const char *password = "Rushik@29";           

WiFiUDP ntpUDP;
const long utcOffsetInSeconds = 19800;
NTPClient timeClient(ntpUDP, "pool.ntp.org");

SoftwareSerial serialSIM800(1,3); 

#define ON LOW
#define OFF HIGH
int DHTPIN = 2;                                            
#define DHTTYPE DHT11                                        
DHT dht(DHTPIN, DHTTYPE); 

int RELAY1 = 15;  // VALVE 1
int RELAY2 = 13;  // VALVE 2
int RELAY3 = 14;  // PUMP
int RELAY4 = 12;  // FLOOD LIGHT

String Not;
String Hum;
String Temp,Soil_Moist,Soil;
String fld_lt, Pump, Val1, Val2, Auto; 
int w1,w2,w3,rs;
float sm;
String tim;
int flag_fld=1,flag_auto=1,flag_w3=1,flag_pump=1,flag_rain=1;
int counter=10000;

int value1 = OFF, value2 = OFF, value3 = OFF, value4 = OFF;
                                              
 
void setup() 
{
  Serial.begin(115200);
  
  pinMode(RELAY1, OUTPUT); digitalWrite(RELAY1, HIGH);
  pinMode(RELAY2, OUTPUT); digitalWrite(RELAY2, HIGH);
  pinMode(RELAY3, OUTPUT); digitalWrite(RELAY3, HIGH);
  pinMode(RELAY4, OUTPUT); digitalWrite(RELAY4, HIGH);
  pinMode(DHTPIN, INPUT);
  pinMode(16,OUTPUT);     //S0
  pinMode(5,OUTPUT);      //S1
  pinMode(4,OUTPUT);      //S2
  pinMode(0,OUTPUT);      //S3
  
  dht.begin();                                                 
                                                
  Serial.print("Connecting to ");
  Serial.print(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);

  Serial.println("Setup Complete");
  Serial.println("Sending SMS");
   
  //Set SMS format to ASCII
  serialSIM800.write("AT+CMGF=1\r\n");
  delay(1000);
 
  //Send new SMS command and message number
  serialSIM800.write("AT+CMGS=\"977937XXXX\"\r\n");
  delay(4000);
   
  //Send SMS content
  serialSIM800.write("TEST");
  delay(1000);
   
  //Send Ctrl+Z / ESC to denote SMS message is complete
  serialSIM800.write((char)26);
  delay(1000);
     
  Serial.println("SMS Sent");
  }

  Serial.println();
  Serial.print("Connected");
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());                               
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);  

  
  timeClient.begin();
  timeClient.setTimeOffset(19800);
              
 
}
 int waterlevel1()
{
  digitalWrite(16,LOW);
  digitalWrite(5,LOW);
  digitalWrite(4,LOW);
  digitalWrite(0,LOW);
  return analogRead(A0);
}
int waterlevel2()
{
  digitalWrite(16,HIGH);
  digitalWrite(5,LOW);
  digitalWrite(4,LOW);
  digitalWrite(0,LOW);
  return analogRead(A0);
}
// WATER LEVEL INDICATOR IN TANK
int waterlevel3()
{
  digitalWrite(16,LOW);
  digitalWrite(5,HIGH);
  digitalWrite(4,LOW);
  digitalWrite(0,LOW);
  return analogRead(A0);
}
int rainsensor()
{
  digitalWrite(16,HIGH);
  digitalWrite(5,HIGH);
  digitalWrite(4,LOW);
  digitalWrite(0,LOW);
  return analogRead(A0);
}
int soilmoisture()
{
  digitalWrite(16,LOW);
  digitalWrite(5,LOW);
  digitalWrite(4,HIGH);
  digitalWrite(0,LOW);
  float soil=analogRead(A0);
  soil=(soil)/1024;
  soil=1-soil;
  soil=soil*100;
  return soil; 
}
void sms()
{
  digitalWrite(16,LOW);
  digitalWrite(5,LOW);
  digitalWrite(4,HIGH);
  digitalWrite(0,LOW);
  serialSIM800.write("AT+CMGF=1\r\n");
  delay(1000);
 
  //Send new SMS command and message number
  serialSIM800.write("AT+CMGS=\"+91 8469044644\"\r\n");
  delay(4000);
   
  //Send SMS content
  serialSIM800.write("TEST");
  delay(1000);
   
  //Send Ctrl+Z / ESC to denote SMS message is complete
  serialSIM800.write("Soil Mositure:" + analogRead(A0));
  delay(1000);
     
  Serial.println("SMS SEnt");
}
void loop() 
{
  // Getting Time
  timeClient.update();

  unsigned long epochTime = timeClient.getEpochTime();
  Serial.print("Epoch Time: ");
  Serial.println(epochTime);

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

  int currentHour = timeClient.getHours();
  Serial.print("Hour: ");
  Serial.println(currentHour);  

  int currentMinute = timeClient.getMinutes();
  Serial.print("Minutes: ");
  Serial.println(currentMinute); 

  tim=(String)currentHour+ ":" + (String)currentMinute;

  Not=Firebase.getString("/Agro/2/Alerts/Notifications");
  Serial.print("Not :");
  Serial.println(Not);

  sms();
  
  
  // Getting data from DHT11
  float h = dht.readHumidity();                                 
  float t = dht.readTemperature();                             
  
  if (isnan(h) || isnan(t))                                    
  {                                   
    Serial.println("Failed to read from DHT sensor!");
  } 
  else
  {
    Serial.print("Humidity: ");  
    Serial.print(h);
    String fireHumid = String(h) + String("%");  
                   
    Serial.print("%  Temperature: ");  
    Serial.print(t);  
    Serial.println("°C ");
    String fireTemp = String(t) + String("°C"); 
                  
    Hum="/Agro/2/DHT11/Humidity/" + (String)currentYear + "/" + (String)currentMonth + "/" + (String)monthDay;
    Temp="/Agro/2/DHT11/Temperature/" + (String)currentYear + "/" + (String)currentMonth + "/" + (String)monthDay;
    Firebase.pushString(Hum, fireHumid);           
    Firebase.pushString(Temp, fireTemp);        
  }
  //Controlling unit
  
  fld_lt=Firebase.getString("/Agro/2/Flood Light");
  Serial.print("Flood  ");
  Serial.println(fld_lt);

  Auto=Firebase.getString("/Agro/2/Auto System");
  
  Pump=Firebase.getString("/Agro/2/Pump");
  Serial.print("Pump ");
  Serial.println(Pump);
  
  Val1=Firebase.getString("/Agro/2/Valve 1");
  Val2=Firebase.getString("/Agro/2/Valve 2");
  
  w1=waterlevel1();
  Serial.print("water level 1: ");
  Serial.println(w1);

  w2=waterlevel2();
  Serial.print("water level 2: ");
  Serial.println(w2);

  w3=waterlevel3();
  Serial.print("water level 3: ");
  Serial.println(w3);

  sm=soilmoisture();
  Serial.print("soil moisture: ");
  Serial.println(sm);
  Soil=String(sm) + String("%");
  Soil_Moist="/Agro/2/DHT11/Soil Moisture/" + (String)currentYear + "/" + (String)currentMonth + "/" + (String)monthDay;
  Firebase.pushString(Soil_Moist, Soil);

  rs=rainsensor();
  Serial.print("rain sensor: ");
  Serial.println(rs);

  if(rs>200)
  {
    if(flag_rain==1)
    {
      Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Rain Sensor : It's Raining");
      counter--;
      flag_rain=0;
    }
  }
  else if(rs<200)
  {
     flag_rain=1;
  }
  
  if(fld_lt=="On"){
    digitalWrite(RELAY4, ON);
    if(flag_fld==1)
    {
      Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Flood Light : On");
      counter--;
      flag_fld=0;
    }
  }
  else if(fld_lt=="Off")
  {
    digitalWrite(RELAY4, OFF);
    if(flag_fld==0)
    {
      Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Flood Light : Off");
      counter--;
      flag_fld=1;
    }
  }
   if(Auto=="On"){
    w3=waterlevel3();
    if(w3<=10)
    {
      Serial.print("Water in tank too low: ");
      Serial.println(w3);
      Firebase.setString("/Agro/2/Auto","Off");
      if(flag_w3==1)
      {
        Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Water Level in Tank : Too Low");
        counter--;
        flag_w3=0;
      }
    }
    else
    {
    flag_w3=1;
    digitalWrite(RELAY3, ON);
    if(flag_auto==1)
    {
      Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Automatic System : On");
      counter--;
      flag_auto=0;
    }
    w1=waterlevel1();
    Serial.print("water ");
    Serial.println(w1);
    if(w1<200)
    {
      digitalWrite(RELAY1, ON);
      Firebase.setString("/Agro/2/Valve 1","On");
    }
    else if(w1>200)
    {
      digitalWrite(RELAY2, ON);
      Firebase.setString("/Agro/2/Valve 2","On");
      digitalWrite(RELAY1, OFF);
      Firebase.setString("/Agro/2/Valve 1","Off");
      w2=waterlevel2();
      if(w2>200)
      {
        digitalWrite(RELAY3, OFF);
        if(flag_auto==0)
        {
            Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Automatic System : Off");
            counter--;
            flag_auto=1;
        }
        Firebase.setString("/Agro/2/Pump","Off");
        digitalWrite(RELAY2, OFF);
        Firebase.setString("/Agro/2/Valve 2","OFF");
      }
    }
    }
  }
  else if(Auto=="Off")
  {
    digitalWrite(RELAY3, OFF);
    if(flag_auto==0)
    {
      Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Automatic System : Off");
      counter--;
      flag_auto=1;
    }
  }
  if(Val1=="On"){
    digitalWrite(RELAY1, ON);
  }
  else if(Val1=="Off")
  {
    digitalWrite(RELAY1, OFF);
  }
  if(Val2=="On"){
    digitalWrite(RELAY2, ON);
  }
  else if(Val2=="Off")
  {
    digitalWrite(RELAY2, OFF);
  }
  if(Pump=="On"){
    w3=waterlevel3();
    if(w3<=10)
    {
      Serial.print("Water in tank too low: ");
      Serial.println(w3);
      Firebase.setString("/Agro/2/Pump","Off");
      if(flag_w3==1)
      {
        Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Water Level in Tank : Too Low");
        counter--;
        flag_w3=0;
      }
    }
    else
    {
      flag_w3=1;
      digitalWrite(RELAY3, ON);
      if(flag_pump==1)
      {
        Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Pump : On");
        counter--;
        flag_pump=0;
      }
    }
  }
    else if(Pump=="Off"){
     digitalWrite(RELAY3, OFF);
     if(flag_pump==0)
     {
        Firebase.pushString("/Agro/2/Alerts/Notifications/" + (String)counter + "/" + (String)tim, "Pump : Off");
        counter--;
        flag_pump=1;
     }
   }
  // Checking for firebase error
  if (Firebase.failed()) 
  {
 
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error()); 
      return;
  }

  delay(1000);
  /*
  digitalWrite(2,LOW);
  int a=digitalRead(2);
  Serial.println(a);
  
 */
}

