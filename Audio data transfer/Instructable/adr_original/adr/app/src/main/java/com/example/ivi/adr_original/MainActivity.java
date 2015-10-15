package com.example.ivi.adr_original;





import android.app.Fragment;
import android.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {
    private EditText password;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new Sketch(getApplicationContext());
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        addListenerOnButton();

    }
    public void addListenerOnButton() {

        password = (EditText)findViewById(R.id.editText);
        btnSubmit = (Button) findViewById(R.id.button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Updated",
                        Toast.LENGTH_SHORT).show();


                Sketch.karli(password.getText().toString());
            }

        });

    }


}

