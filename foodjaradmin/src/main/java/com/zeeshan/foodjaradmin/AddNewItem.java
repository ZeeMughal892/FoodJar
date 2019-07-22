package com.zeeshan.foodjaradmin;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.entities.Items;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddNewItem extends AppCompatActivity {

    private EditText ed_Name, ed_Category, ed_Price, ed_Quantity, ed_Unit, ed_Description;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnSelectImage;
    private Uri imageUri;
    ProgressBar progressBar;

    private StorageReference storageReference;
    private DatabaseReference databaseProducts;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        init();
        setUpToolbar();
        progressBar.setVisibility(View.INVISIBLE);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

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
            addItem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        databaseProducts = FirebaseDatabase.getInstance().getReference("Products");
        storageReference = FirebaseStorage.getInstance().getReference("Products");

        btnSelectImage = findViewById(R.id.btnSelectImage);
        ed_Name = findViewById(R.id.ed_Name);
        ed_Category = findViewById(R.id.ed_Category);
        ed_Price = findViewById(R.id.ed_Price);
        ed_Quantity = findViewById(R.id.ed_Quantity);
        ed_Unit = findViewById(R.id.ed_Unit);
        ed_Description = findViewById(R.id.ed_Description);
        toolbar = findViewById(R.id.toolbar);
        progressBar=findViewById(R.id.progressBarAdd);
    }


    private void addItem() {
        progressBar.setVisibility(View.VISIBLE);

        final String itemName = ed_Name.getText().toString().trim();
        final String itemCategory = ed_Category.getText().toString().trim();
        final String itemPrice = ed_Price.getText().toString().trim();
        final String itemQuantity = ed_Quantity.getText().toString().trim();
        final String itemUnit = ed_Unit.getText().toString().trim();
        final String itemDescription = ed_Description.getText().toString().trim();

        if (TextUtils.isEmpty(itemName)) {
            Toast.makeText(this, "Please Enter Item Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemCategory)) {
            Toast.makeText(this, "Please Enter Item Category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemPrice)) {
            Toast.makeText(this, "Please Enter Item Price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemQuantity)) {
            Toast.makeText(this, "Please Enter Item Quantity", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemUnit)) {
            Toast.makeText(this, "Please Enter Unit of Quantity", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(this, "Please Select an Item Image", Toast.LENGTH_SHORT).show();
        } else {

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            String itemId = databaseProducts.push().getKey();
                            Items items = new Items(itemId, itemName, itemCategory, itemQuantity, itemUnit, itemPrice, url, itemDescription, SearchItem.loginUserID);
                            databaseProducts.child(itemId).setValue(items);
                            clearFields();
                            Toast.makeText(AddNewItem.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }
            });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void clearFields() {
        ed_Name.setText(null);
        ed_Price.setText(null);
        ed_Description.setText(null);
        ed_Quantity.setText(null);
        ed_Category.setText(null);
        ed_Unit.setText(null);

        btnSelectImage.setImageResource(R.drawable.ic_add_circle_black_24dp);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewItem.this, SearchItem.class));

            }
        });
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
}
