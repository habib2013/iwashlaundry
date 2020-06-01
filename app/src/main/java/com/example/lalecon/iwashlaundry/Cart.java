package com.example.lalecon.iwashlaundry;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lalecon.iwashlaundry.Common.Common;
import com.example.lalecon.iwashlaundry.Database.Database;
import com.example.lalecon.iwashlaundry.ViewHolder.CartAdapter;
import com.example.lalecon.iwashlaundry.model.Order;
import com.example.lalecon.iwashlaundry.model.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    AppCompatButton btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace = (AppCompatButton) findViewById(R.id.btnPlaceOrder);


        ImageView back = (ImageView) findViewById(R.id.cartback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backie = new Intent(Cart.this,MainDrawer.class);
                startActivity(backie);


            }
        });



        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new Request
               if(cart.size() > 0)
                    showAlertDialog();
                else
                  Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

       loadListSubcategory();



    }

  private void loadListSubcategory() {
        cart = new Database(this).getCarts();

        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
       recyclerView.setAdapter(adapter);

        //Calculate total price

       int total = 0;
        for (Order order:cart)
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","NG");
       NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
      txtTotalPrice.setText(fmt.format(total));
     //   finish();

          }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;

    }

    private void deleteCart(int position) {
        //we will  remove item at list Order
        cart.remove(position);
        //then we delete old dt
        new Database(this).cleanCart();
        for(Order item:cart)
            new Database(this).addToCart(item);
        //refresh
       // loadListSubcategory();


    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more Step");
        alertDialog.setMessage("Enter your address");

        LayoutInflater inflater = this.getLayoutInflater();

        View order_address_comment =  inflater.inflate(R.layout.order_address_comment, null);



        final  EditText edtAddress = (EditText) order_address_comment.findViewById(R.id.edtAddress);
        final   EditText edtComment = (EditText) order_address_comment.findViewById(R.id.edtComment);


        alertDialog.setView(order_address_comment);

        alertDialog.setIcon(R.drawable.mylaundry);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        edtComment.getText().toString(),
                        cart,
                        "0"

                );
                //Submit to firebase
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                //Delete Cart

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank you!, your order has being placed", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }



}
