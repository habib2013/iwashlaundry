package com.example.lalecon.iwashlaundry;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.lalecon.iwashlaundry.Common.Common;
import com.example.lalecon.iwashlaundry.Database.Database;
import com.example.lalecon.iwashlaundry.model.Order;
import com.example.lalecon.iwashlaundry.model.Subcategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SubcategoryDetail extends AppCompatActivity {

    TextView subcategory_name,subcategory_price,subcategory_description;
    ImageView subcategory_image;
     CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String subcategoryId = "";
    Subcategory currentsubcategory;
    FirebaseDatabase database;
    DatabaseReference subcategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_detail);

        database = FirebaseDatabase.getInstance();
        subcategory  = database.getReference("Subcategory");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        TextView  btnCart = (TextView) findViewById(R.id.btn_cart);
        TextView  checkout = (TextView) findViewById(R.id.checkout);
        TextView  conti = (TextView) findViewById(R.id.contik);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(

                        subcategoryId,currentsubcategory.getName(), numberButton.getNumber(),
                        currentsubcategory.getPrice(),currentsubcategory.getDiscount()


                ));
                Toast.makeText(SubcategoryDetail.this, "Added to washer", Toast.LENGTH_SHORT).show();

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent it = new Intent(SubcategoryDetail.this,Cart.class);
                startActivity(it);


            }
        });
        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

        // subcategory_description = (TextView) findViewById(R.id.subcategory_description);
        subcategory_name = (TextView) findViewById(R.id.subcategory_name);
        subcategory_price = (TextView) findViewById(R.id.subcategory_price);
        subcategory_image = (ImageView) findViewById(R.id.img_subcategory);

          collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
          collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
         collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppbar);


        Typeface custom_font = Typeface.createFromAsset(getAssets(),"font/restaurant_font.otf");
        subcategory_name.setTypeface(custom_font);
        subcategory_price.setTypeface(custom_font);
        btnCart.setTypeface(custom_font);
        checkout.setTypeface(custom_font);
        conti.setTypeface(custom_font);
     //
        // get Subcategory from intent
        if(getIntent() != null)
            subcategoryId = getIntent().getStringExtra("SubcategoryId");

        if(!subcategoryId.isEmpty()){
            if(Common.isConnectedToInternet(getBaseContext()))
                getDetailSubcategory(subcategoryId);
            else {
                Toast.makeText(SubcategoryDetail.this, "Check your connection", Toast.LENGTH_SHORT).show();

            }
        }





    }

    private void getDetailSubcategory(String subcategoryId) {
        subcategory.child(subcategoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentsubcategory = dataSnapshot.getValue(Subcategory.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentsubcategory.getImage()).into(subcategory_image);

               collapsingToolbarLayout.setTitle(currentsubcategory.getName());
                subcategory_price.setText(currentsubcategory.getPrice());
               //    subcategory_description.setText(currentsubcategory.getDescription());
                subcategory_name.setText(currentsubcategory.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
