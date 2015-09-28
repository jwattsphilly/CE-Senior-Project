float a,c;
int i, ii, iii, irec, rec,cc,ccc;
byte b, byt;
boolean l_sig,b_c;
void setup() {
  // put your setup code here, to run once:
  DDRB |= B00100000;//pin 13 out
  //DDRB &= ~B00010000;//pin 12 in
  cli();          // disable global interrupts
  TCCR1A = 0;     // set entire TCCR1A register to 0
  TCCR1B = 0;     // same for TCCR1B

  // set compare match register to desired timer count:
  OCR1A = 5; //how many cycles of 62.5 ns//5

  // turn on CTC mode:
  TCCR1B |= (1 << WGM12);

  // Set CS10 bit for no prescaler:
  TCCR1B |= (1 << CS10);
  TCCR1B |= (1 << CS12);//1024 prescaler

  // enable timer compare interrupt:
  TIMSK1 |= (1 << OCIE1A);

  // enable global interrupts:
  sei();
  cc=0;
  i = 0;
  ii = 0;
  iii = 0;
  rec = 0x00;
  irec = 0x01;
  byt = 0x7D; //compare byte
 Serial.begin(9600);
}

void loop() {
  b = 0;
  i = 0x01;
//Serial.println(ccc);
  while (i < 0x400) { //measure the SI
    a = analogRead(A1);
    if (a > ccc)b |= i; //volt baz +~3;check measurement
  }

  iii++;
  if (iii > 10) { //reset byte after a while of pause
    iii = 0;
    rec = 0;
    irec = 0x01;
  }
 

  if (b > 0) { //detect the high decibel signal
    iii = 0; //set to zero;
    l_sig = true;
    ii++;
  } else if (l_sig && b == 0) { //when it gets low,get the length,data encoding start**
   // Serial.println(ccc);//Debuger
    // write the byte
    if (ii > 20)rec |= irec;
    irec <<= 1;

    if (irec > 0x80) { //if byte ready
     Serial.println(rec);
      if (rec == byt)PORTB |= 0x20;
      else PORTB &= ~0x20;
      irec = 0x01;
      rec = 0;
    }
    l_sig = false;
    ii = 0;
  }
}

ISR(TIMER1_COMPA_vect)
{
  i <<= 1;
  //volume stabilizer #beta//put volume to max
 if(!b_c){
c += analogRead(A1);
cc+=1;
if(cc>20){
 ccc=int(c/cc)+4;
b_c=true;
 
}
  }
}
