package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
    private final String filePath = "PDF_files";
    private int menuClicked = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);

        //get the file directory
        File file = new File(getExternalFilesDir(filePath).toString());
        //pass files in the directory to the sortfiles method
        File[] fileListArr = sortFiles(file.listFiles());
        //Create array and list backwards to show newest entries first
        fileList = Arrays.asList(fileListArr);
        Collections.reverse(fileList);


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
                //Start the pdf viewer activity if the item is not greyed out
                if(menuClicked == 0){
                    String name = fileList.get(position).getPath();
                    Intent i = new Intent(FileView.this, PDFViewer.class);
                    i.putExtra("fileData", name);
                    startActivity(i);
                }
                else{
                    //ensures that the user can not initiate the item click if the item is greyed out
                    menuClicked = 0;
                }

            }

            @Override
            public void onDeleteClick(int position) {
                //remove the entry from the arraylist (removes from view) and path from file system (deletes from device
                removeItem(position);
                File deletePath = fileList.get(position);
                deletePath.delete();
                if (deletePath.exists()) {
                    getApplicationContext().deleteFile(deletePath.getName());
                }
                //relist files in arraylist to match the files in the file system
                //this ensures future deletes while the activity is open will actually remove the file at the position of the array
                reListFiles();
            }

            @Override
            public void onMenuClick(int position) {
                //ensures that the user can not initiate the item click if the item is greyed out
                menuClicked = 1;
            }
        });
    }

    public File[] sortFiles(File[] fileArray) {
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

