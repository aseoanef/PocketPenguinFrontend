package com.example.pocketpenguin;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Products> productsList = null;
    private ArrayList<Products> arraylist;
    private Integer list_id;
    private String nombreProducto;
    private Float precioProducto;
    private RequestQueue queue;
    private TextView textView;
    private Integer listID;

    static String host ="http://10.0.2.2:8000";

    public ListViewAdapter(Context context, List<Products> productsList, Integer list_id) {
        mContext = context;
        this.productsList = productsList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Products>();
        this.arraylist.addAll(productsList);
        this.listID=list_id;
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {

        return productsList.size();
    }

    public boolean isEmpty() {
        return productsList.isEmpty();
    }

    public boolean isEmptyOriginalList() {
        return arraylist.isEmpty();
    }

    @Override
    public Object getItem(int position) {

        return productsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.product_cell, null);
            textView = view.findViewById(R.id.product_cell_textView);
            holder.name = (TextView) view.findViewById(R.id.product_cell_textView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results
        holder.name.setText(productsList.get(position).getName());

        nombreProducto = productsList.get(position).getName();
        precioProducto = productsList.get(position).getPrice();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(nombreProducto, precioProducto, listID);
            }
        });



        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productsList.clear();

        for (Products wp : arraylist) {
            if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                productsList.add(wp);
            }
        }

        notifyDataSetChanged();
    }

    public void addProduct(String nombreProducto, Float precioProducto, Integer listID) throws NullPointerException{
        SharedPreferences preferences = mContext.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);
        this.queue = Volley.newRequestQueue(mContext);



        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("name", nombreProducto);
            requestBody.put("price", precioProducto);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                Request.Method.POST,
                host + "/lists/"+listID+"/items",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(mContext, "Se añadió el producto exitosamente", Toast.LENGTH_LONG).show();
                        textView.setText(nombreProducto);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Ha habido un error :(", Toast.LENGTH_LONG).show();

            }
        }, mContext
        );
        queue.add(request);



    }


}


