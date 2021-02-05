package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

public class PdfEditor extends AppCompatActivity {

    String filePath;
    String fileName;
    EditText pdfEditText;
    File pdfFile;
    PdfReader pdfReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_editor);

        showToolBar();
        getPDF();
        readPDF();
        parsePDF();
    }

    private void showToolBar() {
        //find and set searchbar
        Toolbar editMenuBar = findViewById(R.id.editor_toolbar);
        setSupportActionBar(editMenuBar);
        //clear title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //show back button
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //onclick listener for items
        editMenuBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getPDF() {
        Intent intent = getIntent();
        filePath = intent.getExtras().getString("fileData");
    }

    private void readPDF() {
        pdfFile = new File(filePath);

        pdfReader = null;
        try {
            pdfReader = new PdfReader(new FileInputStream(pdfFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parsePDF() {
        PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
        StringWriter pdfStringWriter = new StringWriter();
        SimpleTextExtractionStrategy stretegy = null;
        try {
            stretegy = parser.processContent(1, new SimpleTextExtractionStrategy());
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfStringWriter.write(stretegy.getResultantText());
        String pdfContents = pdfStringWriter.toString();
        //set extracted text from pdf file
        //to Edit-text
        pdfEditText = findViewById(R.id.pdf_edit_text);
        pdfEditText.setText(pdfContents);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.finishEdit) {
            saveFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFile() {

        fileName = pdfFile.getName();
        recreateMyPDF(pdfEditText.getText().toString());
        exitActivity();
    }

    private void exitActivity(){
        Intent intent = new Intent(this, PDFViewer.class);
        intent.putExtra("fileData", filePath);
        startActivity(intent);
    }

    public void recreateMyPDF(String myString){

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
            File myExternalFile = new File(getExternalFilesDir("PDF_files"), fileName);
            try {
                myPdfDocument.writeTo(new FileOutputStream(myExternalFile));
                Toast.makeText(PdfEditor.this,"File saved!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                //If file is not saved, print stack trace and display toast message
                e.printStackTrace();
                Toast.makeText(PdfEditor.this,"File not saved... Possible permissions error", Toast.LENGTH_SHORT).show();
            }
            myPdfDocument.close();
    }

}