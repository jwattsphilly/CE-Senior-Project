package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by jameswatts on 10/15/15.
 */
public class EnjoyActivity extends Activity{

    private TextView enjoyView = null;
    private boolean counterIsRunning = false;
    private boolean isMicrowavable = true;
    private MediaPlayer mediaPlayer;
    private AssetManager asset;
    private AssetFileDescriptor fileDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isMicrowavable = intent.getBooleanExtra("Microwavable",true);

        setContentView(R.layout.activity_enjoy);

        enjoyView = (TextView) findViewById(R.id.text_view_enjoy_view);
        setupMediaPlayer();
        mediaPlayer.start();
    }

    public void addThirty(View v) {

        if(isMicrowavable) {
            if (!counterIsRunning) {
                CountDownTimer counter = new CountDownTimer(30500, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        int secondsUntilFinished = (int) (millisUntilFinished / 1000);
                        enjoyView.setText(LocalDatabase.secondsToString(secondsUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        UsbSingleton.sendDataUSB("S");  // Send a stop message to turn off the fan
                        enjoyView.setText("Enjoy!");
                        counterIsRunning = false;
                        mediaPlayer.start();
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
            mediaPlayer.start();
        }
    }

    private void setupMediaPlayer() {
        asset = this.getAssets();
        mediaPlayer = new MediaPlayer();
        try {
            fileDescriptor = asset.openFd("game-sound-correct.wav");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setVolume(1, 1);
        } catch (IOException e) {
            Toast.makeText(this, "Preparation of Audio File Failed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void togglePlate(View v)
    {
        UsbSingleton.sendDataUSB("m");  // Toggle the plate motor
    }
}
