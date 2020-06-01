package com.example.lalecon.iwashlaundry;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.lalecon.iwashlaundry.Common.Common;
import com.example.lalecon.iwashlaundry.Database.Database;
import com.example.lalecon.iwashlaundry.Interface.ItemClickListener;
import com.example.lalecon.iwashlaundry.Service.ListenOrder;
import com.example.lalecon.iwashlaundry.ViewHolder.MenuViewHolder;
import com.example.lalecon.iwashlaundry.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AppCompatButton comp;
    FirebaseDatabase database;
    DatabaseReference category;
    CounterFab fab;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("i Wash");
        setSupportActionBar(toolbar);
        TextView txtfullname;
        CardView cdView;



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMenu();

            }
        });


        RelativeLayout RelView;
        TextView tx1,tx2,tx3,tx4,tx5,tx6,tx7,tx8;
        RelView = (RelativeLayout) findViewById(R.id.RelView);


        database = FirebaseDatabase.getInstance();
        category= database.getReference("Category");
        Paper.init(this);

         fab = (CounterFab) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent  = new Intent(MainDrawer.this,Cart.class);
                startActivity(cartIntent);
            }
        });

                        fab.setCount(new Database(this).getCountCart());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View holderView = navigationView.getHeaderView(0);
        txtfullname = (TextView) holderView.findViewById(R.id.txtfullname);
        txtfullname.setText(Common.currentUser.getName());

        // Load menu ggf hhh bbhhh gg h h ff jtthr xfhbfghhhfdbnbeend m
        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
       // recycler_menu.setLayoutManager(layoutManager);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
        if(Common.isConnectedToInternet(this)) {

            loadMenu();
            Intent service = new Intent(MainDrawer.this, ListenOrder.class);
            startService(service);
        }
        else
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();



    }
    @Override
    protected void onResume(){
       super.onResume();
        fab.setCount(new Database(this).getCountCart());


    }




    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with((getBaseContext())).load(model.getImage()).into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Get Category and send to new Activity
                        Intent subcat = new Intent(MainDrawer.this,SubcategoryList.class);

                        //Because CategoryId is key,so we just get key of this item
                        subcat.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(subcat);

                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == R.id.ic_refresh)

            loadMenu();
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.ic_home) {

        }  else if (id == R.id.ic_share) {

        }
       else if (id == R.id.nav_change_pwd) {

           Intent it = new Intent(MainDrawer.this,Settings.class);
           startActivity(it);
       }
        else if (id == R.id.ic_exit) {
          //Delete
            Paper.book().destroy();

            //logout
            Intent signs = new Intent(MainDrawer.this,SignIn.class);
            signs.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(signs);


        }
        else if (id == R.id.ic_abt) {

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
