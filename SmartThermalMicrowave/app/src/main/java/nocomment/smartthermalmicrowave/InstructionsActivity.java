package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by James Watts on 10/13/15.
 */
public class InstructionsActivity extends Activity {

    public static final int RUN_MICROWAVE_REQUEST = 4;
    public static final int RUN_ENJOY_REQUEST = 5;
    public static final int MICROWAVE_FINISHED_RESULT = 42;
    private Button selectButton = null;
    private TextView foodInfoView = null;
    private TextView instructionView = null;
    private ImageView foodImage = null;
    private boolean isMicrowavable = true;
    private String codedInstructionString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle foodItemBundle = intent.getBundleExtra("FoodItemBundle");

        String foodType = foodItemBundle.getString("Food_Type");
        String brandName = foodItemBundle.getString("Brand_Name");
        byte[] imageArray = foodItemBundle.getByteArray("Image_Byte_Array");

        isMicrowavable = (!foodType.equals("Fork"));
        codedInstructionString = foodItemBundle.getString("Instructions");

        setContentView(R.layout.activity_instructions);

        foodInfoView = (TextView) findViewById(R.id.text_view_food_info);
        foodInfoView.setText(foodType + "\n" + brandName);

        instructionView = (TextView) findViewById(R.id.text_view_instruction);
        instructionView.setText(LocalDatabase.parseInstructionString(codedInstructionString));

        foodImage = (ImageView) findViewById(R.id.image_view_food_image);

        if(imageArray == null)
            foodImage.setImageResource(R.drawable.microwave);

        else
        {
            Bitmap foodBitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            foodImage.setImageBitmap(foodBitmap);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        UsbSingleton.sendDataUSB("SS");// Double click the STOP button to cancel out of anything

        // Once the MicrowaveRunningActivity timer is closed due to the timer hitting zero, start the EnjoyActivity
        // This code is only run when the timer gets to zero and not just when "back" is pressed
        if (requestCode == RUN_MICROWAVE_REQUEST && resultCode == MICROWAVE_FINISHED_RESULT) {
            Intent enjoyIntent = new Intent(this, EnjoyActivity.class);
            enjoyIntent.putExtra("Microwavable", isMicrowavable);
            startActivityForResult(enjoyIntent, RUN_ENJOY_REQUEST);
        }
    }

    public void selectButtonOnClick(View v)
    {
        Intent microwaveRunningIntent = new Intent(InstructionsActivity.this, MicrowaveRunningActivity.class);
        microwaveRunningIntent.putExtra("Coded Instruction", codedInstructionString);
        startActivityForResult(microwaveRunningIntent, RUN_MICROWAVE_REQUEST);
    }
}
