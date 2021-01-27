package com.loopbreakr.firstpdf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.RowViewHolder> {

    private ArrayList<RowItem> mRowList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder{
        public ImageView rowImageView;
        public TextView rowTextView;
        public ImageView rowDeleteImage;

        public RowViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            rowImageView = itemView.findViewById(R.id.fileImage);
            rowTextView = itemView.findViewById(R.id.fileName);
            rowDeleteImage = itemView.findViewById(R.id.deleteFile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            rowDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }

                }
            });

        }
    }

    public RowAdapter(ArrayList<RowItem> rowList){
        mRowList = rowList;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        RowViewHolder rvh = new RowViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        RowItem currentItem = mRowList.get(position);

        holder.rowImageView.setImageResource(currentItem.getImageResource());
        holder.rowTextView.setText(currentItem.getFileName());
    }

    @Override
    public int getItemCount() {
        return mRowList.size();
    }


}
