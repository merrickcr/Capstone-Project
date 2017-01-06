package com.example.atv684.positivityreminders.provider;

import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * Created by atv684 on 9/19/16.
 */
public class QuotesContract {

    public static final class QuoteEntry implements BaseColumns {

        public static final String TABLE_NAME = "QUOTES";

        public static final String COLUMN_TEXT = "text";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_NUM_VIEWS = "num_views";

        public static final String COLUMN_FAVORITE = "is_favorite";

        public static final String COLUMN_CUSTOM = "is_custom";

        public static final String COLUMN_IMAGE = "image";

    }

}
