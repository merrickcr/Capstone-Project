package com.example.atv684.positivityreminders.provider;

import android.content.ContentResolver;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Arrays;

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

    public static final class ScheduleEntry implements BaseColumns {

        public static final String MONDAY = "M";
        public static final String TUESDAY = "T";
        public static final String WEDNESDAY = "W";
        public static final String THURSDAY = "Tr";
        public static final String FRIDAY = "F";
        public static final String SATURDAY = "Sat";
        public static final String SUNDAY = "Sun";

        public static final ArrayList<String> FULL_WEEK = new ArrayList<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY,
            SATURDAY, SUNDAY));

        public static final String TABLE_NAME = "SCHEDULES";

        public static final String COLUMN_TIME = "time";

        public static final String COLUMN_DAYS = "days";

    }

}
