package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.sql.SQLException;

/**
 * Created by Darin on 7/25/15.
 */
public class SearchByName extends Activity {
    public final static String SEARCH_STRING = "nocomment.smartthermalmicrowave.EXTRA_SEARCH_STRING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void executeSearch(View view)
    {
        Intent intent = new Intent(this, DisplaySearchResultsSBN.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(SEARCH_STRING, message);
        startActivity(intent);
    }
}
