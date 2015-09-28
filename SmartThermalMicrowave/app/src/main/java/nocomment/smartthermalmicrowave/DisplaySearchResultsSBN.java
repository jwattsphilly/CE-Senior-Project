package nocomment.smartthermalmicrowave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
        TextView results = (TextView) findViewById(R.id.search_results);
        results.setTextSize(22);

        List<FoodItem> resultsReturned = new ArrayList<FoodItem>();

        String searchString = intent.getStringExtra(SearchByName.SEARCH_STRING);
        resultsReturned = LocalDatabase.getMatches(searchString);

        if (resultsReturned.size() == 0)
            results.setText("Doh! No results found.");

        for (FoodItem item : resultsReturned) {
            results.append(item.toString() + "\n");
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
