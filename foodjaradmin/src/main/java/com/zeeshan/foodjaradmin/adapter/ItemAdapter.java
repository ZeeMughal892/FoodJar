package com.zeeshan.foodjaradmin.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.SearchItem;
import com.zeeshan.foodjaradmin.entities.Items;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    private List<Items> itemsList;
    private List<Items> itemsListFull;
    private DatabaseReference databaseProducts;
    private FirebaseUser firebaseUser;
    Button btnUpdateItem, btnDeleteItem;
    ImageView imgItem;
    EditText edItemName, edStockItem, edItemQuantityPerPack, edItemPrice, edDescription;
    Spinner spinnerCategories, spinnerUnit;
    Items currentItems;
    Dialog dialogItem;
    private String itemID;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemCategory, itemPrice,itemQuantity, itemDescription;
        ImageView itemImage;
        ConstraintLayout cardViewItems;


        MyViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txtName);
            itemCategory = itemView.findViewById(R.id.txtCategory);
            itemQuantity = itemView.findViewById(R.id.txtQuantity);
            itemPrice = itemView.findViewById(R.id.txtPrice);
            itemDescription = itemView.findViewById(R.id.txtDescription);
            itemImage = itemView.findViewById(R.id.imgItem);
            cardViewItems = itemView.findViewById(R.id.cardViewItem);
        }
    }

    public ItemAdapter(List<Items> itemsList) {
        this.itemsList = itemsList;
        itemsListFull = new ArrayList<>(itemsList);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(itemView);

        dialogItem = new Dialog(itemView.getContext());
        dialogItem.setContentView(R.layout.dialog_update_item);
        dialogItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");

        myViewHolder.cardViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseProducts.child(itemsList.get(myViewHolder.getAdapterPosition()).getItemId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentItems = dataSnapshot.getValue(Items.class);
                        if (currentItems != null) {
                            edItemName.setText(currentItems.getItemName());
                            edItemPrice.setText(currentItems.getItemPrice());
                            edDescription.setText(currentItems.getItemDescription());
                            edItemQuantityPerPack.setText(currentItems.getItemStockPerPack());
                            edStockItem.setText(currentItems.getItemStock());

                            Picasso.get()
                                    .load(currentItems.getItemImage())
                                    .fit()
                                    .placeholder(R.drawable.app_logo)
                                    .centerCrop()
                                    .into(imgItem);
                            spinnerCategories.setSelection(((ArrayAdapter) spinnerCategories.getAdapter()).getPosition(currentItems.getItemCategory()));
                            spinnerUnit.setSelection(((ArrayAdapter) spinnerUnit.getAdapter()).getPosition(currentItems.getItemUnit()));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(itemView.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                edItemName = dialogItem.findViewById(R.id.ed_ItemName);
                edDescription = dialogItem.findViewById(R.id.ed_Description);
                edItemPrice = dialogItem.findViewById(R.id.ed_ItemPrice);
                edItemQuantityPerPack = dialogItem.findViewById(R.id.ed_QuantityPerPack);
                edStockItem = dialogItem.findViewById(R.id.ed_Stcok);
                imgItem = dialogItem.findViewById(R.id.imgItem);
                spinnerCategories = dialogItem.findViewById(R.id.spinnerCategory);
                spinnerUnit = dialogItem.findViewById(R.id.spinnerUnit);
                btnUpdateItem = dialogItem.findViewById(R.id.btnUpdateItem);
                btnDeleteItem = dialogItem.findViewById(R.id.btnDeleteItem);

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                btnUpdateItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id = currentItems.getItemId();
                        String name = edItemName.getText().toString().trim();
                        String category = spinnerCategories.getSelectedItem().toString().trim();
                        String stock = edStockItem.getText().toString().trim();
                        String stockPerPack = edItemQuantityPerPack.getText().toString().trim();
                        String unit = spinnerUnit.getSelectedItem().toString().trim();
                        String price = edItemPrice.getText().toString().trim();
                        String desc = edDescription.getText().toString().trim();
                        String img = currentItems.getItemImage();

                        Items items = new Items(id, name, category, stock, stockPerPack, unit, price, img, desc, firebaseUser.getUid());
                        databaseProducts.child(id).setValue(items);
                        Toast.makeText(itemView.getContext(), "Item Updated Successfully", Toast.LENGTH_SHORT).show();
                        dialogItem.dismiss();
                    }
                });
                btnDeleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                        builder.setTitle(R.string.app_name);
                        builder.setIcon(R.mipmap.ic_launcher);
                        builder.setMessage("Are you sure you want to delete this item?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        databaseProducts.child(itemID).removeValue();
                                        dialogItem.dismiss();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                dialogItem.show();
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Items items = itemsList.get(position);
        String Price = "SAR " + items.getItemPrice();
        String Stock = " " + items.getItemStock() + " " + items.getItemUnit() + " Left";
        holder.itemName.setText(items.getItemName());
        holder.itemCategory.setText(items.getItemCategory());
        holder.itemQuantity.setText(Stock);
        holder.itemDescription.setText(items.getItemDescription());
        holder.itemPrice.setText(Price);
        itemID = items.getItemId();
        Picasso.get()
                .load(items.getItemImage())
                .placeholder(R.drawable.ic_noimage)
                .fit()
                .centerCrop()
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public Filter getFilter() {
        return itemsFilter;
    }

    private Filter itemsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Items> filteredProductsList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredProductsList.addAll(itemsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Items items : itemsListFull) {
                    if (items.getItemName().toLowerCase().contains(filterPattern) || items.getItemCategory().toLowerCase().contains(filterPattern)) {
                        filteredProductsList.add(items);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredProductsList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemsList.clear();
            itemsList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
