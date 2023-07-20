package com.example.ourchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    Button logOut,sendMessage;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static Connector connector;
    List<ModelOfMessage> messageList;
    String name,link;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.label_homepage);

        logOut=findViewById(R.id.button);
        sendMessage=findViewById(R.id.button2);
        editText=findViewById(R.id.editText);
        progressBar=findViewById(R.id.progressBar3);



        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        String mail=mAuth.getCurrentUser().getEmail();
        String trimEmail;
        trimEmail= mail.replace("@","");
        trimEmail= trimEmail.replace(".","");


        String finalTrimEmail = trimEmail;
        messageList=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        connector=new Connector(messageList,this);
        recyclerView.setAdapter(connector);


        databaseReference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                progressBar.setVisibility(View.VISIBLE);
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    ModelOfMessage message=snapshot1.getValue(ModelOfMessage.class);
                    messageList.add(message);
                }
               recyclerView.scrollToPosition(messageList.size()-1);
                connector.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=editText.getText().toString();
                if(text.isEmpty()){
                    editText.setError("Please write anything");
                    editText.requestFocus();
                    return;
                }

                databaseReference.child("users").child(finalTrimEmail).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ModelOfUsers model=snapshot.getValue(ModelOfUsers.class);
                        name=model.userName;
                        link=model.pictureLink;
                        String key=databaseReference.push().getKey();
                        databaseReference.child("chats").child(key).setValue(new ModelOfMessage(name, link, text, key,mail));
                        editText.setText("");
                        connector.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LogInActivity.class));
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(MainActivity.this);

        dialogExit.setTitle("EXIT!!");
        dialogExit.setMessage("Are you sure?");

        dialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });

        dialogExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogExit.show();
    }
}
