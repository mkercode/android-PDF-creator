package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileView extends AppCompatActivity {

    private RecyclerView fileRecyclerView;
    private RowAdapter fileAdapter;
    private RecyclerView.LayoutManager fileLayoutManager;
    private ArrayList<RowItem> rowItem;
    private List<File> fileList;
    private final String filePath = "PDF_files";
    private String fileData = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        File file = new File(getExternalFilesDir(filePath).toString());
        fileList = Arrays.asList(file.listFiles());
        createRows();
        buildRecyclerView();
    }

    public void createRows(){
        //todo: create hashmap
        // add the values of File [] file and fileList.get ect
        // sort it alphanumerically
        // populate rowItem with the String keys
        // modify onClick methods to remove files from the system using values in the hashmap from on their String keys

        rowItem = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            rowItem.add(new RowItem(fileList.get(i).getName().replace("__", " ").replace('_','\n').replace('-','/').replace(".pdf","")));
        }
    }

    public void removeItem(int position) {
        rowItem.remove(position);
        fileAdapter.notifyItemRemoved(position);
    }

    public void reListFiles(){
        File file = new File(getExternalFilesDir(filePath).toString());
        fileList = Arrays.asList(file.listFiles());
    }

    public void sendAsMail(String file) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");

        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sample Subject"); //set your subject
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Sample Text"); //set your message

        String filePath = file;
        File fileToShare = new File(filePath);
        Uri uri = Uri.fromFile(fileToShare);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share File"));
    }


    public void buildRecyclerView() {

        fileRecyclerView = findViewById(R.id.recyclerView);
        fileRecyclerView.setHasFixedSize(true);
        fileLayoutManager = new LinearLayoutManager(this);
        fileAdapter = new RowAdapter(rowItem);
        fileRecyclerView.setLayoutManager(fileLayoutManager);
        fileRecyclerView.setAdapter(fileAdapter);

        fileAdapter.setOnItemClickListener(new RowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                fileData = fileList.get(position).toString();
                //    Toast.makeText(FileView.this,"Clicked: " + fileData , Toast.LENGTH_SHORT).show();
                sendAsMail(fileData);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);

                File deletePath = fileList.get(position);
                deletePath.delete();
                if(deletePath.exists()){
                    getApplicationContext().deleteFile(deletePath.getName());
                }
                reListFiles();
            }

            @Override
            public void onMenuClick(int position) {
            }
        });
    }
}

