package com.example.farmerhelper.ui.main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmerhelper.DetailActivity;
import com.example.farmerhelper.R;
import com.example.farmerhelper.db.options.PlantOptions;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    Context context;
    Cursor cursor;
    int plantType;

    public PlantAdapter(Context context,Cursor cursor,int plantType) {
        this.context = context;
        this.cursor = cursor;
        this.plantType=plantType;
    }
    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.one_row_item, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }
        String name = cursor.getString(cursor.getColumnIndex(PlantOptions.COLUMN_TITLE));
        long id=cursor.getLong(cursor.getColumnIndex(PlantOptions._ID));
        holder.txtTitle.setText(name);
        holder.itemView.setTag(id);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                int id= Integer.parseInt(String.valueOf(v.getTag()));
                intent.putExtra("id",id);
                intent.putExtra("plantType",plantType);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        private CardView cardView;
        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView=(CardView)itemView;
            txtTitle=itemView.findViewById(R.id.txtTitle);
        }
    }

    public void swapCursor(Cursor newCursor){
        if(cursor!=null)
            cursor.close();
        cursor=newCursor;
        if(newCursor !=null)
            notifyDataSetChanged();
    }


}
