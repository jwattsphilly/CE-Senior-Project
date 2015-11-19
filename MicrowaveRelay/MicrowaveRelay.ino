#include <stdio.h>
#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>

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
#define btnPlate A3      // Connected to relay 15
#define btnY A4          // Connected to relay 16
#define fiveVPin A5      // Psuedo power pin for RGB LED
#define greenLedPin 0    //Status LED green pin (RX Pin)
#define blueLedPin 1     //Status LED blue pin  (TX Pin)
#define redLedPin 13     //Status LED red pin

#define SERIAL_TIMEOUT 1000
#define MAX_SECONDS 5999  // 99 minutes and 59 seconds
#define POWER_HIGH 10
#define POWER_MEDIUM 7
#define POWER_LOW 5
#define POWER_DEFROST 3
#define POWER_ZERO 0
#define HEARTBEAT_TIMEOUT 10000  //10 seconds

/*
 *  (c) Nexus-Computing GmbH Switzerland
 *  Created on: Feb 02, 2012
 *      Author: Manuel Di Cerbo
 */

/*
 * UART-Initialization from www.mikrocontroller.net
 * Hint: They are awesome! :-)
 */

#ifndef F_CPU
#warning "F_CPU was not defined, defining it now as 16000000"
#define F_CPU 16000000UL
#endif

#define BAUD 9600UL      // baud rate
// Calculations
#define UBRR_VAL ((F_CPU+BAUD*8)/(BAUD*16)-1)   // smart rounding
#define BAUD_REAL (F_CPU/(16*(UBRR_VAL+1)))     // real baud rate
#define BAUD_ERROR ((BAUD_REAL*1000)/BAUD) // error in parts per mill, 1000 = no error
#if ((BAUD_ERROR<990) || (BAUD_ERROR>1010))
#error Error in baud rate greater than 1%!
#endif

void uart_init(void) {
  UBRR0H = UBRR_VAL >> 8;
  UBRR0L = UBRR_VAL & 0xFF;

  UCSR0C = (0 << UMSEL01) | (0 << UMSEL00) | (1 << UCSZ01) | (1 << UCSZ00); // asynchron 8N1
  UCSR0B |= (1 << RXEN0); // enable UART RX
  UCSR0B |= (1 << TXEN0); // enable UART TX
  UCSR0B |= (1 << RXCIE0); //interrupt enable
}

/* Receive symbol, not necessary for this example, using interrupt instead*/
int uart_getc(void) {
  while (!(UCSR0A & (1 << RXC0)))
    // wait until symbol is ready
    ;
  return UDR0; // return symbol
}

int uart_putc(unsigned char data) {
  /* Wait for empty transmit buffer */
  while (!(UCSR0A & (1 << UDRE0)))
    ;
  /* Put data into buffer, sends the data */
  UDR0 = data;
  return 0;
}

void initIO(void) {
  DDRD |= (1 << DDD3);
  DDRB = 0xff; //all out
}

volatile int USBdata = 10;
int i = 0;

boolean messageReady = false;
int maxUSBMessageLength = 100;
int USBMessageIter = 0;
char USBMessage[100];

/*End of Manuel Di Cerbo Setup Code*/

int lastPushedButton = -1;
char serialCommand, serialParams;
int cookTimeRemaining = 0;
long stopTimeMillis = 0;
boolean plateIsTurning = true;
long timeLastHeartbeatReceived = 0;

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
  pinMode(btnPlate, OUTPUT);      digitalWrite(btnPlate, HIGH);
  pinMode(btnY, OUTPUT);          digitalWrite(btnY, HIGH);
  pinMode(fiveVPin, OUTPUT);      digitalWrite(fiveVPin, HIGH);
  pinMode(redLedPin, OUTPUT);     digitalWrite(redLedPin, HIGH);
  pinMode(greenLedPin, OUTPUT);   digitalWrite(greenLedPin, HIGH);
  pinMode(blueLedPin, OUTPUT);    digitalWrite(blueLedPin, HIGH);
  
  for(; i < maxUSBMessageLength; i++)
    USBMessage[i] = 0;
  
  initIO();
  uart_init();
  sei();
}

/*
 *  Adapted from Manuel Di Cerbo
 */
ISR(USART_RX_vect) {//attention to the name and argument here, won't work otherwise
  USBdata = UDR0;//UDR0 needs to be read

  if(USBdata == 'f'){
    messageReady = true;
  }
  else if(messageReady == false)  //If USBdata != 'f' and the previous message has been dealt with
  {
    USBMessage[USBMessageIter] = USBdata;
    USBMessageIter++;
  }
}

/**
*  Adapted from Nathan Broadbent
*/
void loop() {
  if(messageReady){
    for(int iter=0; iter<USBMessageIter; iter++){
      serialCommand = USBMessage[iter];
      switch (serialCommand) {
        case 'b':
          serialParams = USBMessage[iter+1];
          switch (serialParams) {
            case '0': pushButton(btn0); iter++; break;
            case '1': pushButton(btn1); iter++; break;
            case '2': pushButton(btn2); iter++; break;
            case '3': pushButton(btn3); iter++; break;
            case '4': pushButton(btn4); iter++; break;
            case '5': pushButton(btn5); iter++; break;
            case '6': pushButton(btn6); iter++; break;
            case '7': pushButton(btn7); iter++; break;
            case '8': pushButton(btn8); iter++; break;
            case '9': pushButton(btn9); iter++; break;
            case 't': pushButton(btnTimeCook); iter++; break;
            case 'c': pushButton(btnStop); iter++; break;
            case 'p': pushButton(btnPower); iter++; break;
            case 's': pushButton(btnStart); iter++; break;
          }
          break;

        // Time  |  t90;  => Set time to 90 seconds
        case 't':
        {
          int cookTimeRemaining = 0;
          while(true)
          {
            iter++; //Go past t
            if(USBMessage[iter] >= 48 &&  USBMessage[iter] <= 57){
              cookTimeRemaining *= 10;
              cookTimeRemaining += (USBMessage[iter] - 48);
            }
            else
            {
              iter--;
              break;
            }
          }
          stopTimeMillis = millis() + (cookTimeRemaining*1000);
          setTime(cookTimeRemaining);
          break;
        }

        // Power (integer or level)
        // Integer: pi6; => Set power to 6
        // Level:   plh  => Set power to high (10)
        case 'p':
          serialParams = USBMessage[iter+1];

          if (serialParams == 'i') {
            // Power integer
            int powerLevel = 0;
            iter++;
            while(true)
            {
              iter++; //Go past i
              if(USBMessage[iter] >= 48 &&  USBMessage[iter] <= 57){
                powerLevel *= 10;
                powerLevel += (USBMessage[iter] - 48);
              }
              else
              {
                iter--;
                break;
              }
            }
            setPower(powerLevel);
          } 
          else if (serialParams == 'l') {
            // Power level
            serialParams = USBMessage[iter+2];
            switch (serialParams) {
              case 'h': setPower(POWER_HIGH);    iter+=2; break;
              case 'm': setPower(POWER_MEDIUM);  iter+=2; break;
              case 'l': setPower(POWER_LOW);     iter+=2; break;
              case 'd': setPower(POWER_DEFROST); iter+=2; break;
              case 'z': setPower(POWER_ZERO);    iter+=2; break;
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

        // Plate Toggle
        case 'm':
          if(plateIsTurning)
          {
            plateIsTurning = false;
            digitalWrite(btnPlate, LOW);
          }
          else
          {
            plateIsTurning = true;
            digitalWrite(btnPlate, HIGH);
          }
          break;
          
        case '~':
          timeLastHeartbeatReceived = millis();
        break;
      }
    }

    //Clear the message, reset the iterator, mark that message can be used again
    for(int iter=0; iter<USBMessageIter; iter++)
      USBMessage[iter] = 0;
    USBMessageIter = 0;
    messageReady = false;
  }
  
  //Check heartbeat
  long currentTime = millis();
  if(currentTime - timeLastHeartbeatReceived >= HEARTBEAT_TIMEOUT)
    indicateConnectionLost();
}

/*
*  
*/
void pushButton(int button) {
  if(button == lastPushedButton) {delay(100);}
  
  delay(250);
  digitalWrite(button, LOW);
  delay(button == btnStart ? 300 : 100);  // Start button needs a longer press
  digitalWrite(button, HIGH);
  
  lastPushedButton = button;
}

/*
*  Method adapted from Nathan Broadbent
*/
void setTime(int totalSeconds) {
  if (totalSeconds > MAX_SECONDS) { return; }

  int minutes, seconds;

  pushButton(btnTimeCook);

  minutes = totalSeconds / 60;
  seconds = totalSeconds % 60;

  if (minutes != 0) {
    if (minutes / 10 != 0) {
      pushButton((minutes / 10) + 2);
    }
    pushButton((minutes % 10) + 2);
  }
  if (seconds >= 10 || minutes != 0) {
    pushButton((seconds / 10) + 2);
  }
  pushButton((seconds % 10) + 2);
}

/*
*  Method adapted from Nathan Broadbent
*/
void setPower(int power) {
  if (power < 0 || power > 10) { return; }

  pushButton(btnPower);

  if (power == 10) {
    pushButton(btn1);
    pushButton(btn0);
  } else {
    pushButton(power + 2);
  }
}

void indicateConnectionLost(){
  digitalWrite(redLedPin, LOW);
}
