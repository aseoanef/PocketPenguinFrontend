package com.example.pocketpenguin;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;
    private TextView userView;
    private Message message;
    public void bindMessage(Message message) {
        this.textView.setText(message.getContent());
        this.message=message;
    }
    public void bindUser(Message message){
        this.userView.setText(message.getUser());
        this.message=message;
    }
    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.messagecontent);
        userView=itemView.findViewById(R.id.user);
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
