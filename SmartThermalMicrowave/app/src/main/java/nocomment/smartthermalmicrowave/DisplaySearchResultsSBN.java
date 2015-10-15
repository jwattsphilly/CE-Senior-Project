package nocomment.smartthermalmicrowave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darin on 9/16/15.
 */
public class DisplaySearchResultsSBN extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);

        Intent intent = getIntent();

        List<FoodItem> resultsReturned = new ArrayList<FoodItem>();

        String searchString = intent.getStringExtra(SearchByName.SEARCH_STRING);
        resultsReturned = LocalDatabase.getMatches(searchString);

        if (resultsReturned.size() == 0){
            List<String> resultsReturnedString = new ArrayList<String>();
            resultsReturnedString.add("Doh! No results found.");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, resultsReturnedString);
            ListView listView = (ListView) findViewById(R.id.search_results);
            listView.setAdapter(arrayAdapter);
        }
        else
        {
            ArrayAdapter<FoodItem> arrayAdapter = new ArrayAdapter<FoodItem>(this, android.R.layout.simple_expandable_list_item_1, resultsReturned);
            ListView listView = (ListView) findViewById(R.id.search_results);
            listView.setAdapter(arrayAdapter);
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
