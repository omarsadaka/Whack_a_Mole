package com.organizers_group.whack_a_mole.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.organizers_group.whack_a_mole.R;
import com.organizers_group.whack_a_mole.levels.EndLevel;

import java.util.HashMap;
import java.util.Map;

public class CreateAcountActivity extends VolumeControlActivity {
    private EditText Uname;
    private EditText Uage;
    private EditText Uemail;
    private EditText Upassword;
    private Button signIn;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

        Uname = (EditText)findViewById(R.id.name);
        Uage = (EditText)findViewById(R.id.age);
        Uemail = (EditText)findViewById(R.id.email);
        Upassword = (EditText)findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.signIn);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                ConnectivityManager cm = (ConnectivityManager) getSystemService(CreateAcountActivity.this.CONNECTIVITY_SERVICE);
                if (cm.getActiveNetworkInfo() == null){
                    Toast.makeText(CreateAcountActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }else {
                    createNewAccount();
                }


            }
        });
        enter();

    }

    private void createNewAccount() {

        final String Name = Uname.getText().toString().trim();
        final String Age = Uage.getText().toString().trim();
         final String Email = Uemail.getText().toString().trim();
         final String Password = Upassword.getText().toString().trim();
         if (Password.length() >= 6) {
             progressDialog.setMessage("Creating Account...");
             progressDialog.show();
             mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(CreateAcountActivity.this,
                     new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreateAcountActivity.this, "Account Created successfully", Toast.LENGTH_LONG).show();
                                    String userID = mAuth.getCurrentUser().getUid();
                                    DatabaseReference currentUserId = mReference.child(userID);
                                    currentUserId.child("UserName").setValue(Name);
                                    currentUserId.child("Age").setValue(Age);
                                    currentUserId.child("Image").setValue("none");

//                       Map<String , String> dataToSave = new HashMap<>( );
//                       dataToSave.put ( "UserName" , Name );
//                       dataToSave.put ( "Age" , Age );
//                       dataToSave.put ( "image" , "none" );
//                       currentUserId.setValue(dataToSave);
                                    progressDialog.dismiss();
                                    Intent returnIntent = new Intent(CreateAcountActivity.this, AuthenticationActivity.class);
                                    returnIntent.putExtra("NAME", Name);
                                    returnIntent.putExtra("AGE", Age);
                                    returnIntent.putExtra("EMAIL", Email);
                                    returnIntent.putExtra("PASSWORD", Password);
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();

                                }else if (!task.isSuccessful()) {

                                        Log.e("Failed", String.valueOf(task.getException()));
                                        Toast.makeText(CreateAcountActivity.this, "Failed To Create Account" + task.getException(), Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                         }
                     });
         }else {
             Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
         }



    }

    public void check(){
        String UserName = Uname.getText().toString();
        String UserAge = Uage.getText().toString();
        String UserEmail = Uemail.getText().toString();
        String UserPassword = Upassword.getText().toString();

        if (TextUtils.isEmpty(UserName)){
            Uname.setError("Can't Be Empty");
            return;
        }
        else if (TextUtils.isEmpty(UserAge)){
            Uage.setError("Can't Be Empty");
            return;
        }else if (TextUtils.isEmpty(UserEmail)){
            Uemail.setError("Can't Be Empty");
            return;
        }else if (TextUtils.isEmpty(UserPassword)){
            Upassword.setError("Can't Be Empty");
            return;
        }

    }

    public void enter(){
        Uname.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    Uage.requestFocus();
                }
                return false;
            }
        });

        Uage.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    Uemail.requestFocus();
                }
                return false;
            }
        });

        Uemail.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == 66) {
                    Upassword.requestFocus();
                }
                return false;
            }
        });

    }
}
