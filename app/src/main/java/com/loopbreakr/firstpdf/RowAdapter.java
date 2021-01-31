package com.loopbreakr.firstpdf;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.RowViewHolder> {

    private ArrayList<RowItem> rowList;
    private OnItemClickListener mListener;

    public RowAdapter(ArrayList<RowItem> rowList){
        this.rowList = rowList;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onMenuClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder{

        public ImageView rowImageView;
        public TextView rowTextView;
        public Button rowDeleteButton;
        public Button rowSendButton;
        public ImageView rowMenuIcon;


        public RowViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //find views in row XML
            rowImageView = itemView.findViewById(R.id.fileImage);
            rowTextView = itemView.findViewById(R.id.fileName);
            rowDeleteButton = itemView.findViewById(R.id.deleteFile);
            rowSendButton = itemView.findViewById(R.id.editButton);
            rowMenuIcon = itemView.findViewById(R.id.menuIcon);

            //handle adapters onclick behavior of the recyclerview. Using to handle visual changes in the XML as well
            itemView.setOnClickListener(v -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);

                        rowDeleteButton.setVisibility(View.INVISIBLE); rowSendButton.setVisibility(View.INVISIBLE); rowMenuIcon.setVisibility(View.VISIBLE);
                        rowTextView.setTextColor(Color.parseColor("#000000"));
                    }
                }
            });

            rowMenuIcon.setOnClickListener(v -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onMenuClick(position);
                        rowDeleteButton.setVisibility(View.VISIBLE); rowSendButton.setVisibility(View.VISIBLE); rowMenuIcon.setVisibility(View.INVISIBLE);
                        rowTextView.setTextColor(Color.parseColor("#808e95"));
                    }
                }

            });
            rowDeleteButton.setOnClickListener(v -> {
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
        return new RowViewHolder(v, mListener);
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

}
