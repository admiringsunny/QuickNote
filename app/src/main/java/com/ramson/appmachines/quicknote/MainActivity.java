package com.ramson.appmachines.quicknote;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    public static final String TAG = "MainActivity";
    public static final String FILE_NAME = "QUICK_NOTE_FILE.txt";
    public static final String FILE_SAVED = "file_saved";
    private EditText mainET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainET = (EditText) findViewById(R.id.main_et);
        mainET.addTextChangedListener(this);
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean fileSaved = prefs.getBoolean(FILE_SAVED, false);

        if (fileSaved) loadSaveFile();
        Button saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                saveFile();
                break;

        }
    }

    private void saveFile() {
        String text = mainET.getText() != null ? mainET.getText().toString() : "";


        Log.d(TAG, "Entered text: " + text);
        Log.d(TAG, "Clicked Save");

        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();

            Toast.makeText(MainActivity.this, getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();

            Log.d(TAG, "Text saved to internal storage");
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File Not Found: " + FILE_NAME);
        } catch (IOException e) {
            Log.e(TAG, "File Not Written: " + FILE_NAME);
        }

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(FILE_SAVED, true).apply();

    }

    private void loadSaveFile() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME); // reads bytes not text
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(new DataInputStream(fis))); // reads the text from a character-based input stream

            /** read line by line */
            EditText mainET = (EditText) findViewById(R.id.main_et);
            String line;
            while ((line = reader.readLine()) != null) {
                mainET.append(line + "\n");
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "File not found: " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Unable to read file: " + FILE_NAME);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveFile();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mainET.getText() != null && mainET.getText().length() >= getResources().getInteger(R.integer.max_length))
            Toast.makeText(this, "Max length exceeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        
    }
}