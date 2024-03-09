package com.example.pocketpenguin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraficActivity extends AppCompatActivity {
    //definir variables

    private Context context=this;
    private RequestQueue queue;
    private float itemPrice;
    private ProgressBar progressBar;
    private float listPrice;
    //clases scuffed
    private JSONArray billslist;
    private JSONArray itemlist;
    JSONArray listList=new JSONArray();
    private JSONArray productList;
    private GraphView grafic;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    //setters y getters
    public GraphView getGraphView(){
        return grafic;
    }
    public void setGrafic(GraphView grafic){
        this.grafic=grafic;
    }
    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public JSONArray getBillslist() {
        return billslist;
    }

    public void setBillslist(JSONArray billslist) {
        this.billslist = billslist;
    }

    public JSONArray getListList(){
        return listList;
    }
    public void setitemlist(JSONArray itemlist){
        this.itemlist=itemlist;
    }
    public JSONArray getItemlist(){
        return itemlist;
    }
    public JSONArray getProductList(){
        return productList;
    }
    public void setProductList(JSONArray productList){
        this.productList=productList;
    }
    public float getListPrice() {
        return listPrice;
    }
    public void setListLists(JSONArray listList){
        this.listList=listList;
    }


    public float getItemPrice() {
        return itemPrice;
    }
    public void setListPrice(float listPrice) {
        this.listPrice = listPrice;
    }

    // Constructor
    public GraficActivity() throws JSONException {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafic);
        setGrafic(findViewById(R.id.graph));
        requestBillsList();
        progressBar=findViewById(R.id.progressBar);


    }
    //añadir datos a la grafica
    public void setGraficData() throws JSONException {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < getBillslist().length(); i++) {
            JSONObject lista = getBillslist().getJSONObject(i);
            DataPoint data=new DataPoint( i , BigDecimal.valueOf(lista.getDouble("moneyamount")).floatValue());
            series.appendData(data,true,6,true);
        }
        grafic.removeAllSeries();
        grafic.addSeries(series);
        progressBar.setVisibility(View.GONE);
    }
    // Método para calcular el precio de la lista de compras
    public void setPrice() throws JSONException {
        JSONArray billslist=new JSONArray();
        setBillslist(billslist);
        for (int i = 0; i < getListList().length(); i++){//lista
            setListPrice(0);
            JSONObject list= getListList().getJSONObject(i);
            for(int a = 0; a < getItemlist().length(); a++){//producto
                JSONObject item= getItemlist().getJSONObject(a);
                if (item.getString("list").equals(list.getString("id")) && item.getBoolean("bought")){
                    for(int x = 0; x < getProductList().length(); x++){//info de producto
                        JSONObject product=getProductList().getJSONObject(x);
                        System.out.println(getProductList());
                        if (item.getString("product_name").equals(product.getString("name"))){
                            setItemPrice(BigDecimal.valueOf(product.getDouble("price")).floatValue());
                        }
                        setItemPrice(item.getInt("quantity")*getItemPrice());
                    }
                    setListPrice(getItemPrice()+getListPrice());
                }
            }
            list.put("moneyamount",getListPrice());
            billslist=getBillslist();
            billslist.put(list);
            setBillslist(billslist);
        }
        setGraficData();
    }
    // Método para obtener información de un artículo
    public void getItemInfo(String name,Callback callback){
        queue=Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Server.name + "/product/",
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        // que indique "Hit OK"
                        // Parseamos la respuesta y la asignamos a nuestro atributo
                        JSONArray productList=new JSONArray();
                        try {
                            for(int i = 0; i < response.length(); i++){
                                if (getProductList()!=null){
                                    productList=getProductList();
                                }
                                JSONObject objectResponse=response.getJSONObject(i);
                                productList.put(objectResponse);
                                setProductList(productList);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            setPrice();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    // Error: No se ha establecido la conexión
                    Toast.makeText(context, "Connection could not be established", Toast.LENGTH_SHORT).show();
                } else {
                    int serverCode = error.networkResponse.statusCode;
                    // Error: El servidor ha dado una respuesta de error
                    Toast.makeText(context, "Server response " + serverCode, Toast.LENGTH_SHORT).show();

                    // La siguiente variable contendrá el código HTTP,
                    // por ejemplo 405, 500,...
                }
            }
        }){
            // Override the getHeaders method to add custom headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("product.name",name);
                return headers;
            }
        };
        // Aquí lanzaremos una petición HTTP
        this.queue.add(request);
    }
    // Método para obtener la información de las facturas
    public void requestBillsInfo(int listpk){
        queue=Volley.newRequestQueue(context);
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);
        JsonArrayRequestAuthenticated request = new JsonArrayRequestAuthenticated(
                Request.Method.GET,
                Server.name + "/lists/"+listpk+"/items/",
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        // que indique "Hit OK"
                        // Parseamos la respuesta y la asignamos a nuestro atributo
                        final int[] completedRequests={0};
                        final float[] listPrice={0};
                        JSONArray itemlista= new JSONArray();
                        if(getItemlist()!=null){
                            itemlista=getItemlist();
                        }
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject item= response.getJSONObject(i);
                                String itemName = item.getString("product_name");
                                if(item.getBoolean("bought")) {
                                    itemlista.put(item);
                                    getItemInfo(itemName,new Callback(){
                                        @Override
                                        public void onSuccess(float itemPrice) throws JSONException {
                                            int quantity = item.getInt("quantity");
                                            listPrice[0] += quantity * itemPrice;
                                            // Increment the counter variable
                                            completedRequests[0]++;
                                            // Check if all requests have completed
                                            if (completedRequests[0] == response.length()) {
                                                // Calculate the list price when all requests have completed
                                                setListPrice(listPrice[0]);

                                            }
                                        }
                                        @Override
                                        public void onError(VolleyError error) {
                                            // Handle error
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            setitemlist(itemlista);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    // Error: No se ha establecido la conexión
                    Toast.makeText(context, "Connection could not be established", Toast.LENGTH_SHORT).show();
                } else {
                    int serverCode = error.networkResponse.statusCode;
                    // Error: El servidor ha dado una respuesta de error
                    Toast.makeText(context, "Server response " + serverCode, Toast.LENGTH_SHORT).show();

                    // La siguiente variable contendrá el código HTTP,
                    // por ejemplo 405, 500,...
                }
            }
        },context){
            // Override the getHeaders method to add custom headers

        };
        // Aquí lanzaremos una petición HTTP
        this.queue.add(request);
    }
    // Define a callback interface for handling asynchronous responses
    interface Callback {
        void onSuccess(float itemPrice) throws JSONException;
        void onError(VolleyError error);
    }
    // Método para obtener la lista de facturas
    public void requestBillsList(){
        queue= Volley.newRequestQueue(context);
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);
        JsonArrayRequestAuthenticated request = new JsonArrayRequestAuthenticated(
                Request.Method.GET,
                Server.name + "/lists/",
                null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray listadelistas=new JSONArray();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject list= null;
                            try {
                                list = response.getJSONObject(i);
                                requestBillsInfo(list.getInt("id"));
                                list.getString("name");
                                listadelistas.put(list);
                                //añadir los gastos de cada lista

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            setListLists(listadelistas);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    // Error: No se ha establecido la conexión
                    Toast.makeText(context, "Connection could not be established", Toast.LENGTH_SHORT).show();
                } else {
                    int serverCode = error.networkResponse.statusCode;
                    // Error: El servidor ha dado una respuesta de error
                    Toast.makeText(context, "Server response " + serverCode, Toast.LENGTH_SHORT).show();

                    // La siguiente variable contendrá el código HTTP,
                    // por ejemplo 405, 500,...
                }
            }
        },context){
            // Override the getHeaders method to add custom headers

        };
        // Aquí lanzaremos una petición HTTP
        this.queue.add(request);
    }
}

