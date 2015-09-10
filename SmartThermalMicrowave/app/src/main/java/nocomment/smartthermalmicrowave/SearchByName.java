package nocomment.smartthermalmicrowave;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;

/**
 * Created by Darin on 7/25/15.
 */
public class SearchByName extends FragmentActivity {

    //TODO Change the priority on this thread so that it doesn't interfere with other important processes
    Thread databaseThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                LocalDatabase.populateFoodList();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(LocalDatabase.foodList.isEmpty())
                Log.w(getLocalClassName(), "No data returned from remote database. Dang.");
            Log.w(getLocalClassName(), "Updated the food list");
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseThread.start();

//        while(LocalDatabase.foodList.isEmpty()){}   //TODO This is a bad plan...
//
//        TextView textView = new TextView(this);
//        for (FoodItem item: LocalDatabase.foodList)
//            textView.append(item.toString() + "\n");

        setContentView(R.layout.activity_search_by_name);

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
