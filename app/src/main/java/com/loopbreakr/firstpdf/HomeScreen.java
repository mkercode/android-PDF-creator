package com.loopbreakr.firstpdf;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.loopbreakr.firstpdf.FileViewer.FileView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeScreen extends AppCompatActivity {

    private EditText myEditText;
    private Button saveButton;
    private Button viewButton;
    private String fileName = "";
    private String currentDate;
    private final String filePath = "PDF_files";
    private int STORAGE_PERMISSION_CODE = 1;
    private boolean isSelected;

    private BaseFont bfBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(HomeScreen.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Do nothing, we granted the permission already
        } else {
            requestStoragePermission();
        }
    }

    private void findViews() {
        myEditText = findViewById(R.id.editText);
        saveButton = findViewById(R.id.button);
        viewButton = findViewById(R.id.viewFiles);
    }

    private void setListeners() {
        saveButton.setOnClickListener(v -> {
            //Get time and date
            currentDate = new SimpleDateFormat("yyyy-MM-dd__HH:mm:ss").format(new Date());
            //Append date and tinme to filename
            fileName = "/File_Name__" + currentDate + ".pdf";
            //Call the createPDF method on the user input
            try {
                createMyPDF(myEditText.getText().toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });

        viewButton.setOnClickListener((v -> {
//                Start second activity
            Intent openFileViewer = new Intent(HomeScreen.this, FileView.class);
            startActivity(openFileViewer);
            finish();
        }));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createMyPDF(String myString) throws FileNotFoundException, DocumentException {

        //Initialize the file with the name and path
        File pdfFile = new File(getExternalFilesDir(filePath), fileName);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, output);
        document.open();

        //set fonts
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 30.0f, Font.NORMAL, BaseColor.BLACK);
        Font regularFont = new Font(Font.FontFamily.HELVETICA, 20.0f, Font.NORMAL, BaseColor.DARK_GRAY);
        Font questionFont = new Font(Font.FontFamily.HELVETICA, 20.0f, Font.UNDERLINE, BaseColor.DARK_GRAY);
        Font seperatorFont = new Font(Font.FontFamily.HELVETICA, 20.0f, Font.NORMAL, BaseColor.WHITE);

        document.add(new Paragraph("This is your input:", titleFont));
        document.add(new Paragraph("///n", seperatorFont));
        document.add(new Paragraph("What is your input?",questionFont));
        document.add(new Paragraph("///n", seperatorFont));
        document.add(new Paragraph(myString, regularFont));
        document.add(new Paragraph("///n", seperatorFont));
        document.add(new Paragraph("Thanks! This was made possible with Itext and Barteksc", regularFont));

        document.close();

        Toast.makeText(HomeScreen.this,"File saved!", Toast.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(HomeScreen.this,
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //ITEXT IMPLEMENTATION
    private void initializeFonts(){
        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}