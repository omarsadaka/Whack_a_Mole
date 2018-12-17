package com.organizers_group.whack_a_mole.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.levels.EndLevel;
import com.organizers_group.whack_a_mole.levels.Level;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.jar.Attributes;

public class AuthenticationActivity extends VolumeControlActivity {
    private Button next;
    Level level;
    int score;
    String Name;
    String Age;
    String Email;
    String Password;
    private RequestQueue requestQueue;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private EditText name;
    private EditText age;
    private EditText email;
    private EditText password;
    private AlertDialog.Builder alertDialog;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        requestQueue = Volley.newRequestQueue(this);
        name = (EditText) findViewById(R.id.nameEt);
        age = (EditText) findViewById(R.id.ageEt);
        email = (EditText) findViewById(R.id.emailEt);
        password = (EditText) findViewById(R.id.passwordEt);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
//                if (mUser != null) {
//                    Toast.makeText(AuthenticationActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(AuthenticationActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
//                }

            }
        };

        score = getIntent().getExtras().getInt("score");
        level = (Level) getIntent().getExtras().getSerializable("level");

        next = (Button) findViewById(R.id.goNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(age.getText().toString()) &&
                        !TextUtils.isEmpty(email.getText().toString())) {
                    Name = name.getText().toString();
                    Age = age.getText().toString();
                    Email = email.getText().toString();
                    Password = password.getText().toString();
                    login(Email, Password);
                }

            }
        });

        enter();

    }

    public void login(String Eml, String Pwd) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(AuthenticationActivity.this.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null){
            Toast.makeText(AuthenticationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }else {
            mAuth.signInWithEmailAndPassword(Eml, Pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        postData();
                        Intent intent = new Intent(AuthenticationActivity.this, EndLevel.class);
                        intent.putExtra("score", score);
                        intent.putExtra("level", level);
                        // intent.putExtra("userId" , userId);
                        startActivity(intent);
                    } else {

                        AlertDialog();
                    }

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void check() {
        String UserEmail = email.getText().toString();
        String UserPassword = password.getText().toString();
        String UserName = name.getText().toString();
        String UserAge = age.getText().toString();

        if (TextUtils.isEmpty(UserEmail)) {
            email.setError("Can't Be Empty");
            return;
        } else if (TextUtils.isEmpty(UserPassword)) {
            password.setError("Can't Be Empty");
            return;
        } else if (TextUtils.isEmpty(UserName)) {
            name.setError("Can't Be Empty");
            return;
        } else if (TextUtils.isEmpty(UserAge)) {
            age.setError("Can't Be Empty");
            return;
        }


    }

    public void AlertDialog() {

        alertDialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.confirmation_dialog, null);
        Button noButton = (Button) view.findViewById(R.id.noBtn);
        Button yesButton = (Button) view.findViewById(R.id.yesBtn);
        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AuthenticationActivity.this, CreateAcountActivity.class);
                startActivityForResult(intent, 1);
                dialog.dismiss();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String resultName = data.getStringExtra("NAME");
                String resultAge = data.getStringExtra("AGE");
                String resultEmail = data.getStringExtra("EMAIL");
                String resultPassword = data.getStringExtra("PASSWORD");

                name.setText(resultName);
                age.setText(resultAge);
                email.setText(resultEmail);
                password.setText(resultPassword);

            }
        }
    }


  //todo send data to ahmed
    public void postData() {
        String URL = "http://cartaman.com/game/wp-json/org/v1/user_son";

        JSONObject json = new JSONObject();
        try {
            json.put("email" , Email );
            json.put("name" , Name);
            json.put("age" , Age);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // post API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response" , String.valueOf(response));
                        try {
                              String userId = response.getString("id");
                            Log.e("id" , String.valueOf(userId));
                            SharedPreferences.Editor editor = getSharedPreferences("ID", MODE_PRIVATE).edit();
                            editor.putString("user_id", userId);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error" , String.valueOf(error));
                        ConnectivityManager cm = (ConnectivityManager) getSystemService(AuthenticationActivity.this.CONNECTIVITY_SERVICE);
                        if (cm.getActiveNetworkInfo() == null){
                            Toast.makeText(AuthenticationActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 1000; // in 1 second cache will be hit, but also refreshed on background
                    final long cacheExpired = 5 * 1000; // in 5 seconds this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void enter(){
        email.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    password.requestFocus();
                }
                return false;
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    name.requestFocus();
                }
                return false;
            }
        });

        name.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    age.requestFocus();
                }
                return false;
            }
        });

    }
}
