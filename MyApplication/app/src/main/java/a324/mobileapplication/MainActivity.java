package a324.mobileapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnFileChoose;
    private Button btnFileHashing;
    private Button btnValidate;
    private Button btnCheckVersion;

    private TextView textfileName;
    private static final int READ_REQUEST_CODE = 42;
    //private FileChooser fileChooserObj;
    Intent intent;
    MainActivity mainObj = this;

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
        /*
        btnFileHashing.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        }));
        */
        /*
        btnValidate.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        }));
        */
        /*
        btnCheckVersion.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        }));
        */
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

            Uri uri = null;
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
