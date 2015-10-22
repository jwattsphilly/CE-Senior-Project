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

import java.util.ArrayList;

/**
 * Created by James Watts on 10/13/15.
 */
public class MicrowaveRunningActivity extends Activity {

    // TODO: Make everything visually appealing

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

        Intent infoIntent = getIntent();
        String fullCodedInstructionString = infoIntent.getStringExtra("Coded Instruction");
        instructionList = LocalDatabase.splitInstructions(fullCodedInstructionString);
        int totalTimeSeconds = LocalDatabase.getTotalSeconds(fullCodedInstructionString);

        secondsUntilFinished = totalTimeSeconds;

        timeOfNextStir = getTimeOfNextStir(instructionList, indexOfInstruction);

        counterText = new TextView(this);
        counterText.setText(LocalDatabase.secondsToString(totalTimeSeconds));
        counterText.setTextSize(75);
        LinearLayout.LayoutParams counterTextParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        counterTextParams.gravity = Gravity.CENTER_HORIZONTAL;

        stirText = new TextView(this);
        stirText.setText(" ");
        stirText.setTextSize(50);
        LinearLayout.LayoutParams stirTextParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        stirTextParams.gravity = Gravity.CENTER_HORIZONTAL;

        stopStartButton = new Button(this);
        stopStartButton.setText("Start");
        stopStartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRunning)               // Stop timer
                        {
                            // TODO: Send the "S" (Pause) instruction to the Arduino
                            UsbSingleton.sendDataUSB("S");
                            counter.cancel();
                            stirText.setText(" ");
                            stopStartButton.setText("Start");
                            isRunning = false;
                        }
                        else                        // Start timer
                        {
                            String currentInstruction = instructionList.get(indexOfInstruction);
                            // TODO: Send the instruction to the Arduino
                            UsbSingleton.sendDataUSB(currentInstruction);

                            // TODO: Send "s" (start) if we're just paused
                            // TODO: Send instruction portion if we're at the beginning or during a stir

                            startCounter(secondsUntilFinished);

                            stirText.setText(" ");
                            stopStartButton.setText("Stop");
                            isRunning = true;
                        }
                    }
                }
        );

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        rootLayout.addView(counterText, counterTextParams);

        rootLayout.addView(stirText, stirTextParams);

        rootLayout.addView(stopStartButton,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        setContentView(rootLayout);
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

}
