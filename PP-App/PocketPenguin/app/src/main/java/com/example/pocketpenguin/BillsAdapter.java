package com.example.pocketpenguin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillsAdapter extends RecyclerView.Adapter<BillsViewHolder> {
    private BillsList billstoshow;
    public BillsAdapter(BillsList bills){
        this.billstoshow=bills;
    }
    @NonNull
    @Override
    public BillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. Necesitamos un LayoutInflater. Lo creamos a partir de un Context
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 2. Con el LayoutInflater, 'inflamos' el XML y generamos una View
        View cellView = inflater.inflate(R.layout.recycler_bills_cell, parent, false);
        // 3. Esta View es la que pasamos al constructor de ClipViewHolder.
        //    ¡Y ya está listo!
        BillsViewHolder billsViewHolder = new BillsViewHolder(cellView);
        return billsViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull BillsViewHolder holder, int position) {
        // Usamos .get(position) para acceder al 'enésimo' elemento de la lista
        // O sea, el correspondiente a la posición 'position'
        Bill dataForThisCell = this.billstoshow.getBills().get(position);
        holder.bindBill(dataForThisCell);
    }
    @Override
    public int getItemCount() {
        return this.billstoshow.getBills().size();
    }
}
