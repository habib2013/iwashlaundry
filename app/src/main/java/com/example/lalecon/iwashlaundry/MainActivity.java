package com.example.lalecon.iwashlaundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalecon.iwashlaundry.Common.Common;
import com.example.lalecon.iwashlaundry.R;
import com.example.lalecon.iwashlaundry.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    AppCompatButton started,login;
    TextView wash,mission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        started = (AppCompatButton) findViewById(R.id.btn_started);
        login = (AppCompatButton) findViewById(R.id.btn_login);
        wash = (TextView) findViewById(R.id.wash);
        mission = (TextView) findViewById(R.id.mission);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"font/restaurant_font.otf");
        wash.setTypeface(custom_font);
        mission.setTypeface(custom_font);
        started.setTypeface(custom_font);
        login.setTypeface(custom_font);

        Paper.init(this);


        started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this,SignIn.class);
                startActivity(it);

            }
        });

       started.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent it = new Intent(MainActivity.this,SignUp.class);
               startActivity(it);

           }
       });

        //check box
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if(user != null && pwd != null){
            if(!user.isEmpty() && !pwd.isEmpty()){
                login(user,pwd);

            }


        }

    }

    private void login(final String phone, final String pwd) {
        if (Common.isConnectedToInternet(getBaseContext())) {
            // save user  & password
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = database.getReference("User");

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please Wait...");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Check if u                         ser nit exist in Database
                    if (dataSnapshot.child(phone).exists()) {
                        //Get user info
                        mDialog.dismiss();


                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd)) {
                            Intent it = new Intent(MainActivity.this, MainDrawer.class);
                            Common.currentUser = user;
                            startActivity(it);

                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Wrong username or password!!", Toast.LENGTH_LONG).show();


                        }
                    } else {

                        Toast.makeText(MainActivity.this, "User not exist in database!!", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(MainActivity.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }


}
