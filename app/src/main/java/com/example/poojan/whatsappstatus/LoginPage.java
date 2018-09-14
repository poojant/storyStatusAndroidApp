package com.example.poojan.whatsappstatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {

    private EditText username;
    private Button loginbtn;
    private DatabaseReference mDatabaseRef;
    private ArrayList<String> mimageurl;
    private String user;
    private long count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.textuser);
        loginbtn = (Button) findViewById(R.id.btnlogin);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mimageurl = new ArrayList<>();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String upload = postSnapshot.getKey();
                            if(user.equals(upload)) {
                                count++;
                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        }
                        if(count==0)
                        {
                            Toast.makeText(LoginPage.this,"Invalid Username",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //Toast.makeText(LoginPage.this,"User is " +user,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
