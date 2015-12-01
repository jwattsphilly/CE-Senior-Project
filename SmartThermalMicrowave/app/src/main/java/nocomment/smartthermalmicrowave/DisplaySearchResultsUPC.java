//package nocomment.smartthermalmicrowave;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Darin on 9/23/15.
// */
//public class DisplaySearchResultsUPC extends Activity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display_search_results);
//
//        Intent intent = getIntent();
//
//        List<FoodItem> resultsReturned = new ArrayList<FoodItem>();
//
//        try {
//            //If searchString was a long (UPC)
//            String searchString = intent.getStringExtra(MainActivity.SEARCH_STRING);
//            if(searchString != null) {
//                long UPC = Long.parseLong(searchString);
//                resultsReturned = LocalDatabase.getMatches(UPC);
//            }
//        }
//        catch(NumberFormatException nfe) {
//            throw new NumberFormatException("Invalid UPC.");
//        }
//
//        if (resultsReturned.size() == 0){
//            List<String> resultsReturnedString = new ArrayList<String>();
//            resultsReturnedString.add("Doh! No results found.");
//
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, resultsReturnedString);
//            ListView listView = (ListView) findViewById(R.id.search_results);
//            listView.setAdapter(arrayAdapter);
//        }
//        else
//        {
//            ArrayAdapter<FoodItem> arrayAdapter = new ArrayAdapter<FoodItem>(this, android.R.layout.simple_list_item_1, resultsReturned);
//            final ListView listView = (ListView) findViewById(R.id.search_results);
//            listView.setAdapter(arrayAdapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id) {
//
//                    FoodItem selectedFood = (FoodItem) listView.getItemAtPosition(position);
//
//                    Intent intention = new Intent(DisplaySearchResultsUPC.this, InstructionsActivity.class);
//
//                    Bundle foodItemBundle = new Bundle();
//                    foodItemBundle.putString("Food_Type", selectedFood.getFoodType());
//                    foodItemBundle.putString("Brand_Name", selectedFood.getBrandName());
//                    foodItemBundle.putBoolean("Frozen", selectedFood.getFrozen());
//                    foodItemBundle.putString("Instructions", selectedFood.getInstructions());
//
//                    intention.putExtra("FoodItemBundle", foodItemBundle);
//
//                    startActivity(intention);
//                }
//            });
//        }
//
//
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
