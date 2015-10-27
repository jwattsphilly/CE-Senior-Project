package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Darin on 10/13/15.
 */
public class ManualMicrowaveControl extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.microwave_manual_control);
    }

    public void pressButton(View view)
    {
        switch(view.getId())
        {
            case R.id.button0:
                UsbSingleton.sendDataUSB("b0");
                break;
            case R.id.button1:
                UsbSingleton.sendDataUSB("b1");
                break;
            case R.id.button2:
                UsbSingleton.sendDataUSB("b2");
                break;
            case R.id.button3:
                UsbSingleton.sendDataUSB("b3");
                break;
            case R.id.button4:
                UsbSingleton.sendDataUSB("b4");
                break;
            case R.id.button5:
                UsbSingleton.sendDataUSB("b5");
                break;
            case R.id.button6:
                UsbSingleton.sendDataUSB("b6");
                break;
            case R.id.button7:
                UsbSingleton.sendDataUSB("b7");
                break;
            case R.id.button8:
                UsbSingleton.sendDataUSB("b8");
                break;
            case R.id.button9:
                UsbSingleton.sendDataUSB("b9");
                break;
            case R.id.buttonStart:
                UsbSingleton.sendDataUSB("s");
                break;
            case R.id.buttonStop:
                UsbSingleton.sendDataUSB("S");
                break;
            default:
                break;
        }

    }
}
