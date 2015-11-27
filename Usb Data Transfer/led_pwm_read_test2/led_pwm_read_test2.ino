/*
Adafruit Arduino - Lesson 3. RGB LED
*/
int redPin = 13;
int greenPin = 1;
int bluePin = 0;
//uncomment this line if using a Common Anode LED
#define COMMON_ANODE
void setup()
{
pinMode(redPin, OUTPUT);
pinMode(greenPin, OUTPUT);
pinMode(bluePin, OUTPUT);
}
void loop()
{
setColor(255,0,0);
delay(1);
setColor(0,255,0);
delay(1);
setColor(0,0,255);
delay(1);

}
void setColor(int red, int green, int blue)
{
#ifdef COMMON_ANODE
red = 255 - red;
green = 255 - green;
blue = 255 - blue;
#endif
analogWrite(redPin, red);
analogWrite(greenPin, green);
analogWrite(bluePin, blue);
}

