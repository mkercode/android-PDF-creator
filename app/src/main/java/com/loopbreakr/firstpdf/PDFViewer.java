package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;


import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;

public class PDFViewer extends AppCompatActivity {

    String filePath;
    PDFView pdfView;
    File pdfFile;
    private Menu pdfMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfView = findViewById(R.id.pdfView);

        //Get the pdf data from the previous activity
        Intent intent = getIntent();
        filePath = intent.getExtras().getString("fileData");

        //display the toolbar
        Toolbar myToolbar = findViewById(R.id.PDFToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //show PDF in Viewer
        pdfFile = new File(filePath);
        pdfView.fromFile(pdfFile).load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewer_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }
}