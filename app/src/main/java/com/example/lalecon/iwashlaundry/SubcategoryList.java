package com.example.lalecon.iwashlaundry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lalecon.iwashlaundry.Common.Common;
import com.example.lalecon.iwashlaundry.Interface.ItemClickListener;
import com.example.lalecon.iwashlaundry.ViewHolder.SubcategoryViewHolder;
import com.example.lalecon.iwashlaundry.model.Subcategory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SubcategoryList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference subcategoryList;



    String categoryId = "";
        FirebaseRecyclerAdapter<Subcategory,SubcategoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_list);

        ImageView back = (ImageView) findViewById(R.id.reback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backie = new Intent(SubcategoryList.this,MainDrawer.class);
                startActivity(backie);


            }
        });


        database = FirebaseDatabase.getInstance();
        subcategoryList = database.getReference("Subcategory");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_subcategory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
       // recyclerView.setLayoutManager(layoutManager);


        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
            if(!categoryId.isEmpty() && categoryId != null){
                if(Common.isConnectedToInternet(getBaseContext()))
                loadListSubcategory(categoryId);
                    else
                {

                    Toast.makeText(SubcategoryList.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                }
            }

        }



    }

    private void loadListSubcategory(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Subcategory, SubcategoryViewHolder>(Subcategory.class,R.layout.subcategory_item,SubcategoryViewHolder.class,subcategoryList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(SubcategoryViewHolder viewHolder, Subcategory model, int position) {
                viewHolder.subcategory_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.subcategory_image);

                final Subcategory local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent subcategorydetail = new Intent(SubcategoryList.this,SubcategoryDetail.class);
                        subcategorydetail.putExtra("SubcategoryId",adapter.getRef(position).getKey());
                        startActivity(subcategorydetail);
                    }
                });

            }
        };

        //setAdapter
        Log.d("TAG",""+adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }
}
