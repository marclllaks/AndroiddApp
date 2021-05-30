package com.example.application;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class PractitionerItemAdapter extends RecyclerView.Adapter<PractitionerItemAdapter.ViewHolder> implements Filterable {

    private static final int SECRET = 99;
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    private ArrayList<PractitionerItem> mItemData = new ArrayList<>();
    private ArrayList<PractitionerItem> mItemDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;

    PractitionerItemAdapter(Context context, ArrayList<PractitionerItem> itemsData) {
        this.mItemData = itemsData;
        this.mItemDataAll = itemsData;
        this.mContext = context;
    }

    public void toElement(int id) {
        Intent intent = new Intent(mContext, ElementActivity.class);
        intent.putExtra("SECRET", 99);
        intent.putExtra("ID", id);
        mContext.startActivity(intent);
    }


    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<PractitionerItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0) {
                results.count = mItemDataAll.size();
                results.values = mItemDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(PractitionerItem item : mItemDataAll) {
                    if(item.getName().toLowerCase().contains(filterPattern) || item.getIdentifier().toString().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mItemData = (ArrayList)filterResults.values;
            notifyDataSetChanged();
        }
    };

    public int getImage(String imageName) {

        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        return drawableResourceId;
    }



    @Override
    public PractitionerItemAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PractitionerItemAdapter.ViewHolder holder, int position) {

        PractitionerItem currentItem = mItemData.get(position);

        holder.bindTo(currentItem);


        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }

    }

    @Override
    public int getItemCount() {
        return mItemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mItemImage;
        private TextView mAktiv;



        public ViewHolder(View itemView) {

            super(itemView);
            mTitleText = itemView.findViewById(R.id.itemTitle);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mItemImage = itemView.findViewById(R.id.itemImage);
            mAktiv = itemView.findViewById(R.id.price);

            itemView.findViewById(R.id.add_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //((PractitionerListActivity)mContext).updateAlertIcon();
                    toElement(parseInt(mInfoText.getText().toString()));
                }
            });

        }


        void bindTo(PractitionerItem currentItem){
            mTitleText.setText(currentItem.getName());
            mInfoText.setText(currentItem.getIdentifier().toString());
            if (currentItem.isActive()){
                mAktiv.setText("Aktív");
            } else {
                mAktiv.setText("Inaktív");
            }

            // Load the images into the ImageView using the Glide library.
            Glide.with(mContext).load(getImage(currentItem.getPhoto().toString())).into(mItemImage);
        }
    }
}
