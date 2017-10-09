package a324.mobileapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Button btnFileChoose;
    private Button btnFileHashing;
    private Button btnValidate;
    private Button btnCheckVersion;
    private Button btnShowValidatedFiles;

    private TextView textfileName;
    private static final int READ_REQUEST_CODE = 42;
    //private FileChooser fileChooserObj;
    private Intent intent;
    MainActivity mainObj = this;
    private Uri uri = null;

    test2 t2 = new test2();     //test2.java is still in progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Assign buttons and views of GUI to objects in this class:
        btnFileChoose = (Button) findViewById(R.id.btnChooseFile);
        btnFileHashing = (Button) findViewById(R.id.btnFileHashing);
        btnValidate = (Button) findViewById(R.id.btnValidate);
        btnCheckVersion = (Button) findViewById(R.id.btnCheckVersion);
        textfileName = (TextView) findViewById(R.id.textViewFileName);
        btnShowValidatedFiles = (Button) findViewById(R.id.btnShowValidatedFiles);
        enableButtonsClick();
    }
    //Activate buttons for clicking:
    private void enableButtonsClick() {


        btnFileChoose.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showChooser();
            }
        }));

        //Hashing of the file by using test2 class:
        btnFileHashing.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "Hashing should happen now", Toast.LENGTH_SHORT).show();
                //t2.convertPDF();
            }
        }));


        btnValidate.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write a record for the "file validated" to a file on the device to reference all submitted files.
                writeSubmittedFiles();
                SplashScreen sc = new SplashScreen();
            }
        }));

        /*
        btnCheckVersion.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        }));
        */

        btnShowValidatedFiles.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readSubmittedFiles();
            }
        }));
    }

    private void writeSubmittedFiles() {
        if (uri != null)
        {
            String uriStr = "" + uri;
            String fileName = "submitted_files";

            FileOutputStream fos;
            try {
                fos = openFileOutput(fileName, Context.MODE_APPEND);
                fos.write((uriStr + "\n").getBytes());  //currently only writes the URIs to a file
                fos.close();
                Toast.makeText(MainActivity.this, "Saving uri to file", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            uri = null;
        }
    }

    private void readSubmittedFiles()
    {
        try {
            String message = "";
            FileInputStream fis = openFileInput("submitted_files");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bfr = new BufferedReader(isr);
            StringBuffer strBuffer = new StringBuffer();
            while ((message = bfr.readLine()) != null)
            {
                strBuffer.append(message + "\n");
            }
            Toast.makeText(MainActivity.this, strBuffer.toString(), Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Open a file explorer to select a file:
    private void showChooser() {

        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        Toast.makeText(MainActivity.this, "Opening file browser", Toast.LENGTH_SHORT).show();
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    //Select a file, then show the URI of the file:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            //uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                textfileName.setText("URI: " + uri);

                //Do something with the document (open, send...):
                /*
                File file = new File(uri.getPath());
                Intent intentOpen = new Intent(Intent.ACTION_VIEW);
                intentOpen.setData(Uri.fromFile(file));
                startActivity(intentOpen);
                */
            }
        }
    }
}