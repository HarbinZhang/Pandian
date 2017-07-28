package com.harbin.pandian;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harbin.pandian.database.GoodsContract;
import com.harbin.pandian.database.GoodsDbHelper;

public class RukudanAdapter extends RecyclerView.Adapter<RukudanAdapter.ViewHolder> {

    //    private String[] mDataset;

    private Context context;

    private Cursor cursor;

    private SQLiteDatabase mDb;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cv_container;
        public TextView tv_id;
        public TextView tv_name;
        public TextView tv_quantity, tv_unit, tv_loc_name, tv_certifate;
        public TextView tv_model, tv_loc_code, tv_acceptence_quantity, tv_total_price, tv_production_batch_number;
        public TextView tv_production_date, tv_effective_date, tv_manufacturer;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.tv_rukudan_id);
            tv_name = (TextView) itemView.findViewById(R.id.tv_rukudan_name);
            tv_quantity = (TextView) itemView.findViewById(R.id.tv_rukudan_quantity);
            tv_unit = (TextView) itemView.findViewById(R.id.tv_rukudan_unit);
            tv_loc_name = (TextView) itemView.findViewById(R.id.tv_rukudan_loc_name);
            tv_certifate = (TextView) itemView.findViewById(R.id.tv_rukudan_certificate);
            cv_container = (CardView) itemView.findViewById(R.id.cv_rukudan);

            tv_model = (TextView) itemView.findViewById(R.id.tv_rukudan_model);
            tv_loc_code = (TextView) itemView.findViewById(R.id.tv_rukudan_loc_code);
            tv_acceptence_quantity = (TextView) itemView.findViewById(R.id.tv_rukudan_acceptence_quantity);
            tv_total_price = (TextView) itemView.findViewById(R.id.tv_rukudan_total_price);
            tv_production_batch_number = (TextView) itemView.findViewById(R.id.tv_rukudan_production_batch_number);
            tv_production_date = (TextView) itemView.findViewById(R.id.tv_rukudan_production_date);
            tv_effective_date = (TextView) itemView.findViewById(R.id.tv_rukudan_effective_date);
            tv_manufacturer = (TextView) itemView.findViewById(R.id.tv_rukudan_manufacturer);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RukudanAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;

        GoodsDbHelper dbHelper = new GoodsDbHelper(context);
        mDb = dbHelper.getWritableDatabase();

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        editor.putString("scanned_loc_code", null);
        editor.apply();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RukudanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_rukudan_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);



        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(!cursor.moveToPosition(position)){
            return;
        }

        final String id = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_ID));
        final String name = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_NAME));
        final int done = cursor.getInt(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_DONE));
        final long sqlId = cursor.getLong(cursor.getColumnIndex(GoodsContract.GoodsEntry._ID));
        final String quantity = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_QUANTITY));
        final String unit = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_UNIT));
        final String loc_name = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_LOCNAME));
        final String certificate = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_CERTIFICATE));

        final String model = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_MODEL));
        final String loc_code = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_LOCCODE));
        final String acceptence_quantity = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_ACCEPTENCE_QUANTITY));
        final String total_price = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_TOTAL_PRICE));
        final String production_batch_number = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_PRODUCTION_BATCH_NUMBER));
        final String production_date = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_PRODUCTION_DATE));
        final String effective_date = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_EFFECTIVE_DATE));
        final String manufacturer = cursor.getString(cursor.getColumnIndex(GoodsContract.GoodsEntry.COLUMN_MANUFACTURER));


        holder.tv_name.setText(name);
        holder.tv_id.setText(id);
        holder.tv_certifate.setText(certificate);
        holder.tv_quantity.setText(quantity);
        holder.tv_unit.setText(unit);
        holder.tv_loc_name.setText(loc_name);


        holder.tv_model.setText(model);
        holder.tv_loc_code.setText(loc_code);
        holder.tv_acceptence_quantity.setText(acceptence_quantity);
        holder.tv_total_price.setText(total_price);
        holder.tv_production_batch_number.setText(production_batch_number);
        holder.tv_production_date.setText(production_date);
        holder.tv_effective_date.setText(effective_date);
        holder.tv_manufacturer.setText(manufacturer);




        if (done == 1){
//            holder.itemView.setBackgroundColor(Color.GRAY);
            holder.cv_container.setCardBackgroundColor(Color.GRAY);
        }

        holder.cv_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context, GoodsActivity.class);


                final String loc_code = prefs.getString("scanned_loc_code",null);
                final String loc_name = prefs.getString("curt_loc_name", null);



                if(loc_name == null){
                    intent.putExtra("loc_name", "");
                }else{
//                    detail = new CheckInDetail(id, quantity, unit, loc_code, loc_name, certificate);
                    intent.putExtra("loc_name", loc_name);
                }

                intent.putExtra("id", id);
                intent.putExtra("quantity", quantity);
                intent.putExtra("unit", unit);
                intent.putExtra("loc_code", loc_code);
                intent.putExtra("certificate", certificate);
                intent.putExtra("name", name);
                intent.putExtra("sqlId", sqlId);



                if (loc_code == null){
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("货架未锁定")
                            .setMessage("确定继续吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    return;
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    context.startActivity(intent);
                }



            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                CheckInDetail detail;
//
//
//                Intent intent = new Intent(context, GoodsActivity.class);
//
//
//                String loc_code = prefs.getString("scanned_loc_code",null);
//                String loc_name = prefs.getString("curt_loc_name", null);
////                if (loc_code == null){
////                    Toast.makeText(v.getContext(), "请先锁定货架", Toast.LENGTH_SHORT).show();
////                    return ;
////                }else
//                    if(loc_name == null){
////                    detail = new CheckInDetail(id, quantity, unit, loc_code, "", certificate);
//                    intent.putExtra("loc_name", "");
//                }else{
////                    detail = new CheckInDetail(id, quantity, unit, loc_code, loc_name, certificate);
//                    intent.putExtra("loc_name", loc_name);
//                }
//
//                intent.putExtra("id", id);
//                intent.putExtra("quantity", quantity);
//                intent.putExtra("unit", unit);
//                intent.putExtra("loc_code", loc_code);
//                intent.putExtra("certificate", certificate);
//                intent.putExtra("name", name);
//                intent.putExtra("sqlId", sqlId);
////                Intent.putExtra("Detail", detail);
//                context.startActivity(intent);
//            }
//        });



//        try{
//            final String id = mDataset.getJSONObject(position).getString("ID").toString();
//            holder.tv_id.setText(id);
//            final String name = mDataset.getJSONObject(position).getString("name").toString();
//            holder.tv_name.setText(name);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try{
//                        Intent intent = new Intent(context, GoodsActivity.class);
//                        context.startActivity(intent);
//                    }catch (Exception e){
//
//                    }
//
//                }
//            });
//        }catch (Exception e){
//            holder.tv_id.setText("Error");
//        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if(cursor != null) cursor.close();
        cursor = newCursor;
        if(newCursor != null){
            this.notifyDataSetChanged();
        }
    }

    private Cursor getAllGoods(){
        return mDb.query(
                GoodsContract.GoodsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}

