package com.loopbreakr.firstpdf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.RowViewHolder> implements Filterable{

    private ArrayList<RowItem> rowList;
    public ArrayList<RowItem> rowListAll;
    private OnItemClickListener rowListener;

    public RowAdapter(ArrayList<RowItem> rowList){
        this.rowList = rowList;
        this.rowListAll = new ArrayList<>(rowList);
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        rowListener = listener;
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder{

        public ImageView rowImageView;
        public TextView rowTextView;
        public ImageView rowDeleteIcon;

        public RowViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //find views in row XML
            rowImageView = itemView.findViewById(R.id.fileImage);
            rowTextView = itemView.findViewById(R.id.fileName);
            rowDeleteIcon = itemView.findViewById(R.id.deleteIcon);

            //handle adapters onclick behavior of the recyclerview. Using to handle visual changes in the XML as well
            itemView.setOnClickListener(v -> {
                if(listener != null){

                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });

            rowDeleteIcon.setOnClickListener(v -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new RowViewHolder(v, rowListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        String currentItem = rowList.get(position).getFileName();
        holder.rowTextView.setText(currentItem);
    }

    //return the # of items in the recyclerview
    @Override
    public int getItemCount() {
        return rowList.size();
    }

    /////////////SEARCH FILTER METHODS////////////////////
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<RowItem> filtredList = new ArrayList<>();

            //add all to the filtred list if searchtext is empty
            if(constraint.toString().isEmpty()){
                filtredList.addAll(rowListAll);
            }
            //else check if filename in the row item matches the searchtext, if it does add the row item to the filtred list
            else{
                for (RowItem row: rowListAll){
                    if(row.getFileName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filtredList.add(row);
                    }
                }
            }
            //create a filterResults variable to hold the filtred results to the publishResults method
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtredList;
            return filterResults;
        }

        //runs on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            rowList.clear();
            rowList.addAll((ArrayList<RowItem>) results.values);
            notifyDataSetChanged();
        }
    };
}