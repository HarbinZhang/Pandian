package com.harbin.pandian.database;

import android.provider.BaseColumns;

/**
 * Created by Harbin on 7/26/17.
 */

public class RukuListContract {
    public static final class RukuListEntry implements BaseColumns {
        public static final String TABLE_NAME = "rukudan";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SERIAL_NUMBER = "serial_number";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_STORE_NAME = "store_name";
        public static final String COLUMN_CREATOR_NAME = "creator_name";
        public static final String COLUMN_CREATE_TIME = "create_time";
        public static final String COLUMN_DONE = "done";
    }
}
