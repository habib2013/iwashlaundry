package com.example.lalecon.iwashlaundry.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.lalecon.iwashlaundry.model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lalecon on 6/1/2018.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "ilaundry.sqlite";
    private static final int DB_VER = 1;

    public Database(Context context)
    {
        super(context, DB_NAME, null,DB_VER);
    }
    public List<Order> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        db.isOpen();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ProductName","ProductId","Quantity","Price","Discount"};

        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result  = new ArrayList<>();

        if(c.moveToFirst())
        {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                ));

            } while (c.moveToNext());
            c.close();

        }




        db.close();

        return result;

    }

    public void addToCart (Order order){

        SQLiteDatabase db = getReadableDatabase();
        db.isOpen();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s'); ",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount()
        );

        db.execSQL(query);
        db.close();

    }

    public void cleanCart (){

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail" );

        db.execSQL(query);
        db.close();
    }

    public int getCountCart() {
        int count = 0;
SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
          do{
              count = cursor.getInt(0);

          }while (cursor.moveToNext());

        }
      return count;
    }
}
