package com.example.lalecon.iwashlaundry;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lalecon.iwashlaundry.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        AppCompatButton btn_update = (AppCompatButton) findViewById(R.id.btn_update);

        LayoutInflater inflater = LayoutInflater.from(this);
        //View layout_pwd = inflater.inflate(R.layout.change_password_layout,null);
        final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);
        final EditText edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        final EditText edtRepeatPasword = (EditText) findViewById(R.id.edtRepeatPassword);
ImageView reback = (ImageView) findViewById(R.id.reback);

        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final android.app.AlertDialog waitingDIalog = new SpotsDialog(Settings.this);
                waitingDIalog.show();

                if(edtPassword.getText().toString().equals(Common.currentUser.getPassword())){
                    if(edtNewPassword.getText().toString().equals(edtRepeatPasword.getText().toString())){
                        Map<String,Object> passwordUpdate = new HashMap();
                        passwordUpdate.put("password",edtNewPassword.getText().toString());

                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(Common.currentUser.getPhone())
                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        waitingDIalog.dismiss();
                                        Toast.makeText(Settings.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Settings.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    else {

                        Toast.makeText(Settings.this, "New Password doesn't match", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    waitingDIalog.dismiss();
                    Toast.makeText(Settings.this, "Wrong Old Password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
