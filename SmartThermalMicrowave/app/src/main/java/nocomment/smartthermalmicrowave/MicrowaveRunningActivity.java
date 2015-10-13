package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // TODO: Test this
        Intent infoIntent = getIntent();
        String fullCodedInstructionString = infoIntent.getStringExtra("Coded Instruction");
        instructionList = LocalDatabase.splitInstructions(fullCodedInstructionString);
        int totalTimeSeconds = LocalDatabase.getTotalSeconds(fullCodedInstructionString);

        secondsUntilFinished = totalTimeSeconds;

        timeOfNextStir = getTimeOfNextStir(instructionList, indexOfInstruction);

        counterText = new TextView(this);
        counterText.setText(secondsToString(totalTimeSeconds));

        stirText = new TextView(this);
        stirText.setText(" ");

        stopStartButton = new Button(this);
        stopStartButton.setText("Start");
        stopStartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRunning)               // Stop timer
                        {
                            // TODO: Send the "S" (Pause) instruction to the Arduino
                            counter.cancel();
                            stirText.setText(" ");
                            stopStartButton.setText("Start");
                        }
                        else                        // Start timer
                        {
                            String currentInstruction = instructionList.get(indexOfInstruction);
                            // TODO: Send the instruction to the Arduino
                            startCounter(secondsUntilFinished);

                            stirText.setText(" ");
                            stopStartButton.setText("Stop");
                        }
                    }
                }
        );

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        rootLayout.addView(counterText,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));

        rootLayout.addView(stirText,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));

        rootLayout.addView(stopStartButton,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

    }

    public String secondsToString(int time)
    {
        int minutes = time / 60;
        int seconds = time % 60;

        String minutesString = (minutes > 9)? ""+minutes: "0"+minutes;
        String secondsString = (seconds > 9)? ""+seconds: "0"+seconds;

        return minutesString + ":" + secondsString;
    }

    private void startCounter(final int timeLeft)
    {
        counter = new CountDownTimer(timeLeft * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                secondsUntilFinished = (int) (millisUntilFinished / 1000);
                counterText.setText(secondsToString(secondsUntilFinished));

                // Check if we're at a stir point, if so, "pause" the timer until start button pressed
                if(timeOfNextStir !=0 && secondsUntilFinished<=timeOfNextStir)
                {
                    this.cancel();
                    stirText.setText("Stir Please");    // "Go STIR Crazy!"
                    indexOfInstruction++;               // Update the "current" instruction and time of next stir
                    timeOfNextStir = getTimeOfNextStir(instructionList, indexOfInstruction);
                }
            }

            @Override
            public void onFinish() {
                secondsUntilFinished = 0;
                // YAY!  We finished!
                // TODO: Start EnjoyActivity
            }
        };

        counter.start();
    }

    private int getTimeOfNextStir(ArrayList<String> instructionList, int currentInstructionIndex)
    {
        int time = 0;

        for(int index=currentInstructionIndex; index<instructionList.size(); index++)
        {
            time += LocalDatabase.getTotalSeconds(instructionList.get(index));
        }

        return time;
    }

}
