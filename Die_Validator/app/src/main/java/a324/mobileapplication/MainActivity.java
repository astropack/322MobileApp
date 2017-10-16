package a324.mobileapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Button btnFileChoose;
    private Button btnValidate;
    private Button btnShowValidatedFiles;
    private TextView textfileName;

    private TextView tName;
    private TextView tSize;
    private TextView tMime;
    private TextView tPath;

    //private String strSubFiles = "submitted_files";   //these are if we have default file names for write/read
    //private String strTmpTxt = "tmp";

    private static final int READ_REQUEST_CODE = 42;    //used to browse for a file
    private Intent intent;
    private Uri uri = null;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Assign buttons and views of GUI to objects in this class:
        btnFileChoose = (Button) findViewById(R.id.btnChooseFile);
        btnValidate = (Button) findViewById(R.id.btnValidate);
        textfileName = (TextView) findViewById(R.id.textViewFileName);
        btnShowValidatedFiles = (Button) findViewById(R.id.btnShowValidatedFiles);

        tName = (TextView) findViewById(R.id.textViewName);
        tSize = (TextView) findViewById(R.id.textViewSize);
        tMime = (TextView) findViewById(R.id.textViewMimeType);
        tPath = (TextView) findViewById(R.id.textViewPath);

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

        btnValidate.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!path.equals(""))
                {
                    writeSubmittedFiles();
                    Intent splashS = new Intent(MainActivity.this, SplashScreen.class).putExtra("<StringName>", path);
                    startActivity(splashS);
                    path = "";
                }
            }
        }));

        btnShowValidatedFiles.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readSubmittedFiles();
            }
        }));
    }

    private void writeSubmittedFiles() {    //currently only writes the file paths to a file
        if (uri != null)
        {
            //String name = "" + uri;
            String fileName = "submitted_files";

            FileOutputStream fos;
            try {
                fos = openFileOutput(fileName, Context.MODE_APPEND);
                fos.write((path + "\n").getBytes());
                fos.close();
                Toast.makeText(MainActivity.this, "Saving uri to file", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //path = "";
        }
    }

    private void readSubmittedFiles()   //show list of files validated in a toast
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
        //intent.setType("*/*");
        intent.setType("application/pdf");  //Only pdf's can be selected
        //intent.addFlags(intent.FLAG_GRANT_READ_URI_PERMISSION);
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

                //Get the name of the file:
                Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                //int pa = returnCursor.getColumnCount();
                returnCursor.moveToFirst();
                String mimeType = getContentResolver().getType(uri);

                String name = returnCursor.getString(nameIndex);
                tName.setText("Name: " + returnCursor.getString(nameIndex));
                tSize.setText("Size: " + sizeIndex);
                tMime.setText("Mime type: " + mimeType);

                path = uri.getScheme() + "://" + uri.getAuthority() +"/"+ name; //Get path of file from URI
                tPath.setText(path);
                //test if File object can be created:
                /*
                tPath.setText(path);
                File f = null;
                try {
                    f = new File(path);
                }catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "File error", Toast.LENGTH_SHORT).show();
                }
                */
            }
        }
    }
}