package com.loopbreakr.firstpdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private List<File> fileList;
    private final String filePath = "PDF_files";
    private String fileData = "";
    private boolean isClicked;

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
        rowItem = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            rowItem.add(new RowItem(R.drawable.ic_book,(fileList.get(i).getName().replace("__", " ").replace('_','\n').replace('-','/').replace(".pdf",""))));
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

    public void showMenu(){


    }
    public void hideMenu(){

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
                Toast.makeText(FileView.this,"Clicked: " + fileData , Toast.LENGTH_SHORT).show();
//                if(isClicked == true){
//                    Button rowDeleteButton = fileRecyclerView.findViewById(R.id.deleteFile);
//                    Button rowSendButton = fileRecyclerView.findViewById(R.id.sendButton);
//                    Button rowMenuButton = fileRecyclerView.findViewById(R.id.menuButton);
//                    rowDeleteButton.setVisibility(View.INVISIBLE);
//                    rowSendButton.setVisibility(View.INVISIBLE);
//                    rowMenuButton.setVisibility(View.VISIBLE);
//                }
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
//                isClicked = true;
//                Button rowDeleteButton = fileRecyclerView.findViewById(R.id.deleteFile);
//                Button rowSendButton = fileRecyclerView.findViewById(R.id.sendButton);
//                Button rowMenuButton = fileRecyclerView.findViewById(R.id.menuButton);
//                rowDeleteButton.setVisibility(View.VISIBLE);
//                rowSendButton.setVisibility(View.VISIBLE);
//                rowMenuButton.setVisibility(View.GONE);

            }
        });
    }
}

