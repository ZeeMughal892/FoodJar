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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    EditText ed_Name, ed_Price, ed_Quantity, ed_StockPerPack, ed_Description;
    static final int PICK_IMAGE_REQUEST = 1;
    ImageButton btnSelectImage;
    Uri imageUri;
    ProgressBar progressBar;
    StorageReference storageReference;
    DatabaseReference databaseProducts;
    Spinner spinnerCategories, spinnerUnit;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        init();
        setUpToolbar();
        progressBar.setVisibility(View.GONE);
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
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addItem() {
        progressBar.setVisibility(View.VISIBLE);

        final String itemName = ed_Name.getText().toString().trim();
        final String itemCategory = spinnerCategories.getSelectedItem().toString().trim();
        final String itemPrice = ed_Price.getText().toString().trim();
        final String itemQuantity = ed_Quantity.getText().toString().trim();
        final String itemQuantityPerPack = ed_StockPerPack.getText().toString().trim();
        final String itemUnit = spinnerUnit.getSelectedItem().toString().trim();
        final String itemDescription = ed_Description.getText().toString().trim();

        if (TextUtils.isEmpty(itemName)) {
            Toast.makeText(this, "Please Enter Item Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemCategory)) {
            Toast.makeText(this, "Please Enter Item Category", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemPrice)) {
            Toast.makeText(this, "Please Enter Item Price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemQuantity)) {
            Toast.makeText(this, "Please Enter Item Quantity", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(itemQuantityPerPack)) {
            Toast.makeText(this, "Please Enter Quantity Per Pack", Toast.LENGTH_SHORT).show();
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
                            Items items = new Items(itemId, itemName, itemCategory, itemQuantity, itemQuantityPerPack, itemUnit, itemPrice, url, itemDescription, firebaseUser.getUid());
                            databaseProducts.child(itemId).setValue(items);
                            clearFields();
                            Toast.makeText(AddNewItem.this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            });
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

    private void clearFields() {
        ed_Name.setText(null);
        ed_Price.setText(null);
        ed_Description.setText(null);
        ed_Quantity.setText(null);
        ed_StockPerPack.setText(null);
        btnSelectImage.setImageResource(R.drawable.ic_add_circle_black_24dp);
    }

    private void setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddNewItem.this,SearchItem.class);
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
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        storageReference = FirebaseStorage.getInstance().getReference("products");
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ed_Name = findViewById(R.id.ed_Name);
        spinnerCategories = findViewById(R.id.spinnerCategory);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        ed_Price = findViewById(R.id.ed_Price);
        ed_Quantity = findViewById(R.id.ed_Quantity);
        ed_StockPerPack = findViewById(R.id.ed_QuantityPerPack);
        ed_Description = findViewById(R.id.ed_Description);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBarAdd);
    }
}
