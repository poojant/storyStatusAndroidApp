package com.example.poojan.whatsappstatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView mystory;
    private TextView status;
    private TextView mystatus;
    public ArrayList<String> username,outer,inner;
    private ListView lv;
    private long count = 0;
    public long childcount;
    private String user;
    //public String upload;
    private DatabaseReference dbref, dbcommon, dbperson, dbtime, dbcount, dbtimeoutside, db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            user = bundle.getString("user");
            mystory = (ImageView)findViewById(R.id.imageView);
            status = (TextView)findViewById(R.id.textView2);
            mystatus = (TextView)findViewById(R.id.textView);
            lv = (ListView) findViewById(R.id.listview);
            username = new ArrayList<>();
            outer = new ArrayList<>();
            inner = new ArrayList<>();
            dbref = FirebaseDatabase.getInstance().getReference().child(user);
            dbcommon = FirebaseDatabase.getInstance().getReference();
            dbtime = FirebaseDatabase.getInstance().getReference();
            dbtime.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        final String namelvl1 = snapshot.getKey();
                        dbtimeoutside = dbtime.child(namelvl1);
                        dbtimeoutside.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                {
                                    //final long child = dataSnapshot.getChildrenCount();
                                    final String namelvl2 = postSnapshot.getKey();
                                    db = dbtime.child(namelvl1).child(namelvl2);
                                    db.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot obj : dataSnapshot.getChildren())
                                            {
                                                String ts = obj.getKey();
                                                if(ts.equals("Timestamp"))
                                                {
                                                    long time = obj.getValue(Long.class);
                                                    long tstamp = System.currentTimeMillis();
                                                    long diff = tstamp - time;
                                                    if(diff>86400000)
                                                    {
                                                        dbtime.child(namelvl1).child(namelvl2).removeValue();
                                                        dbcount = dbtime.child(namelvl1);
                                                        dbcount.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                long child = dataSnapshot.getChildrenCount();
                                                                if(child<=1)
                                                                {
                                                                    dbtime.child(namelvl1).setValue("random");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getChildrenCount();
                    //Toast.makeText(MainActivity.this,"count is"+count,Toast.LENGTH_SHORT).show();
                    if(count>0)
                    {
                        status.setText("Click the image to Add Status\nClick My status to View Status");
                    }
                    else{
                        status.setText("Click the image to Add Status");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            dbcommon.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        final String upload = postSnapshot.getKey();
                        if(upload.equals(user))
                        {

                        }
                        else{
                            dbperson = dbcommon.child(upload);
                            dbperson.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Toast.makeText(MainActivity.this,"count is"+username,Toast.LENGTH_SHORT).show();
                                    childcount = dataSnapshot.getChildrenCount();
                                    if(childcount>0) {
                                        username.add(upload);
                                        //Toast.makeText(MainActivity.this,"count is"+username,Toast.LENGTH_SHORT).show();
                                        MyAdapter myAdapter = new MyAdapter(MainActivity.this,username);
                                        lv.setAdapter(myAdapter);
                                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent3 = new Intent(MainActivity.this,StatusViewer.class);
                                                intent3.putExtra("userid",username.get(i));
                                                startActivity(intent3);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            // username.add(dbperson.getKey());
                            }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mystory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,Camera_gallery.class);
                    intent.putExtra("userid",user);
                    startActivity(intent);
                }
            });

            mystatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent(MainActivity.this,StatusViewer.class);
                    intent2.putExtra("userid",user);
                    startActivity(intent2);
                }
            });

        }
        }


}
