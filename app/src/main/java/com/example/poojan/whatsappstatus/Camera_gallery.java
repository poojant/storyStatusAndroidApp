package com.example.poojan.whatsappstatus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;

public class Camera_gallery extends AppCompatActivity {

    private Button btngallery;
    private Button btncamera;
    private ImageView image;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_INTENT = 2;
    private StorageReference mStorage;
    private DatabaseReference mdatabase;
    private ProgressDialog mProgress;
    private String user;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_gallery);

        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("userid");
        mStorage = FirebaseStorage.getInstance().getReference();
        btngallery = (Button) findViewById(R.id.button2);
        btncamera = (Button) findViewById(R.id.button3);
        //image = (ImageView) findViewById(R.id.imageView3);
        mProgress = new ProgressDialog(this);

        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, GALLERY_INTENT);
            }
        });
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_REQUEST_CODE && resultCode == RESULT_OK )
        {
            count = new Random().nextInt(50000);

            mProgress.setMessage("Uploading your Status...");
            mProgress.show();
            /*Uri uri = data.getData();
            try {
                StorageReference filepath = mStorage.child("Story").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        Toast.makeText(Camera_gallery.this,"Status Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Camera_gallery.this,"Status Not Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch(Exception e)
            {
                Toast.makeText(Camera_gallery.this,"Error",Toast.LENGTH_SHORT).show();
            }*/
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataBAOS = baos.toByteArray();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

//name of the image file (add time to have different files to avoid rewrite on the same file)

            StorageReference imagesRef = storageRef.child(user).child("" + new Date().getTime());

            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(),"Sending failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    mdatabase = FirebaseDatabase.getInstance().getReference().child(user).child(user + count );
                    mdatabase.child("image").setValue(taskSnapshot.getDownloadUrl().toString());
                    mdatabase.child("Timestamp").setValue(ServerValue.TIMESTAMP);
                    mdatabase.child("Notification").setValue("New Status - "+user );
                    Toast.makeText(Camera_gallery.this,"Status Uploaded Successfully",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK )
        {
            count = new Random().nextInt(50000);

            mProgress.setMessage("Uploading your Status...");
            mProgress.show();
            Uri uri = data.getData();
            try {
                StorageReference filepath = mStorage.child(user).child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        mdatabase = FirebaseDatabase.getInstance().getReference().child(user).child(user + count );
                        mdatabase.child("image").setValue(taskSnapshot.getDownloadUrl().toString());
                        mdatabase.child("Timestamp").setValue(ServerValue.TIMESTAMP);
                        mdatabase.child("Notification").setValue("New Status - "+user );
                        /*FirebaseMessagingService obj = new MyFirebaseMessagingService();
                        obj.sendNotification(user);*/
                        Toast.makeText(Camera_gallery.this,"Status Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Camera_gallery.this,"Status Not Uploaded Successfully",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch(Exception e)
            {
                Toast.makeText(Camera_gallery.this,"Error",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
