package com.zeeshan.foodjaradmin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zeeshan.foodjaradmin.R;
import com.zeeshan.foodjaradmin.SearchItem;
import com.zeeshan.foodjaradmin.entities.Items;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    private List<Items> itemsList;
    private List<Items> itemsListFull;
    private Button btnUpdate;
    DatabaseReference databaseProducts;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemCategory, itemPrice,  itemDescription;
        ImageView itemImage;
        EditText edStock;
        String itemID;


        MyViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.txtName);
            itemCategory = itemView.findViewById(R.id.txtCategory);
            itemPrice = itemView.findViewById(R.id.txtPrice);
            edStock = itemView.findViewById(R.id.ed_Stock);
            itemDescription = itemView.findViewById(R.id.txtDescription);
            itemImage = itemView.findViewById(R.id.imgItem);
            btnUpdate=itemView.findViewById(R.id.btnUpdate);
        }

    }

    public ItemAdapter(List<Items> itemsList) {
        this.itemsList = itemsList;
        itemsListFull = new ArrayList<>(itemsList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_update_stock, viewGroup, false);
        final MyViewHolder myViewHolder= new MyViewHolder(itemView);

        databaseProducts= FirebaseDatabase.getInstance().getReference("Products");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int position=myViewHolder.getAdapterPosition();

                String itemId=itemsList.get(position).getItemId();
                String name= itemsList.get(position).getItemName();
                String category=itemsList.get(position).getItemCategory();
                String stock=myViewHolder.edStock.getText().toString().trim();
                String unit=itemsList.get(position).getItemUnit();
                String price=itemsList.get(position).getItemPrice();
                String desc=itemsList.get(position).getItemDescription();
                String img=itemsList.get(position).getItemImage();

                Items items=new Items(itemId,name,category,stock,unit,price,img,desc, SearchItem.loginUserID);
                databaseProducts.child(itemId).setValue(items);
                Toast.makeText(itemView.getContext(),"Stock Updated Successfully",Toast.LENGTH_SHORT).show();
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Items items = itemsList.get(position);
        String Stock = items.getItemStock();
        String Price = "Rs. " + items.getItemPrice();


        holder.itemName.setText(items.getItemName());
        holder.itemCategory.setText(items.getItemCategory());
        holder.itemDescription.setText(items.getItemDescription());
        holder.itemPrice.setText(Price);
        holder.edStock.setText(Stock);

        Picasso.get()
                .load(items.getItemImage())
                .placeholder(R.drawable.ic_noimage)
                .fit()
                .centerCrop()
                .into(holder.itemImage);

        holder.itemID = items.getItemId();
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
            itemsList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

}
