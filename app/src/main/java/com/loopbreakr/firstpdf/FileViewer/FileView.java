package com.loopbreakr.firstpdf.FileViewer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.loopbreakr.firstpdf.PdfViewer;
import com.loopbreakr.firstpdf.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FileView extends AppCompatActivity {

    private RecyclerView fileRecyclerView;
    private RowAdapter fileAdapter;
    private RecyclerView.LayoutManager fileLayoutManager;
    private ArrayList<RowItem> rowItem;
    private File[] files;
    private File tempFile;
    private File selectedFileData;
    private MenuItem searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        //create the toolbar view from the toolbar in the xml
        showSearchBar();
        createRows();
        buildRecyclerView();
    }

    private void showSearchBar() {
        Toolbar myToolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void createRows(){
        //Create a array of files in the app folder named PDF_files sorted in descending order
        File file = new File(getExternalFilesDir("PDF_files").toString());
        files = file.listFiles();
        Arrays.sort(files, Collections.reverseOrder());
        //Populate RowItem Arraylist with the file names and paths
        rowItem = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            rowItem.add(new RowItem(tempFile.getName().replace("__", "\n").replace('_',' ').replace('-','/').replace(".pdf",""), tempFile));
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

                String selectedFilePath = rowItem.get(position).getFileData().getPath();
                openPDF(selectedFilePath);
            }

            @Override
            public void onDeleteClick(int position) {
                //delete actual file from device
                selectedFileData = rowItem.get(position).getFileData();
                selectedFileData.delete();
                //delete from recyclerview
                removeItem(position);
            }
        });
    }

    //Method to remove row from recyclerview
    public void removeItem(int position) {
        //This code works fine without searchview:
//        rowItem.remove(position);
//        fileAdapter.notifyDataSetChanged();
        //MESSY - will optimize. Right now we are recreating the rowList from the files on the device, and passing it to the adapter
        createRows();
        fileAdapter = new RowAdapter(rowItem);
        fileRecyclerView.setAdapter(fileAdapter);

        searchBar.collapseActionView();
    }

    //Method to send file data to pdf viewer class
    public void openPDF(String path){
        Intent intent = new Intent(this, PdfViewer.class);
        intent.putExtra("fileData", path);
        startActivity(intent);
    }

    ////////////FILTERING METHODS
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get the searchbar
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchBar = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchBar.getActionView();

        //handle it's behavior
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //for submitted data (not used in this application)
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //for live data updates when text is typed into searchbar
            @Override
            public boolean onQueryTextChange(String newText) {
                fileAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}

