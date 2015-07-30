package nocomment.smartthermalmicrowave;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "nocomment.smartthermalmicrowave.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    //Activity setup for Browse by Item
    public void browseByItem(View view){
        Intent intent = new Intent(this, BrowseByItem.class);
        String message = "Browse By Item Button Pressed";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    //Activity setup for Scan UPC
    public void scanUPC(View view) {
        Intent intent = new Intent(this, ScanUPC.class);
        String message = "Scan UPC Button Pressed";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    //Activity setup for Search By Name
    public void searchByName(View view){
        Intent intent = new Intent(this, SearchByName.class);
        String message = "Search By Name Button Pressed";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
