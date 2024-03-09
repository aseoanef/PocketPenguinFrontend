package com.example.pocketpenguin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private MessageList messagetoshow;
    public MessageAdapter(MessageList messages){
        this.messagetoshow=messages;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 1. Necesitamos un LayoutInflater. Lo creamos a partir de un Context
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 2. Con el LayoutInflater, 'inflamos' el XML y generamos una View
        View cellView = inflater.inflate(R.layout.message_cells, parent, false);
        // 3. Esta View es la que pasamos al constructor de ClipViewHolder.
        //    ¡Y ya está listo!
        MessageViewHolder messageViewHolder = new MessageViewHolder(cellView);
        return messageViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Usamos .get(position) para acceder al 'enésimo' elemento de la lista
        // O sea, el correspondiente a la posición 'position'
        Message dataForThisCell = this.messagetoshow.getMessages().get(position);
        holder.bindMessage(dataForThisCell);
        holder.bindUser(dataForThisCell);
    }
    @Override
    public int getItemCount() {
        return this.messagetoshow.getMessages().size();
    }
}


