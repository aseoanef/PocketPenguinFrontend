package com.example.pocketpenguin;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BillsViewHolder extends RecyclerView.ViewHolder {
    private TextView nameView;
    private TextView amountView;
    private Bill bill;
    public void bindBill(Bill bill) {
        this.nameView.setText(bill.getTitle());
        this.amountView.setText(bill.getMoneyAmount()+"€");
    }
    public BillsViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView=itemView.findViewById(R.id.textviewer);
        amountView=itemView.findViewById(R.id.gasto);
       // itemView.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
             //   int clipId = bill.getId();
               // Context context = view.getContext();
                //Toast.makeText(context,"Touched cell with clipId: " + clipId,Toast.LENGTH_SHORT).show();
                // Mostramos un Toast para completar la tarea
                //Intent intent = new Intent(context, shoppinglist.class);
                //intent.putExtra(shoppinglist.INTENT_CLIP_ID, clipId);
                //intent.putExtra(shoppinglist.INTENT_CLIP_URL, bill.getId());
                //context.startActivity(intent);}
        //});
    }
}
