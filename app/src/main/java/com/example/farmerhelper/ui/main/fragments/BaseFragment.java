package com.example.farmerhelper.ui.main.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmerhelper.DetailActivity;
import com.example.farmerhelper.LoginActivity;
import com.example.farmerhelper.R;
import com.example.farmerhelper.db.PlantDbManager;
import com.example.farmerhelper.models.Settings;
import com.example.farmerhelper.results.Result;
import com.example.farmerhelper.ui.main.PlantAdapter;
import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment {

    int plantType;
    PlantAdapter plantAdapter;
    PlantDbManager dbManager;

    public BaseFragment(int plantType) {
        this.plantType = plantType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_base, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        dbManager = new PlantDbManager(v.getContext());
        Cursor entities = dbManager.getAll(plantType);
        plantAdapter = new PlantAdapter(v.getContext(), entities, plantType);
        recyclerView.setAdapter(plantAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle(R.string.item_is_deleting_are_you_sure);
                alert.setPositiveButton(R.string.yes, (dialog, which) -> {
                    int id = Integer.parseInt(String.valueOf(viewHolder.itemView.getTag()));
                    Result result = dbManager.delete(id);
                    if (!result.Success)
                        Toast.makeText(getContext(), result.Message, Toast.LENGTH_SHORT).show();
                    plantAdapter.swapCursor(dbManager.getAll(plantType));
                    Snackbar.make(viewHolder.itemView, result.Message, Snackbar.LENGTH_LONG).setAction(
                            result.Message, null).show();
                });
                alert.setNegativeButton(R.string.no, (dialog, which) ->
                        {
                            Toast.makeText(getContext(), getString(R.string.cancelled), Toast.LENGTH_SHORT).show();
                            plantAdapter.swapCursor(dbManager.getAll(plantType));
                        }
                );
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);

        return v;
    }


}