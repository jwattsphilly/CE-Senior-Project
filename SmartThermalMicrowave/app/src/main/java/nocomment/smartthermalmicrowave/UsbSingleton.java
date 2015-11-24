package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Darin on 10/21/15.
 */
public class UsbSingleton {
    private static final int VID = 0x2341;
    private static final int PID = 0x0043;
    private static UsbController sUsbController;
    private static Timer heartbeatTimer = new Timer();
    private static long heartbeatDelay = 100;   //Time between heartbeats in milliseconds
    private static String heartbeatChar = "~f";

    public static void initUSB(Activity context)
    {
        if(sUsbController == null){
            sUsbController = new UsbController(context, mConnectionHandler, VID, PID);
        }

        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendDataUSB(heartbeatChar);
            }
        }, 0, heartbeatDelay);
    }

    public static void closeConnectionUSB()
    {
        if(sUsbController != null){
            sUsbController.stop();
        }
    }

    public static synchronized void sendDataUSB(String payload)
    {
        for(int i=0; i<payload.length(); i++)
        {
            final char substring = payload.charAt(i);

            SystemClock.sleep(5);   //Give the arduino time to process
            sUsbController.send((byte) (substring & 0xFF));
        }
        SystemClock.sleep(5);   //Give the arduino time to process
        sUsbController.send((byte) ('f' & 0xFF));   //Send the payload termination character
    }

    private static final IUsbConnectionHandler mConnectionHandler = new IUsbConnectionHandler() {
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
