package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

        getData();
        showMenuBar();
        showPDF();
    }

    private void getData() {
        Intent intent = getIntent();
        filePath = intent.getExtras().getString("fileData");
    }

    private void showMenuBar() {
        //find and set searchbar
        Toolbar viewMenuBar = findViewById(R.id.PDFToolbar);
        setSupportActionBar(viewMenuBar);
        //clear title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //show back button
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewMenuBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showPDF() {
        pdfView = findViewById(R.id.pdfView);
        pdfFile = new File(filePath);
        pdfView.fromFile(pdfFile).load();
    }

    public void exitActiviity(){
        Intent openFileViewer = new Intent(PDFViewer.this, FileView.class);
        startActivity(openFileViewer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewer_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.editPDF) {
            openEditor(filePath);
            return true;
        }
        else if(id == R.id.deletePDF) {
            pdfFile.delete();
            exitActiviity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openEditor(String path) {
        Intent intent = new Intent(this, PdfEditor.class);
        intent.putExtra("fileData", path);
        startActivity(intent);
    }
}
