package com.example.pocketpenguin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.NameList;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private ListLista listTobePresented;
    private int posicion;
    private CarritoActividad actividad;

    public ListAdapter(ListLista listas, CarritoActividad actividad) {
        this.listTobePresented = listas;
        this.actividad = actividad;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cellView = inflater.inflate(R.layout.recycler_view_cell, parent, false);
        ListViewHolder cellViewHolder = new ListViewHolder(cellView, actividad);

        //if (isModoOscuro()) {
          //  cellView.setBackgroundResource(R.style.CeldaClara);
      //  }

        return cellViewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Lista dataForThisCell = this.listTobePresented.getListas().get(position);
        holder.binData(dataForThisCell, actividad);

    }



    @Override
    public int getItemCount() {
        return this.listTobePresented.getListas().size();
    }



}
