package com.harbin.pandian.database;

import android.provider.BaseColumns;

/**
 * Created by Harbin on 7/21/17.
 */

public class GoodsContract {
    public static final class GoodsEntry implements BaseColumns {
        public static final String TABLE_NAME = "goods";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MERCHANDISE = "merchandise";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DONE = "done";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_LOCNAME = "loc_name";
        public static final String COLUMN_CERTIFICATE = "certificate";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_LOCCODE = "loc_code";
        public static final String COLUMN_ACCEPTENCE_QUANTITY = "acceptence_quantity";
        public static final String COLUMN_TOTAL_PRICE = "total_price";
        public static final String COLUMN_PRODUCTION_BATCH_NUMBER = "production_batch_number";
        public static final String COLUMN_PRODUCTION_DATE = "production_date";
        public static final String COLUMN_EFFECTIVE_DATE = "effective_date";
        public static final String COLUMN_MANUFACTURER = "manufacturer";
    }
}
