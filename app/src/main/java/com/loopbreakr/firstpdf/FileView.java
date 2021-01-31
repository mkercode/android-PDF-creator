package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileView extends AppCompatActivity {

    private RecyclerView fileRecyclerView;
    private RowAdapter fileAdapter;
    private RecyclerView.LayoutManager fileLayoutManager;
    private ArrayList<RowItem> rowItem;
    private File[] files;
    private File tempFile;
    private List<File> fileList;
    private final String filePath = "PDF_files";
    private File fileData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        File file = new File(getExternalFilesDir(filePath).toString());
        files = file.listFiles();
        fileList = Arrays.asList(files);

        createRows();
        buildRecyclerView();
    }

    public void createRows(){
        rowItem = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            rowItem.add(new RowItem(tempFile.getName().replace("__", " ").replace('_','\n').replace('-','/').replace(".pdf",""), tempFile));
        }
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
                fileData = rowItem.get(position).getFileData();
                Toast.makeText(FileView.this,"Clicked: " + fileData.getPath() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {

                fileData = rowItem.get(position).getFileData();
                fileData.delete();
                Toast.makeText(FileView.this,"Deleted: " + fileData.getPath() , Toast.LENGTH_SHORT).show();

                removeItem(position);
            }

            @Override
            public void onMenuClick(int position) {

            }
        });
    }

    public void removeItem(int position) {
        rowItem.remove(position);
        fileAdapter.notifyItemRemoved(position);
    }


}

