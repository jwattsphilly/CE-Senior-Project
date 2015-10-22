package ch.serverbox.android.usbcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Darin on 10/15/15.
 */
public class ThermalMicrowaveUsbActivity extends Activity {
    /** Called when the activity is first created. */
    private static final int VID = 0x2341;
    private static final int PID = 0x0043;//I believe it is 0x0000 for the Arduino Megas
    private static UsbController sUsbController;

    private EditText payloadDataInput;
    private TextView payloadDataText;
    private Button btnUpdate;
    private Button btnDataSend;
    static String payloadByte = "t78f";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(sUsbController == null){
            sUsbController = new UsbController(this, mConnectionHandler, VID, PID);
        }

        payloadDataInput = (EditText) findViewById(R.id.payload_val_input);

        payloadDataText = (TextView)findViewById(R.id.payload_val_text);
        payloadDataText.setText(payloadByte);

        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ThermalMicrowaveUsbActivity.this, "Updated",
                        Toast.LENGTH_SHORT).show();
                String input = payloadDataInput.getText().toString();
                if(!input.equals("")) {
                    payloadByte = input.toString();
                    payloadDataText.setText(input);
                }
            }
        });

        btnDataSend = (Button) findViewById(R.id.btn_send);
        btnDataSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ThermalMicrowaveUsbActivity.this, "Data Sent",
                        Toast.LENGTH_SHORT).show();

                if(sUsbController != null){
                    for(int i=0; i<payloadByte.length(); i++)
                    {
                        final char substring = payloadByte.charAt(i);
                        SystemClock.sleep(5);   //Give the arduino time to process
                        sUsbController.send((byte) (substring & 0xFF));
                    }
                }
            }
        });
    }

    private final IUsbConnectionHandler mConnectionHandler = new IUsbConnectionHandler() {
        @Override
        public void onUsbStopped() {
            L.e("Usb stopped!");
        }

        @Override
        public void onErrorLooperRunningAlready() {
            L.e("Looper already running!");
        }

        @Override
        public void onDeviceNotFound() {
            if(sUsbController != null){
                sUsbController.stop();
                sUsbController = null;
            }
        }
    };
}
