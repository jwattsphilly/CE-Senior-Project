package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Darin on 10/13/15.
 */
public class ManualMicrowaveControl extends Activity
{
    private enum MicrowaveMode{
        EXPRESS, TIME_COOK, POWER, RUNNING, PAUSED
    }

    private MicrowaveMode mode;
    private String timerString;
    private int powerLevel;
    private TextView timerText;
    private CountDownTimer timer = null;
    private int secondsUntilFinished = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = MicrowaveMode.EXPRESS;
        timerString = "";
        powerLevel = 10;
        timerText = (TextView) findViewById(R.id.timerTextView);
        setContentView(R.layout.microwave_manual_control);
    }

    public void pressButton(View view)
    {
        timerText = (TextView) findViewById(R.id.timerTextView);
        switch(view.getId())
        {
            case R.id.button0:
                switch(mode)
                {
                    case EXPRESS:   // 0 does nothing here
                        break;
                    case TIME_COOK: // Append a 0 on the timer
                    {
                        if (timerString != "") {
                            timerString += '0';             // Append the timer string
                            timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                            UsbSingleton.sendDataUSB("b0"); // Send message to microwave
                        }
                        break;
                    }
                    case POWER:     // Change power level to '0' or '10'
                    {
                        if (powerLevel == 1) {              // Last button pressed would have been '1'
                            powerLevel = 10;                // Change power level to 10
                            UsbSingleton.sendDataUSB("b0"); // Send message to microwave
                        } else if (powerLevel != 0) {
                            powerLevel = 0;                 // Change power level to 0
                            UsbSingleton.sendDataUSB("b0"); // Send message to microwave
                        }
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button1:
                switch(mode)
                {
                    case EXPRESS:   // Start microwave at 1:00
                    {
                        timerString = "100";                // Timer string now 1 minute
//                        timerText.setText("01:00");         // Update TextView
                        UsbSingleton.sendDataUSB("b1");     // Send message to microwave
                        startTimer(60);                     // Start the timer for 1 minute
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                    case TIME_COOK: // Append a 1 on the timer
                    {
                        timerString += '1';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b1");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '1'
                    {
                        powerLevel = 1;
                        UsbSingleton.sendDataUSB("b1");     // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button2:
                switch(mode)
                {
                    case EXPRESS:   // Start microwave at 2:00
                    {
                        timerString = "200";                // Timer string now 2 minutes
//                        timerText.setText("02:00");         // Update TextView
                        UsbSingleton.sendDataUSB("b2");     // Send message to microwave
                        startTimer(120);                    // Start the timer for 2 minutes
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                    case TIME_COOK: // Append a 2 on the timer
                    {
                        timerString += '2';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b2");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '2'
                    {
                        powerLevel = 2;
                        UsbSingleton.sendDataUSB("b2");     // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button3:
                switch(mode)
                {
                    case EXPRESS:   // Start microwave at 3:00
                    {
                        timerString = "300";                // Timer string now 3 minutes
//                        timerText.setText("03:00");         // Update TextView
                        UsbSingleton.sendDataUSB("b3");     // Send message to microwave
                        startTimer(180);                    // Start the timer for 3 minutes
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                    case TIME_COOK: // Append a 3 on the timer
                    {
                        timerString += '3';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b3");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '3'
                    {
                        powerLevel = 3;
                        UsbSingleton.sendDataUSB("b3");     // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button4:
                switch(mode)
                {
                    case EXPRESS:   // Start microwave at 4:00
                    {
                        timerString = "400";                // Timer string now 4 minutes
//                        timerText.setText("04:00");         // Update TextView
                        UsbSingleton.sendDataUSB("b4");     // Send message to microwave
                        startTimer(240);                    // Start the timer for 4 minutes
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                    case TIME_COOK: // Append a 4 on the timer
                    {
                        timerString += '4';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b4");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '4'
                    {
                        powerLevel = 4;
                        UsbSingleton.sendDataUSB("b4");     // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button5:
                switch(mode)
                {
                    case EXPRESS:   // Start microwave at 5:00
                    {
                        timerString = "500";                // Timer string now 5 minutes
//                        timerText.setText("05:00");         // Update TextView
                        UsbSingleton.sendDataUSB("b5");     // Send message to microwave
                        startTimer(300);                    // Start the timer for 5 minutes
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                    case TIME_COOK: // Append a 5 on the timer
                    {
                        timerString += '5';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b5");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '5'
                    {
                        powerLevel = 5;
                        UsbSingleton.sendDataUSB("b5");     // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button6:
                switch(mode)
                {
                    case EXPRESS:   // Start microwave at 6:00
                    {
                        timerString = "600";                // Timer string now 6 minutes
//                        timerText.setText("06:00");         // Update TextView
                        UsbSingleton.sendDataUSB("b6");     // Send message to microwave
                        startTimer(360);                    // Start the timer for 6 minutes
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                    case TIME_COOK: // Append a 6 on the timer
                    {
                        timerString += '6';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b6");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '6'
                    {
                        powerLevel = 6;
                        UsbSingleton.sendDataUSB("b6");     // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button7:
                switch(mode)
                {
                    case TIME_COOK: // Append a 7 on the timer
                    {
                        timerString += '7';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b7");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '7'
                    {
                        powerLevel = 7;
                        UsbSingleton.sendDataUSB("b7"); // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case EXPRESS:   // Do nothing
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button8:
                switch(mode)
                {
                    case TIME_COOK: // Append an 8 on the timer
                    {
                        timerString += '8';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b8");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '8'
                    {
                        powerLevel = 8;
                        UsbSingleton.sendDataUSB("b8"); // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case EXPRESS:   // Do nothing
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.button9:
                switch(mode)
                {
                    case TIME_COOK: // Append a 9 on the timer
                    {
                        timerString += '9';                 // Append the timer string
                        timerText.setText(convertToMinutesAndSeconds(timerString));// Update TextView
                        UsbSingleton.sendDataUSB("b9");     // Send message to microwave
                        break;
                    }
                    case POWER:     // Change power level to '9'
                    {
                        powerLevel = 9;
                        UsbSingleton.sendDataUSB("b9");     // Send message to microwave
                        timerText.setText("Power Level: " + powerLevel);// Update TextView
                        break;
                    }
                    case EXPRESS:   // Do nothing
                    case RUNNING:   // Do nothing
                    case PAUSED:    // Do nothing
                        break;
                }
                break;
            case R.id.buttonStart:
                switch(mode)
                {
                    case EXPRESS:   // Set timer to 30 seconds
                    {
                        timerString = "30";
//                        timerText.setText("00:30");         // Update TextView
                        UsbSingleton.sendDataUSB("s");      // Send message to microwave
                        startTimer(30);                     // Start the timer for 30 seconds
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                    case TIME_COOK: // Start
                    case POWER:     // Start
                    {
                        if(!timerString.isEmpty())
                        {
                            UsbSingleton.sendDataUSB("s");      // Send message to microwave
                            startTimer(convertToSeconds(convertToMinutesAndSeconds(timerString)));  // Start the timer using timerString
                            mode = MicrowaveMode.RUNNING;
                        }
                        break;
                    }
                    case RUNNING:   // add 30 seconds
                    {
                        UsbSingleton.sendDataUSB("s");      // Send message to microwave
                        timer.cancel();
                        secondsUntilFinished+=30;           // Add 30 seconds to the timer
                        startTimer(secondsUntilFinished);
                        break;
                    }
                    case PAUSED:
                    {
                        startTimer(secondsUntilFinished);
                        mode = MicrowaveMode.RUNNING;
                        break;
                    }
                }
                break;
            case R.id.buttonStop:
                switch(mode)
                {
                    case PAUSED:    // Cancel
                    case TIME_COOK: // Cancel
                    case POWER:     // Cancel
                    {
                        timerString = "";                   // Reset timer string and power level
                        powerLevel = 10;
                        UsbSingleton.sendDataUSB("S");      // Send message to microwave
                        timerText.setText("00:00");         // Update TextView
                        mode = MicrowaveMode.EXPRESS;
                        break;
                    }
                    case RUNNING:   // Pause
                    {
                        UsbSingleton.sendDataUSB("S");      // Send message to microwave
                        timer.cancel();
                        mode = MicrowaveMode.PAUSED;
                        break;
                    }
                    case EXPRESS:   // Do nothing
                        break;
                }
                break;
            case R.id.plateToggleBtn:
                switch(mode)
                {
                    case RUNNING:   // Toggle the motor
                        UsbSingleton.sendDataUSB("m");
                    default:        // Do nothing
                        break;
                }
                break;
            case R.id.buttonPowerLevel:
                switch(mode)
                {
                    case TIME_COOK: // Go into power level setting mode
                    {
                        if(timerString!="")     // Make sure the timer string has been set
                        {
                            powerLevel = 10;    // Reset the power level to 10
                            timerText.setText("Power Level: " + powerLevel);// Update TextView
                            UsbSingleton.sendDataUSB("bp"); // Send message to microwave
                            mode = MicrowaveMode.POWER;
                        }
                    }
                    default:        // Do nothing
                        break;
                }
                break;
            case R.id.buttonTimeCook:
                switch(mode)
                {
                    case EXPRESS:   // Go into time cook mode
                    case POWER:     // Go back into time cook mode
                    {
                        timerString = "";                   // Reset the timer String
                        timerText.setText("00:00");         // Update TextView
                        UsbSingleton.sendDataUSB("bt");     // Send message to microwave
                        mode = MicrowaveMode.TIME_COOK;
                    }
                    default:        // Do nothing
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void startTimer(final int timeLeft)
    {
        secondsUntilFinished = timeLeft;
        timer = new CountDownTimer(timeLeft * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                secondsUntilFinished = (int) (millisUntilFinished / 1000);
                timerText.setText(LocalDatabase.secondsToString(secondsUntilFinished));
            }

            @Override
            public void onFinish() {
                secondsUntilFinished = 0;
//                timerText.setText(LocalDatabase.secondsToString(secondsUntilFinished));
                timerText.setText("Enjoy!");
                // YAY!  We finished!
            }
        };
        timer.start();
    }

    // TODO: Test this
    private int convertToSeconds(String minutesAndSecondsString)
    {
        int index = minutesAndSecondsString.indexOf(':');
        int seconds = Integer.parseInt(minutesAndSecondsString.substring(index+1));
        int minutes = Integer.parseInt(minutesAndSecondsString.substring(0,index));

        seconds+=(60*minutes);

        return seconds;
    }

    private String convertToMinutesAndSeconds(String tString)
    {
        if(tString.isEmpty())
            return "00:00";

        if(tString.length() == 1)
            return "00:0"+tString;

        // Get the last two digits and convert them to minutes and seconds (in case someone tried to put in 90 seconds instead of 1:30)
        int index = tString.length()-2;
        int lastTwoDigits = Integer.parseInt(tString.substring(index));

        int minutes = lastTwoDigits / 60;
        int seconds = lastTwoDigits % 60;

        // Get the first digits and add them to minutes
        if(tString.length() > 2)
        {
            minutes += Integer.parseInt(tString.substring(0, index));
        }

        String minutesString = (minutes > 9) ? ""+minutes : "0"+minutes;
        String secondsString = (seconds > 9) ? ""+seconds : "0"+seconds;

        return minutesString + ":" + secondsString;
    }

}
