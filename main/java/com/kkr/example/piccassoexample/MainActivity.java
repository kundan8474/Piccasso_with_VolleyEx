package com.kkr.example.piccassoexample;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.android.volley.Response.Listener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    
    private ImageView imageView;
    private Context context;
    private RequestQueue requestQueue;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView);
        context=this;
        pd=new ProgressDialog(context);
        pd.setMessage("loading image using Piccasso");
        pd.setCancelable(true);
        pd.setProgressStyle(pd.STYLE_SPINNER);
        requestQueue= Volley.newRequestQueue(context);
        pd.setProgress(0);
        pd.setIndeterminate(true);
        pd.show();
        getImageUrl(imageView);
    }

    private void getImageUrl(final ImageView imageView)
    {
        String apiURl="http://api-oauth.opencart-api.com/api/rest/bestsellers/limit/10";

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(apiURl, null,
                new Response.Listener<JSONObject>() {
                    String imageURL;
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray data=response.getJSONArray("data");
                            for(int len=0;len<data.length();len++)
                            {
                                JSONObject bestSellers=data.getJSONObject(len);
                                imageURL=bestSellers.getString("thumb");
                            }
                            Picasso.with(context).load(imageURL).into(imageView);
                            if(pd.isShowing()){pd.cancel();}

                        }catch (JSONException e){e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ",error.getMessage());
                if(pd.isShowing()){pd.cancel();}
            }
        }){@Override
        public Map< String, String > getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-type", "application/json");
            headers.put("Authorization", "Bearer 1d1b4e1b245c284e8eda12877f64772c5f9d19f1");
            return headers;
        }};

        // Adding JsonObject request to request queue
    requestQueue.add(jsonObjectReq);



}}
