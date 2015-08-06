#define btn0 2           // Connected to relay 1
#define btn1 3           // Connected to relay 2
#define btn2 4           // Connected to relay 3
#define btn3 5           // Connected to relay 4
#define btn4 6           // Connected to relay 5
#define btn5 7           // Connected to relay 6
#define btn6 8           // Connected to relay 7
#define btn7 9           // Connected to relay 8
#define btn8 10          // Connected to relay 9
#define btn9 11          // Connected to relay 10
#define btnTimeCook 12   // Connected to relay 11
#define btnPower A0      // Connected to relay 12
#define btnStart A1      // Connected to relay 13
#define btnStop A2       // Connected to relay 14
#define btnX A3          // Connected to relay 15
#define btnY A4          // Connected to relay 16
// Note: pin A5 is purple wire, not currently used
// Note: pin 0 is red wire, not currently used
// Note: pin 1 is brown wire, not currently used
// Note: pin 13 is orange wire, not currently used
#define SERIAL_TIMEOUT 1000
#define MAX_SECONDS 5999  // 99 minutes and 59 seconds
#define POWER_HIGH 10
#define POWER_MEDIUM 7
#define POWER_LOW 5
#define POWER_DEFROST 3
#define POWER_ZERO 0

int lastPushedButton = -1;
char serialCommand, serialParams;
int cookTimeRemaining = 0;
long stopTimeMillis = 0;

void setup() {
  pinMode(btn0, OUTPUT);          digitalWrite(btn0, HIGH);
  pinMode(btn1, OUTPUT);          digitalWrite(btn1, HIGH);
  pinMode(btn2, OUTPUT);          digitalWrite(btn2, HIGH);
  pinMode(btn3, OUTPUT);          digitalWrite(btn3, HIGH);
  pinMode(btn4, OUTPUT);          digitalWrite(btn4, HIGH);
  pinMode(btn5, OUTPUT);          digitalWrite(btn5, HIGH);
  pinMode(btn6, OUTPUT);          digitalWrite(btn6, HIGH);
  pinMode(btn7, OUTPUT);          digitalWrite(btn7, HIGH);
  pinMode(btn8, OUTPUT);          digitalWrite(btn8, HIGH);
  pinMode(btn9, OUTPUT);          digitalWrite(btn9, HIGH);
  pinMode(btnTimeCook, OUTPUT);   digitalWrite(btnTimeCook, HIGH);
  pinMode(btnPower, OUTPUT);      digitalWrite(btnPower, HIGH);
  pinMode(btnStart, OUTPUT);      digitalWrite(btnStart, HIGH);
  pinMode(btnStop, OUTPUT);       digitalWrite(btnStop, HIGH);
  pinMode(btnX, OUTPUT);          digitalWrite(btnX, HIGH);
  pinMode(btnY, OUTPUT);          digitalWrite(btnY, HIGH);
  
  Serial.begin(9600);
}

/**
*  Adapted from Nathan Broadbent
*/
void loop() {
  while (Serial.available() > 0) {
    /* 
     * Serial API
     */
    serialCommand = Serial.read();
    switch (serialCommand) {
      case 'b':
        if (waitForSerial()) {
          serialParams = Serial.read();
          switch (serialParams) {
            case '0': pushButton(btn0); break;
            case '1': pushButton(btn1); break;
            case '2': pushButton(btn2); break;
            case '3': pushButton(btn3); break;
            case '4': pushButton(btn4); break;
            case '5': pushButton(btn5); break;
            case '6': pushButton(btn6); break;
            case '7': pushButton(btn7); break;
            case '8': pushButton(btn8); break;
            case '9': pushButton(btn9); break;
            case 't': pushButton(btnTimeCook); break;
            case 'c': pushButton(btnStop); break;
            case 'p': pushButton(btnPower); break;
            case 's': pushButton(btnStart); break;
          }
        }
        break;

      // Time  |  t90;  => Set time to 90 seconds
      case 't':
        if (waitForSerial()) {
          cookTimeRemaining = Serial.parseInt();
          stopTimeMillis = millis() + (cookTimeRemaining*1000);
          setTime(cookTimeRemaining);
        }
        break;

      // Power (integer or level)
      // Integer: pi6; => Set power to 6
      // Level:   plh  => Set power to high (10)
      case 'p':
        if (waitForSerial()) {
          serialParams = Serial.read();

          if (serialParams == 'i') {
            // Power integer
            setPower(Serial.parseInt());

          } else if (serialParams == 'l') {
            // Power level
            if (waitForSerial()) {
              serialParams = Serial.read();
              switch (serialParams) {
                case 'h': setPower(POWER_HIGH);    break;
                case 'm': setPower(POWER_MEDIUM);  break;
                case 'l': setPower(POWER_LOW);     break;
                case 'd': setPower(POWER_DEFROST); break;
                case 'z': setPower(POWER_ZERO);    break;
              }
            }
          }
        }
        break;

      // Start
      case 's':
        pushButton(btnStart);
        break;

      // Stop
      case 'S':
        pushButton(btnStop);
        break;
        
      // Wait
      case 'w':
        int waitTime = Serial.parseInt();
        while( millis() - stopTimeMillis >= 1000) {}   // Wait for timer to get to 1 second
        pushButton(btnStop);                           // Press stop twice in order to clear
        pushButton(btnStop);                           // the last cook time 
        delay(waitTime * 1000);
        break;
    }
  }
}

/*
*  
*/
void pushButton(int button) {
  if(button == lastPushedButton) {delay(100);}
  
  digitalWrite(button, LOW);
  delay(button == btnStart ? 300 : 100);  // Start button needs a longer press
  digitalWrite(button, HIGH);
  
  lastPushedButton = button;
}

/*
*  Method from Nathan Broadbent
*/
bool waitForSerial() {
  unsigned long serialTimer = millis();
  while(!Serial.available()) {
    if (millis() - serialTimer > SERIAL_TIMEOUT) {
      return false;
    }
  }
  return true;
}

/*
*  Method adapted from Nathan Broadbent
*/
void setTime(int totalSeconds) {
  if (totalSeconds > MAX_SECONDS) { return; }

  // Setting time will stop the microwave
  //if (on) { on = false; }

  int minutes, seconds;
  //currentTime = totalSeconds;

  pushButton(btnTimeCook);

  minutes = totalSeconds / 60;
  seconds = totalSeconds % 60;

  if (minutes != 0) {
    if (minutes / 10 != 0) {
      pushButton(minutes / 10);
    }
    pushButton(minutes % 10);
  }
  if (seconds >= 10 || minutes != 0) {
    pushButton(seconds / 10);
  }
  pushButton(seconds % 10);
}

/*
*  Method adapted from Nathan Broadbent
*/
void setPower(int power) {
  if (power < 0 || power > 10) { return; }

  pushButton(btnPower);

  if (power == 10) {
    pushButton(1);
    pushButton(0);
  } else {
    pushButton(power);
  }
}

