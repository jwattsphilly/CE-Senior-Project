package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by James Watts on 10/13/15.
 */
public class InstructionsActivity extends Activity {

    // TODO: Make everything visually appealing

    public static final int RUN_MICROWAVE_REQUEST = 4;
    public static final int RUN_ENJOY_REQUEST = 5;
    public static final int MICROWAVE_FINISHED_RESULT = 42;
    private Button selectButton = null;
    private TextView foodInfoView = null;
    private TextView instructionView = null;
    private boolean isMicrowavable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle foodItemBundle = intent.getBundleExtra("FoodItemBundle");

        String foodType = foodItemBundle.getString("Food_Type");
        String brandName = foodItemBundle.getString("Brand_Name");
        boolean frozen = foodItemBundle.getBoolean("Frozen");
        String frozenString = frozen ? " (Frozen)": " (Not frozen)";

        isMicrowavable = (!foodType.equals("Fork"));

        final String codedInstructionString = foodItemBundle.getString("Instructions");

        foodInfoView = new TextView(this);
        foodInfoView.setText(foodType + frozenString + "\n" + brandName);
        foodInfoView.setTextSize(25);

        instructionView = new TextView(this);
        instructionView.setText(LocalDatabase.parseInstructionString(codedInstructionString));

        selectButton = new Button(this);
        selectButton.setText("Select");
        selectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start a new MicrowaveRunningActivity
                        Intent microwaveRunningIntent = new Intent(InstructionsActivity.this, MicrowaveRunningActivity.class);
                        microwaveRunningIntent.putExtra("Coded Instruction", codedInstructionString);
                        startActivityForResult(microwaveRunningIntent, RUN_MICROWAVE_REQUEST);
                    }
                }
        );

        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        rootLayout.addView(foodInfoView,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));

        rootLayout.addView(instructionView,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));

        rootLayout.addView(selectButton,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        setContentView(rootLayout);
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


}
