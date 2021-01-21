package com.loopbreakr.firstpdf;

import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText myEditText;
    private Button myButton;
    String fileName = "";
    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEditText = findViewById(R.id.editText);
        myButton = findViewById(R.id.button);
        filePath = "PDF_files";

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get time and date
                String currentDate = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss").format(new Date());

                //Append date and tinme to filename
                fileName = "/PDFFile_" + currentDate + ".pdf";

                //Call the createPDF method on the user input
                createMyPDF(myEditText.getText().toString());
            }
        });

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
}