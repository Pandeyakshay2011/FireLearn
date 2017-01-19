package com.example.anderson.firelearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private Button btImagen;
    private ImageView ivImagen;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorage = FirebaseStorage.getInstance().getReference();

        btImagen = (Button) findViewById(R.id.btImagen);

        ivImagen = (ImageView) findViewById(R.id.ivImagen);

        mProgress = new ProgressDialog(this);

        btImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && requestCode == RESULT_OK){

            mProgress.setMessage("Uploading Image...");
            mProgress.show();

            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("Photo").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.dismiss();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();

                    Picasso.with(MainActivity.this).load(downloadUri).fit().centerCrop().into(ivImagen);

                    Toast.makeText(MainActivity.this, "Uploading Finished...", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
}
