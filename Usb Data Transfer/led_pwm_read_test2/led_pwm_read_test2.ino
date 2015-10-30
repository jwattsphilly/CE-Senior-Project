#define LED 13 // LED is on Pin 13 or Pin 5 of Port B
volatile uint8_t pause=255;

void setup()
{
  Serial.begin(9600);
  pinMode(13, OUTPUT);
}

void loop()
{
  if(Serial.available())
   { 
   //Serial.print("Got something.");
    pause = Serial.parseInt();
    //Serial.print("pause was set to ");
    //Serial.print(pause); Serial.print(" \n");
   }
   else{
  digitalWrite(LED, HIGH);
  delay(pause);
  digitalWrite(LED, LOW);
  delay(255-pause);
  
  //Serial.println(pause);    
   }
 
}
