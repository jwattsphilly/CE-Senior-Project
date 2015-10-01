package com.example.ivi.adr;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private EditText payloadDataInput;
    private TextView payloadDataText;
    private Button btnUpdate;
    private Button btnDataSend;

    MediaPlayer mediaPlayer;
    AssetManager asset;
    AssetFileDescriptor fd;
    int bitSelector = 1;
    int bytc;
    static int payloadByte = 0x7c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FragmentManager fragmentManager = getFragmentManager();
//        Fragment fragment = new Sketch(getApplicationContext());
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();
        asset = this.getAssets();
        payloadDataInput = (EditText) findViewById(R.id.payload_val_input);

        payloadDataText = (TextView)findViewById(R.id.payload_val_text);
        payloadDataText.setText(Integer.toString(payloadByte));

        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Updated",
                        Toast.LENGTH_SHORT).show();
                String input = payloadDataInput.getText().toString();
                if(!input.equals("")) {
                    payloadByte = Integer.parseInt(input);
                    payloadDataText.setText(input);
                }
            }
        });

        btnDataSend = (Button) findViewById(R.id.btn_send);
        btnDataSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Data Sent",
                        Toast.LENGTH_SHORT).show();
                sendData();
            }
        });

        setupMediaPlayer();
    }

    public EditText getPayloadDataInput() {
        return payloadDataInput;
    }

    public void setPayloadDataInput(EditText payloadDataInput) {
        this.payloadDataInput = payloadDataInput;
    }

    public Button getBtnUpdate() {
        return btnUpdate;
    }

    public void setBtnUpdate(Button btnUpdate) {
        this.btnUpdate = btnUpdate;
    }

    public Button getBtnDataSend() {
        return btnDataSend;
    }

    public void setBtnDataSend(Button btnDataSend) {
        this.btnDataSend = btnDataSend;
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            fd = asset.openFd("lnoise.wav");
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setVolume(0, 0);
            mediaPlayer.start();
            mediaPlayer.pause();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Preparation of Audio File Failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendData()
    {
        mediaPlayer.start();
        while(bitSelector <= 128) {
            bytc = payloadByte & bitSelector;

            //Create a small gap with no noise
            mediaPlayer.setVolume(0, 0);
            delay(10);

            //Start transmission of bit
            mediaPlayer.setVolume(1, 1);
            if (bytc == 0) {
                delay(30);  //0
            } else {
                delay(130); //1
            }
            //Stop bit transmission
            mediaPlayer.setVolume(0, 0);

            bitSelector <<= 1;
            delay(10);
        }
        bitSelector = 1;
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
    }

    private void delay(long milliseconds)
    {
        android.os.SystemClock.sleep(milliseconds);
    }
}

