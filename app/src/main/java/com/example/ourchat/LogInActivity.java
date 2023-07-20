package com.example.ourchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    TextView signUp;
    EditText etName;
    EditText etPass;
    Button logIn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.label_log_in);

        signUp=findViewById(R.id.tvsignup);
        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etname);
        etPass = findViewById(R.id.etpass);
        logIn=findViewById(R.id.bsignin);
        logIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvsignup){
            startActivity(new Intent(this,SignUpActivity.class));
        }
        if(v.getId()==R.id.bsignin){
            String mail=etName.getText().toString().trim();
            String password=etPass.getText().toString().trim();

            if(mail.isEmpty()){
                etName.setError("You can't leave blank");
                etName.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                etName.setError("Enter valid Email ID");
                etName.requestFocus();
                return;
            }
            if(password.isEmpty()){
                etPass.setError("You can't leave blank");
                etPass.requestFocus();
                return;
            }
            if(password.length()<6){
                etPass.setError("Enter at least 6 digit password");
                etPass.setText("");
                etPass.requestFocus();
                return;
            }


            mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String trimEmail;
                        trimEmail= mail.replace("@","");
                        trimEmail= trimEmail.replace(".","");
                        startActivity(new Intent(LogInActivity.this,MainActivity.class));
                        finish();

                    Toast.makeText(LogInActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(LogInActivity.this);

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