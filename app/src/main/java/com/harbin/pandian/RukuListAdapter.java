package com.harbin.pandian;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

public class RukuListAdapter extends RecyclerView.Adapter<RukuListAdapter.ViewHolder> {

//    private String[] mDataset;
    private JSONArray mDataset;

    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_id;
        public TextView tv_provider;
        public TextView tv_createTime, tv_creator, tv_store, tv_serialNumber;
        public CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.tv_ruku_list_id);
            tv_provider = (TextView) itemView.findViewById(R.id.tv_ruku_list_provider);
            tv_createTime = (TextView) itemView.findViewById(R.id.tv_ruku_list_create_time);
            tv_creator = (TextView) itemView.findViewById(R.id.tv_ruku_list_creator);
            tv_store = (TextView) itemView.findViewById(R.id.tv_ruku_list_store);
            tv_serialNumber = (TextView) itemView.findViewById(R.id.tv_ruku_list_serial_number);
            cardView = (CardView) itemView.findViewById(R.id.cv_rukulist);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RukuListAdapter(JSONArray myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RukuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_ruku_list_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        context = parent.getContext();
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try{
            final String id = mDataset.getJSONObject(position).getString("id").toString();
            final String provider = mDataset.getJSONObject(position).getString("supplier_name").toString();
            final String createTime = mDataset.getJSONObject(position).getString("create_time").toString();
            final String creator = mDataset.getJSONObject(position).getString("creator_name").toString();
            final String store = mDataset.getJSONObject(position).getString("store_name").toString();
            final String serial_number = mDataset.getJSONObject(position).getString("serial_number").toString();

            holder.tv_id.setText(id);
            holder.tv_provider.setText(provider);
            holder.tv_createTime.setText(createTime);
            holder.tv_creator.setText(creator);
            holder.tv_store.setText(store);
            holder.tv_serialNumber.setText(serial_number);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(context, RukudanActivity.class);
//                        intent.putExtra("rukudan", mDataset.getJSONObject(position).getString("detail").toString());
                        intent.putExtra("rukudan", id);
                        context.startActivity(intent);

                    }catch (Exception e){
                        Log.e("Error: ",e.getMessage());
                    }
                }
            });



//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try{
//                        Intent intent = new Intent(context, RukudanActivity.class);
////                        intent.putExtra("rukudan", mDataset.getJSONObject(position).getString("detail").toString());
//                        intent.putExtra("rukudan", id);
//                        context.startActivity(intent);
//
//                    }catch (Exception e){
//
//                    }
//
//                }
//            });



        }catch (Exception e){
            holder.tv_id.setText("Error");
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }
}

