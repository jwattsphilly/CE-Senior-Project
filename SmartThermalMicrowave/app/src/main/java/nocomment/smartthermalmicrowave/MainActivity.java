package nocomment.smartthermalmicrowave;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.sql.SQLException;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "nocomment.smartthermalmicrowave.MESSAGE";
    public final static String SEARCH_STRING = "nocomment.smartthermalmicrowave.EXTRA_SEARCH_STRING";

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
            Log.i(getLocalClassName(), "Updated the food list");
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        // Remember that you should never show the action bar if the
//        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        databaseThread.start();

        while(LocalDatabase.foodList.isEmpty()){}   //TODO This is a bad plan...

        UsbSingleton.initUSB(this);

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

    //Activity setup for Scan UPC
    public void scanUPC(View view) {
        Intent intent = new Intent(this, ScanUPC.class);
        String message = "Scan UPC Button Pressed";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    //Activity setup for Search By Name
    public void searchByName(View view) {
        Intent intent = new Intent(this, SearchByName.class);
        String message = "Search By Name Button Pressed";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    //Activity setup for Manual Control
    public void manualControl(View view){
        Intent intent = new Intent(this, ManualMicrowaveControl.class);
        String message = "Manual Microwave Control Button Pressed";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
