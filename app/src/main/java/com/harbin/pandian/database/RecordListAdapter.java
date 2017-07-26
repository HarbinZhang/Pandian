package com.harbin.pandian.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harbin.pandian.R;

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {

    private Context mContext;

    private Cursor mCursor;


    private SQLiteDatabase mDb;

    public RecordListAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;

        RecordDbHelper dbHelper = new RecordDbHelper(context);
        mDb = dbHelper.getWritableDatabase();

    }


    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_record_list_adapter, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecordViewHolder holder, int position) {

        if(!mCursor.moveToPosition(position)){
            return ;
        }

        final String barcode = mCursor.getString(mCursor.getColumnIndex(RecordContract.RecordEntry.COLUMN_BARCODE));

        final String time = mCursor.getString(mCursor.getColumnIndex(RecordContract.RecordEntry.COLUMN_TIMESTAMP));
        long id = mCursor.getLong(mCursor.getColumnIndex(RecordContract.RecordEntry._ID));

        holder.itemView.setTag(id);
        holder.tv_id.setText(String.valueOf(id));
        holder.tv_barcode.setText(barcode);
        holder.tv_timestamp.setText(time);


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor){
        if(mCursor != null) mCursor.close();
        mCursor = newCursor;
        if(newCursor != null){
            this.notifyDataSetChanged();
        }
    }




    class RecordViewHolder extends RecyclerView.ViewHolder{

        TextView tv_timestamp;
        TextView tv_barcode;
        TextView tv_id;

        public RecordViewHolder(View itemView){
            super(itemView);

            tv_id = (TextView) itemView.findViewById(R.id.tv_record_id);
            tv_barcode = (TextView) itemView.findViewById(R.id.tv_record_barcode);
            tv_timestamp = (TextView) itemView.findViewById(R.id.tv_record_time);

        }

    }


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

}

