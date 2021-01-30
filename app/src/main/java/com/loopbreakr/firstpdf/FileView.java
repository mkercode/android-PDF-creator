package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileView extends AppCompatActivity {

    private RecyclerView fileRecyclerView;
    private RowAdapter fileAdapter;
    private RecyclerView.LayoutManager fileLayoutManager;
    private EditText searchBar;
    private ArrayList<RowItem> rowItem;
    private List<File> fileList;
    private File fileListArr[];
    private final String filePath = "PDF_files";
    private int menuClicked = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);

        //get the file directory
        File file = new File(getExternalFilesDir(filePath).toString());
        //pass files in the directory to the sortfiles method
        fileListArr = sortfiles(file.listFiles());
        //list backwards to show newest entries first
        Collections.reverse(Arrays.asList(fileListArr));
        //create array
        fileList = Arrays.asList(fileListArr);

        createRows();
        buildRecyclerView();
    }

    public void createRows() {
        rowItem = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            rowItem.add(new RowItem(R.drawable.ic_book, (fileList.get(i).getName().replace('_', ' ').replace("&", " ").replace('_', '\n').replace('-', '/').replace(".pdf", ""))));
        }
    }

    public void removeItem(int position) {
        rowItem.remove(position);
        fileAdapter.notifyItemRemoved(position);
    }

    public void reListFiles() {
        File file = new File(getExternalFilesDir(filePath).toString());
        fileList = Arrays.asList(file.listFiles());
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
                if(menuClicked == 0){
                    String name = fileList.get(position).getPath();
                    Intent i = new Intent(FileView.this, PDFViewer.class);
                    i.putExtra("fileData", name);
                    startActivity(i);
                    //Toast.makeText(FileView.this, "Clicked: " + fileList, Toast.LENGTH_SHORT).show();
                }
                else{
                    menuClicked = 0;
                    String name = fileList.get(position).getPath();
                    Log.d("PATH: ",  name);
                }

            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);

                File deletePath = fileList.get(position);
                deletePath.delete();
                if (deletePath.exists()) {
                    getApplicationContext().deleteFile(deletePath.getName());
                }
                reListFiles();
            }

            @Override
            public void onMenuClick(int position) {
                menuClicked = 1;
            }
        });
    }

    public File[] sortfiles(File[] fileArray) {
        final File[] sortedFileName = fileArray;
        if (sortedFileName != null && sortedFileName.length > 1) {
            Arrays.sort(sortedFileName, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
        return fileArray;
    }
}

