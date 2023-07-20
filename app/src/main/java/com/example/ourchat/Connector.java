package com.example.ourchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Connector extends RecyclerView.Adapter<Connector.ViewHolder> {

    List<ModelOfMessage> messages;
    Context context;

    public Connector(List<ModelOfMessage> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public Connector.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.model_of_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Connector.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    holder.user.setText(messages.get(position).getNameOfUser());
    holder.text.setText(messages.get(position).getMessageOfUser());
     Glide.with(context).load(messages.get(position).getPicLinkOfUser()).into(holder.imageView);
     holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(View v) {
             String mail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
             if(messages.get(position).getMail().equals(mail)){
                 CharSequence[] items = {"Delete"};
                 AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                 dialog.setItems(items, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         if(i == 0){
                             String key=messages.get(position).getIdOfKey();
                             DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                             databaseReference.child("chats").child(key).removeValue();
                         }
                     }
                 });
                 dialog.show();
             }
             return false;
         }
     });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView user,text;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.modelImageView);
            user=itemView.findViewById(R.id.modelUserName);
            text=itemView.findViewById(R.id.modelMessage);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
