package com.example.altafshah.goldbin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.common.ConnectionResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RentToilet extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private static final String TAG = "RentToilet";
    private static final String URL_FOR_TREELIST = "http://10.1.19.32/goldbin/trees";
    ProgressDialog progressDialog;
    Button GetLocation, SaveTreeLocation;
    EditText latitude, longitude;
    EditText Tree_Name, Tree_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_toilet);
        requestPermission();

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        client = LocationServices.getFusedLocationProviderClient(this);
        GetLocation = (Button) findViewById(R.id.getLocation);
        SaveTreeLocation = (Button)findViewById(R.id.SaveTreeLocation);

        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        Tree_Name = (EditText) findViewById(R.id.Tree_Name);
        Tree_type = (EditText) findViewById(R.id.Tree_type);

        GetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(RentToilet.this,ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    return;
                }

                client.getLastLocation().addOnSuccessListener(RentToilet.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location!=null){
                            float lat = (float) (location.getLatitude());
                            float lng = (float) (location.getLongitude());
                            latitude.setText(String.valueOf(lat));
                            longitude.setText(String.valueOf(lng));
                        }
                    }
                });
            }
        });

        SaveTreeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void addTree(final String latitude, final String longitude, final String Tree_Name, final String Tree_type) {
        // Tag used to cancel the request
        String cancel_req_tag = "RentToilet";

        progressDialog.setMessage("Adding tree ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_TREELIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String Tree_Name = jObj.getJSONObject("Tree_Name").getString("Tree_Name");
                        String Tree_type = jObj.getJSONObject("Tree_type").getString("Tree_type");
                        String latitude = jObj.getJSONObject("latitude").getString("latitude");
                        String longitude= jObj.getJSONObject("longitude").getString("longitude");

                        Toast.makeText(getApplicationContext(), "Hurray! You have successfully added a tree!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RentToilet.this,
                                UserActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("Tree_Name", Tree_Name);
                params.put("Tree_type", Tree_type);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}