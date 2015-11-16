package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by James Watts on 10/13/15.
 */
public class MicrowaveRunningActivity extends Activity {

    private int secondsUntilFinished = 0;
    private int timeOfNextStir = 0;
    private int indexOfInstruction = 0;
    private boolean isRunning = false;
    private ArrayList<String> instructionList = new ArrayList<String>();

    private CountDownTimer counter = null;
    private TextView counterText = null;
    private TextView stirText = null;
    private Button stopStartButton = null;
    private ToggleButton motorControlButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent infoIntent = getIntent();
        String fullCodedInstructionString = infoIntent.getStringExtra("Coded Instruction");
        instructionList = LocalDatabase.splitInstructions(fullCodedInstructionString);
        int totalTimeSeconds = LocalDatabase.getTotalSeconds(fullCodedInstructionString);

        secondsUntilFinished = totalTimeSeconds;

        timeOfNextStir = getTimeOfNextStir(instructionList, indexOfInstruction);

        counterText = (TextView) findViewById(R.id.text_box_counter_text);
        counterText.setText(LocalDatabase.secondsToString(totalTimeSeconds));

        stirText = (TextView) findViewById(R.id.text_box_stir_text);

        // Send the first part of the instruction to the Arduino
        UsbSingleton.sendDataUSB(instructionList.get(indexOfInstruction));

        setContentView(R.layout.activity_microwave_running);
    }

    private void startCounter(final int timeLeft)
    {
        counter = new CountDownTimer(timeLeft * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                secondsUntilFinished = (int) (millisUntilFinished / 1000);
                counterText.setText(LocalDatabase.secondsToString(secondsUntilFinished));

                // Check if we're at a stir point, if so, "pause" the timer until start button pressed
                if(timeOfNextStir !=0 && secondsUntilFinished<=timeOfNextStir)
                {
                    this.cancel();
                    stirText.setText("Stir Please");    // "Go STIR Crazy!"
                    indexOfInstruction++;               // Update the "current" instruction and time of next stir
                    timeOfNextStir = getTimeOfNextStir(instructionList, indexOfInstruction);
                    stopStartButton.setText("Start");
                    isRunning = false;
                    // Send instruction portion
                    UsbSingleton.sendDataUSB(instructionList.get(indexOfInstruction));
                }
            }

            @Override
            public void onFinish() {
                secondsUntilFinished = 0;
                counterText.setText(LocalDatabase.secondsToString(secondsUntilFinished));
                // YAY!  We finished!
                setResult(InstructionsActivity.MICROWAVE_FINISHED_RESULT);
                finish();
            }
        };

        counter.start();
    }

    private int getTimeOfNextStir(ArrayList<String> instructionList, int currentInstructionIndex)
    {
        int time = 0;

        for(int index=currentInstructionIndex+1; index<instructionList.size(); index++)
        {
            time += LocalDatabase.getTotalSeconds(instructionList.get(index));
        }

        return time;
    }

    public void startStopOnClick(View v)
    {
        if (isRunning)               // Stop timer
        {
            // Send the "S" (Pause) instruction to the Arduino
            UsbSingleton.sendDataUSB("S");
            // Stop our timer
            counter.cancel();
            stirText.setText(" ");
            stopStartButton.setText("Start");
            isRunning = false;
        } else                        // Start timer
        {
            // Send "s" (start) instruction to the Arduino
            UsbSingleton.sendDataUSB("s");
            // Start our counter
            startCounter(secondsUntilFinished);

            stirText.setText(" ");
            stopStartButton.setText("Stop");
            isRunning = true;
        }
    }

}
