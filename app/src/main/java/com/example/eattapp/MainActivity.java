package com.example.eattapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.eattapp.Common.Common;
import com.example.eattapp.Model.Shipper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    FButton btn_sign_in;
    MaterialEditText edt_phone, edt_password;

    FirebaseDatabase database;
    DatabaseReference shippers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_in=(FButton)findViewById(R.id.btnSignIn);
        edt_password=(MaterialEditText)findViewById(R.id.edtPassword);
        edt_phone=(MaterialEditText)findViewById(R.id.edtPhone);

        database= FirebaseDatabase.getInstance();
        shippers=database.getReference(Common.SHIPPER_TABLE);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(edt_phone.getText().toString(),edt_password.getText().toString());

            }
        });


    }

    private void login(String phone, final String password) {
        shippers.child(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Shipper shipper=dataSnapshot.getValue(Shipper.class);
                            if ((shipper.getPassword().equals(password))) {
                                //login succeed
                              startActivity(new Intent(MainActivity.this,HomeActivity.class));
                              Common.currentShipper= shipper;
                              finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            Toast.makeText(MainActivity.this, "Shipper's phone does not exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
