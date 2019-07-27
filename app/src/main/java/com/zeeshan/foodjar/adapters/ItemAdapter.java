package com.zeeshan.foodjar.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjar.R;
import com.zeeshan.foodjar.ItemDetail;
import com.zeeshan.foodjar.entities.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    private List<Products> productsList;
    private List<Products> productsListFull;


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView itemName, itemCategory, itemPrice, itemQuantity, itemDescription;
        ImageView itemImage;
        String itemID;


        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            itemName = itemView.findViewById(R.id.txtName);
            itemCategory = itemView.findViewById(R.id.txtCategory);
            itemPrice = itemView.findViewById(R.id.txtPrice);
            itemQuantity = itemView.findViewById(R.id.txtQuantity);
            itemDescription = itemView.findViewById(R.id.txtDescription);
            itemImage = itemView.findViewById(R.id.imgItem);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(itemView.getContext(), ItemDetail.class);
            intent.putExtra("ItemID", itemID);
            ItemDetail.itemID = itemID;
            itemView.getContext().startActivity(intent);
        }
    }

    public ItemAdapter(List<Products> productsList) {
        this.productsList = productsList;
        productsListFull = new ArrayList<>(productsList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Products products = productsList.get(position);
        String Stock = " " + products.getItemStock() + " " + products.getItemUnit() + " Left";
        String Price = "Rs. " + products.getItemPrice();


        holder.itemName.setText(products.getItemName());
        holder.itemCategory.setText(products.getItemCategory());
        holder.itemDescription.setText(products.getItemDescription());
        holder.itemPrice.setText(Price);
        holder.itemQuantity.setText(Stock);

        Picasso.get()
                .load(products.getItemImage())
                .placeholder(R.drawable.ic_noimage)
                .fit()
                .centerCrop()
                .into(holder.itemImage);

        holder.itemID = products.getItemId();
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {
        return productsFilter;
    }

    private Filter productsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Products> filteredProductsList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredProductsList.addAll(productsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Products products : productsListFull) {
                    if (products.getItemName().toLowerCase().contains(filterPattern) || products.getItemCategory().toLowerCase().contains(filterPattern)) {
                        filteredProductsList.add(products);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredProductsList;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productsList.clear();
                productsList.addAll((List)filterResults.values);
                notifyDataSetChanged();
        }
    };

}
