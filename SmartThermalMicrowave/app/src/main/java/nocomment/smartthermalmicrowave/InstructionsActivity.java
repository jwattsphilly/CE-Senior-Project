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

    private Button selectButton = null;
    private TextView foodInfoView = null;
    private TextView instructionView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Test all of this!

        Intent intent = getIntent();
        Bundle foodItemBundle = intent.getBundleExtra("FoodItemBundle");

        String foodType = foodItemBundle.getString("Food_Type");
        String brandName = foodItemBundle.getString("Brand_Name");
        boolean frozen = foodItemBundle.getBoolean("Frozen");
        String frozenString = frozen ? " (Frozen)": " (Not frozen)";

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
                        Intent sendIntent = new Intent(InstructionsActivity.this, MicrowaveRunningActivity.class);
                        sendIntent.putExtra("Coded Instruction", codedInstructionString);
                        startActivity(sendIntent);
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
}
