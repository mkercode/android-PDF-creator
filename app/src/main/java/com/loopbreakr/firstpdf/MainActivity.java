package com.loopbreakr.firstpdf;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText myEditText;
    private String fileName = "";
    private final String filePath = "PDF_files";
    private int STORAGE_PERMISSION_CODE = 1;
    private boolean isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEditText = findViewById(R.id.editText);
        Button myButton = findViewById(R.id.button);
        Button viewButton = findViewById(R.id.viewFiles);


        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Do nothing, we granted the permission already
        } else {
            requestStoragePermission();
        }

        myButton.setOnClickListener(v -> {
            //Get time and date
            String currentDate = new SimpleDateFormat("yyyy-MM-dd&HH:mm:ss").format(new Date());

            //Append date and tinme to filename
            fileName = "/Abdile_Name__" + currentDate + ".pdf";

            //Call the createPDF method on the user input
            createMyPDF(myEditText.getText().toString());
        });

        viewButton.setOnClickListener((v -> {
            myEditText.setText(getExternalFilesDir(filePath).toString());
            File file = new File(getExternalFilesDir(filePath).toString());
            File[] fileList = file.listFiles();

            for (int i = 0; i < fileList.length; i++)
            {
                String name = fileList[i].getName();
                Log.d("FILE:", fileList[i].getName() + " " + fileList[i].getAbsolutePath());
            }
//                Start second activity
            Intent k = new Intent(MainActivity.this, FileView.class);
            startActivity(k);

        }));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createMyPDF(String myString){

        //Create the pdf page
        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Paint myPaint = new Paint();

        //Initialize top and left margin for text
        int x = 10, y=25;

        //Paint the string to the page
        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }

        //Finish writing/painting on the page
        myPdfDocument.finishPage(myPage);

        //Initialize the file with the name and path
        File myExternalFile = new File(getExternalFilesDir(filePath), fileName);
        try {
            myPdfDocument.writeTo(new FileOutputStream(myExternalFile));
            Toast.makeText(MainActivity.this,"File saved!", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            //If file is not saved, print stack trace, clear edittext, and display toast message
            e.printStackTrace();
            myEditText.setText("");
            Toast.makeText(MainActivity.this,"File not saved... Possible permissions error", Toast.LENGTH_SHORT).show();
        }

        myPdfDocument.close();
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}