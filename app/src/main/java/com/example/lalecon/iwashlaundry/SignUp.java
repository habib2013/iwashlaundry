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

import com.example.lalecon.iwashlaundry.Common.Common;
import com.example.lalecon.iwashlaundry.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
EditText input_phone,input_name,input_password;
    AppCompatButton btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        input_phone = (EditText) findViewById(R.id.input_phone);
        input_name = (EditText) findViewById(R.id.input_name);
        input_password = (EditText) findViewById(R.id.input_password);

        btn_signup = (AppCompatButton) findViewById(R.id.btn_login);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"font/restaurant_font.otf");
        input_phone.setTypeface(custom_font);
         input_name.setTypeface(custom_font);
        btn_signup.setTypeface(custom_font);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backie = new Intent(SignUp.this,MainActivity.class);
                startActivity(backie);


            }
        });


        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Please Wait...");
                    mDialog.show();


                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user exists
                            if (dataSnapshot.child(input_phone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "User Already exists in database", Toast.LENGTH_LONG).show();


                            } else {
                                mDialog.dismiss();
                                User user = new User(input_name.getText().toString(), input_password.getText().toString());
                                table_user.child(input_phone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "SignUp successful", Toast.LENGTH_LONG).show();
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    Toast.makeText(SignUp.this, "Please Check your connection settings", Toast.LENGTH_SHORT).show();
                    
                }
            }
        });


    }
}
