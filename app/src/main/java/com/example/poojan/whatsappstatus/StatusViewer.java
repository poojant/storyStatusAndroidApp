package com.example.poojan.whatsappstatus;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class StatusViewer extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private StorageReference storage;
    private DatabaseReference mDatabaseRef;
    private ArrayList<String> mimageurl;
    private String user;
    private ProgressBar progressDialog;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_viewer);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            user = bundle.getString("userid");
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(user);//.push().child("image");
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            progressDialog = findViewById(R.id.progressba);

            mimageurl = new ArrayList<String>();
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String upload = postSnapshot.child("image").getValue(String.class);
                        mimageurl.add(upload);
                    }
                    mAdapter = new ImageAdapter(StatusViewer.this, mimageurl);

                    mRecyclerView.setAdapter(mAdapter);
                    progressDialog.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(StatusViewer.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
}
}
