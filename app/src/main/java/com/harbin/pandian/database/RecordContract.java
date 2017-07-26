package com.harbin.pandian.database;

import android.provider.BaseColumns;

/**
 * Created by Harbin on 7/3/17.
 */

public class RecordContract {
    public static final class RecordEntry implements BaseColumns{
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_BARCODE = "barcode";
    }
}
