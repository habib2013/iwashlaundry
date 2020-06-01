package com.example.lalecon.iwashlaundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.rey.material.widget.CheckBox;

import com.example.lalecon.iwashlaundry.Common.Common;
import com.example.lalecon.iwashlaundry.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

import static android.R.attr.phoneNumber;

public class SignIn extends AppCompatActivity {
AppCompatButton btn_login;
    EditText input_phone,input_password;
    ImageView back;
    FirebaseAuth mAuth;
    CheckBox chkRemember;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);


        input_phone = (EditText) findViewById(R.id.input_phone);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        chkRemember = (CheckBox) findViewById(R.id.chkRemember);


        Paper.init(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"font/restaurant_font.otf");
        input_phone.setTypeface(custom_font);
        btn_login.setTypeface(custom_font);


        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backie = new Intent(SignIn.this,MainActivity.class);
                startActivity(backie);


            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");


        back = (ImageView) findViewById(R.id.back);
       btn_login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (Common.isConnectedToInternet(getBaseContext())) {
                // save user  & password
                    if(chkRemember.isChecked()){
                       Paper.book().write(Common.USER_KEY,input_phone.getText().toString());
                        Paper.book().write(Common.PWD_KEY,input_phone.getText().toString());


                    }

                  // final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                  // mDialog.setMessage("Please Wait...");
                  // mDialog.show();


                   final android.app.AlertDialog mDialog = new SpotsDialog(SignIn.this);
                   mDialog.show();



                   table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           //Check if user exist in Database
                           if (dataSnapshot.child(input_phone.getText().toString()).exists()) {
                               //Get user info
                               mDialog.dismiss();


                               User user = dataSnapshot.child(input_phone.getText().toString()).getValue(User.class);
                               user.setPhone(input_phone.getText().toString());
                               if (user.getPassword().equals(input_password.getText().toString())) {
                                   Intent it = new Intent(SignIn.this, MainDrawer.class);
                                   Common.currentUser = user;
                                   startActivity(it);

                                   finish();
                                   table_user.removeEventListener(this);

                               } else {
                                   Toast.makeText(SignIn.this, "Wrong username or password!!", Toast.LENGTH_LONG).show();


                               }
                           } else {
                               mDialog.dismiss();
                               Toast.makeText(SignIn.this, "User not exist in database!!", Toast.LENGTH_LONG).show();


                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
               }
               else {
                   Toast.makeText(SignIn.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                   return;
               }
           }

       });
    }


    }

