package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Darin on 7/25/15.
 */
public class BrowseByItem extends Activity {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    UsbEndpoint input, output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Create a textView to hold the message
        TextView textView = new TextView(this);
        textView.setVerticalScrollBarEnabled(true);
        textView.setTextSize(40);
        //textView.setText(message);

        //Set up USB connection
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = manager.getDeviceList();

        String resultantString = new String();
        for(String deviceKey: devices.keySet()) {
            int timeout = 1000;
            resultantString += deviceKey + ",";
            textView.setText("Device Key:" + resultantString + "\n");

            /* SET UP THE USB STUFF */
            UsbDevice device = devices.get(deviceKey);

            //User must approve of connected device
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(device, mPermissionIntent);

            //Open the USB device and get the interface set up
            UsbDeviceConnection connection = manager.openDevice(device);
            UsbInterface cameraUSBInterface = device.getInterface(0);

            //Take the interface from the kernal if needed
            connection.claimInterface(cameraUSBInterface, true);

            //Find the endpoints
            for(int j = 0; j < cameraUSBInterface.getEndpointCount(); j++)
            {
                if(cameraUSBInterface.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT && cameraUSBInterface.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                {
                    // from android to device
                    output = cameraUSBInterface.getEndpoint(j);
                }

                if(cameraUSBInterface.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_IN && cameraUSBInterface.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                {
                    // from device to android
                    input = cameraUSBInterface.getEndpoint(j);
                }
            }


            //Reset the camera
            resetCamera(connection);

            //Send the data
//            vector<uint8_t> data = {0x01};
//            vendor_transfer(0, 0x54, 0, 0, data);
            byte[] data0 = {0x01};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x54, 0, 0, data0, data0.length, timeout);

//            vector<uint8_t> data = {0x00, 0x00};
//            vendor_transfer(0, 0x3c, 0, 0, data);
            byte[] data1 = {0x00, 0x00};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x3c, 0, 0, data1, data1.length, timeout);

//            vector<uint8_t> data(4);
//            vendor_transfer(1, 0x4e, 0, 0, data);
            byte[] data2 = new byte[4];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x4e, 0, 0, data2, data2.length, timeout);
//            textView.append(data2.toString()+ "\n");
//
//            vector<uint8_t> data(12);
//            vendor_transfer(1, 0x36, 0, 0, data);
            byte[] data3 = new byte[12];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x36, 0, 0, data3, data3.length, timeout);
//            textView.append(data3.toString()+ "\n");
//
//            vector<uint8_t> data = { 0x20, 0x00, 0x30, 0x00, 0x00, 0x00 };
//            vendor_transfer(0, 0x56, 0, 0, data);
            byte[] data4 = {0x20, 0x00, 0x30, 0x00, 0x00, 0x00};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x56, 0, 0, data4, data4.length, timeout);
//
//            vector<uint8_t> data(64);
//            vendor_transfer(1, 0x58, 0, 0, data);
            byte[] data5 = new byte[64];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x58, 0, 0, data5, data5.length, timeout);
//            textView.append(data5.toString()+ "\n");
//
//            vector<uint8_t> data = { 0x20, 0x00, 0x50, 0x00, 0x00, 0x00 };
//            vendor_transfer(0, 0x56, 0, 0, data);
            byte[] data6 = {0x20, 0x00, 0x50, 0x00, 0x00, 0x00 };
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x56, 0, 0, data6, data6.length, timeout);
//
//            vector<uint8_t> data(64);
//            vendor_transfer(1, 0x58, 0, 0, data);
            byte[] data7 = new byte[64];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x58, 0, 0, data7, data7.length, timeout);
//            textView.append(data7.toString()+ "\n");
//
//            vector<uint8_t> data = { 0x0c, 0x00, 0x70, 0x00, 0x00, 0x00 };
//            vendor_transfer(0, 0x56, 0, 0, data);
            byte[] data8 = {0x0c, 0x00, 0x70, 0x00, 0x00, 0x00};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x56, 0, 0, data8, data8.length, timeout);
//
//            vector<uint8_t> data(24);
//            vendor_transfer(1, 0x58, 0, 0, data);
            byte[] data9 = new byte[24];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x58, 0, 0, data9, data9.length, timeout);
//            textView.append(data9.toString()+ "\n");
//
//            vector<uint8_t> data = { 0x06, 0x00, 0x08, 0x00, 0x00, 0x00 };
//            vendor_transfer(0, 0x56, 0, 0, data);
            byte[] data10 = {0x06, 0x00, 0x08, 0x00, 0x00, 0x00};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x56, 0, 0, data10, data10.length, timeout);
//
//            vector<uint8_t> data(12);
//            vendor_transfer(1, 0x58, 0, 0, data);
            byte[] data11 = new byte[12];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x58, 0, 0, data11, data11.length, timeout);
//            textView.append(data11.toString()+ "\n");
//
//            vector<uint8_t> data = { 0x08, 0x00 };
//            vendor_transfer(0, 0x3E, 0, 0, data);
            byte[] data12 = {0x08, 0x00};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x3E, 0, 0, data12, data12.length, timeout);
//
//            vector<uint8_t> data(2);
//            vendor_transfer(1, 0x3d, 0, 0, data);
            byte[] data13 = new byte[2];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x3d, 0, 0, data13, data13.length, timeout);
//            textView.append(data13.toString()+ "\n");
//
//            vector<uint8_t> data = { 0x08, 0x00 };
//            vendor_transfer(0, 0x3E, 0, 0, data);
            byte[] data14 = {0x08, 0x00};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x3E, 0, 0, data14, data14.length, timeout);
//
//            vector<uint8_t> data = { 0x01, 0x00 };
//            vendor_transfer(0, 0x3C, 0, 0, data);
            byte[] data15 = {0x01, 0x00};
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x3C, 0, 0, data15, data15.length, timeout);
//
//            vector<uint8_t> data(2);
//            vendor_transfer(1, 0x3d, 0, 0, data);
            byte[] data16 = new byte[2];
            connection.controlTransfer(UsbConstants.USB_DIR_IN, 0x3d, 0, 0, data16, data16.length, timeout);
//            textView.append(data16.toString()+ "\n");

            //Start the frame transfer
            int imageWidth = 206;
            int imageHeight = 156;
            int imageSizeInPixels = imageWidth * imageHeight;
            byte[] data_framesize = {(byte)(imageSizeInPixels & 0xff), (byte)((imageSizeInPixels>>8) & 0xff), 0, 0 };
            connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x53, 0, 0, data_framesize, data_framesize.length, timeout);

            //Start the bulk transfer
            int bufsize = imageSizeInPixels * 2;
            int offset = 0;
            byte[] frameBuffer = new byte[bufsize];
            while(offset != bufsize) {
                int BTSize = connection.bulkTransfer(cameraUSBInterface.getEndpoint(0), frameBuffer, offset, (bufsize - offset), timeout);
                offset += BTSize;
                if(BTSize <= 0) {
                    textView.append("BTSize is <= 0. Error occurred.\n");
                    break;
                }
                else
                    textView.append("Got " + BTSize + " bytes.\n");
            }

            for(int i = 0; i < 100; i++ ){
                textView.append("" + frameBuffer[i]);
            }

            StringBuilder stringBuilder = new StringBuilder(bufsize);
            stringBuilder.append(frameBuffer);
            textView.append("\n" + stringBuilder.substring(0));
        }

        //Set the text view as the activity layout
        setContentView(textView);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetCamera(UsbDeviceConnection connection)
    {
        int timeout = 1000;
        byte[] data = {0x00, 0x00};
        connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x3C, 0, 0, data, data.length, timeout);
        connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x3C, 0, 0, data, data.length, timeout);
        connection.controlTransfer(UsbConstants.USB_DIR_OUT, 0x3C, 0, 0, data, data.length, timeout);
    }

}
