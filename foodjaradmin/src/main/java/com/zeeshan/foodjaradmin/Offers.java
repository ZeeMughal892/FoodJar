package com.zeeshan.foodjaradmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.zeeshan.foodjaradmin.entities.Items;
import com.zeeshan.foodjaradmin.entities.OffersModel;

public class Offers extends AppCompatActivity {

    EditText edTitle;
    static final int PICK_IMAGE_REQUEST = 1;
    ImageView btnSelectImage;
    Uri imageUri;
    StorageReference storageReference;
    DatabaseReference databaseOffers;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBarOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        init();
        setUpToolbar();
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }

    private void addOffer() {
        final String title = edTitle.getText().toString().trim();
        if (!title.isEmpty() && imageUri == null) {
            progressBarOffer.setVisibility(View.VISIBLE);
            String offerId = databaseOffers.push().getKey();
            OffersModel offer = new OffersModel(offerId, title, firebaseUser.getUid());
            databaseOffers.child(offerId).setValue(offer);
            Toast.makeText(Offers.this, "Offer Added Successfully", Toast.LENGTH_SHORT).show();
            progressBarOffer.setVisibility(View.GONE);
            edTitle.setText(null);
        } else if (imageUri != null && title.isEmpty()) {
            progressBarOffer.setVisibility(View.VISIBLE);
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            String offerId = databaseOffers.push().getKey();
                            OffersModel offer = new OffersModel(offerId, url, firebaseUser.getUid());
                            databaseOffers.child(offerId).setValue(offer);
                            Toast.makeText(Offers.this, "Offer Banner Added Successfully", Toast.LENGTH_SHORT).show();
                            progressBarOffer.setVisibility(View.GONE);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Offers.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            imageUri = null;
        } else {
            Toast.makeText(Offers.this, "Please Insert One of the above entries", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btnSave) {
            addOffer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Offers.this,SearchItem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(btnSelectImage);
        }
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseOffers = FirebaseDatabase.getInstance().getReference("offers");
        storageReference = FirebaseStorage.getInstance().getReference("offers");
        btnSelectImage = findViewById(R.id.imgSelectOfferImage);
        toolbar = findViewById(R.id.toolbar);
        edTitle = findViewById(R.id.edTitle);
        progressBarOffer = findViewById(R.id.progressBarOffer);
    }

}
