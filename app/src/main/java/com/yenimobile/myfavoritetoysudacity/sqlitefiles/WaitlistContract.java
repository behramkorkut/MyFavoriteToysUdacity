package com.yenimobile.myfavoritetoysudacity.sqlitefiles;

import android.provider.BaseColumns;

import java.util.logging.StreamHandler;

public class WaitlistContract {

    private WaitlistContract(){}

    public static final class WaitlistEntry implements BaseColumns {
        public static final String TABLE_NAME = "waitlist";
        public static final String COLUMN_GUEST_NAME = "guestName";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
