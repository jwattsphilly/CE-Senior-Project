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
public class EnjoyActivity extends Activity{

    private TextView enjoyView = null;
    private boolean counterIsRunning = false;
    private boolean isMicrowavable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isMicrowavable = intent.getBooleanExtra("Microwavable",true);

        setContentView(R.layout.activity_enjoy);
    }

    public void addThirty(View v) {

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

    public void togglePlate(View v)
    {
        UsbSingleton.sendDataUSB("m");  // Toggle the plate motor
    }
}
