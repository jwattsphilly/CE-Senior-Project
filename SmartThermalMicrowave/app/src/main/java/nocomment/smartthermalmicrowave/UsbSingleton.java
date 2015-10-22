package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;

/**
 * Created by Darin on 10/21/15.
 */
public class UsbSingleton {
    private static final int VID = 0x2341;
    private static final int PID = 0x0043;//I believe it is 0x0000 for the Arduino Megas
    private static UsbController sUsbController;

    public static void initUSB(Activity context)
    {
        if(sUsbController == null){
            sUsbController = new UsbController(context, mConnectionHandler, VID, PID);
        }
    }

    public static void sendDataUSB(String payload)
    {
        for(int i=0; i<payload.length(); i++)
        {
            final char substring = payload.charAt(i);
            SystemClock.sleep(5);   //Give the arduino time to process
            sUsbController.send((byte) (substring & 0xFF));
        }
        SystemClock.sleep(5);   //Give the arduino time to process
        sUsbController.send((byte) ('f' & 0xFF));
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
