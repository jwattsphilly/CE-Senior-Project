package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.app.ListActivity;
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
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Darin on 7/25/15.
 */
public class BrowseByItem extends ActionBarActivity {
//    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
//    UsbEndpoint input, output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_by_item);

        List<FoodItem> resultsReturned = new ArrayList<FoodItem>();

        String searchString = "";   //Empty string returns all items
        resultsReturned = LocalDatabase.getMatches(searchString);

        if (resultsReturned.size() == 0){
            List<String> resultsReturnedString = new ArrayList<String>();
            resultsReturnedString.add("Doh! No results found.");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultsReturnedString);
            ListView listView = (ListView) findViewById(R.id.browse_results);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
                    TextView clickedView = (TextView)view;

                    Toast.makeText(BrowseByItem.this, "Item with id [" + id + "] - Position [" + position + "] - Planet [" + clickedView.getText() + "]", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            ArrayAdapter<FoodItem> arrayAdapter = new ArrayAdapter<FoodItem>(this, android.R.layout.simple_list_item_1, resultsReturned);
            final ListView listView = (ListView) findViewById(R.id.browse_results);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {

                    FoodItem selectedFood = (FoodItem) listView.getItemAtPosition(position);

                    Intent intention = new Intent(BrowseByItem.this, InstructionsActivity.class);

                    Bundle foodItemBundle = new Bundle();
                    foodItemBundle.putString("Food_Type", selectedFood.getFoodType());
                    foodItemBundle.putString("Brand_Name", selectedFood.getBrandName());
                    foodItemBundle.putBoolean("Frozen", selectedFood.getFrozen());
                    foodItemBundle.putString("Instructions", selectedFood.getInstructions());

                    intention.putExtra("FoodItemBundle", foodItemBundle);

                    startActivity(intention);
                }
            });
        }
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
}
