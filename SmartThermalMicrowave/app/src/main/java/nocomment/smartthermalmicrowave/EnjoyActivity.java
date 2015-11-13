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

/**
 * Created by jameswatts on 10/15/15.
 */
public class EnjoyActivity extends Activity implements View.OnClickListener{

    private TextView enjoyView = null;
    private boolean counterIsRunning = false;
    private boolean isMicrowavable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // TODO: Make everything visually appealing

        Intent intent = getIntent();
        isMicrowavable = intent.getBooleanExtra("Microwavable",true);

        enjoyView = new TextView(this);
        enjoyView.setText("Enjoy!");
        enjoyView.setTextSize(75);

        Button add30Button = new Button(this);
        add30Button.setText("+30 seconds?");
        add30Button.setOnClickListener(this);

        ToggleButton motorControlButton = new ToggleButton(this);
        motorControlButton.setText("Stop Plate");
        motorControlButton.setTextOff("Stop Plate");
        motorControlButton.setTextOn("Start Plate");
        motorControlButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UsbSingleton.sendDataUSB("m");  // Toggle the plate motor
                    }
                }
        );

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams enjoyParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        enjoyParams.gravity = Gravity.CENTER_HORIZONTAL;
        buttonParams.gravity = Gravity.CENTER_HORIZONTAL;

        rootLayout.addView(enjoyView, enjoyParams);
        rootLayout.addView(motorControlButton, buttonParams);
        rootLayout.addView(add30Button, buttonParams);

        setContentView(rootLayout);
    }

    @Override
    public void onClick(View v) {

        if(isMicrowavable) {
            if (!counterIsRunning) {
                CountDownTimer counter = new CountDownTimer(30000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int secondsUntilFinished = (int) (millisUntilFinished / 1000);
                        enjoyView.setText(LocalDatabase.secondsToString(secondsUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        enjoyView.setText("Enjoy!");
                        counterIsRunning = false;
                    }
                };

                UsbSingleton.sendDataUSB("s");
                counter.start();
                counterIsRunning = true;
            } else {
                // TODO: Perhaps have STOP/PAUSE capabilities?
                // TODO: OR keep adding 30 more seconds
            }
        }
        else
        {
            enjoyView.setText("Nice try, but that's not going to work!");
        }
    }
}
