
package com.alex.schwartzman.fivehundredpx.provider;

import android.content.Context;

import com.blandware.android.atleap.provider.ormlite.OrmLiteDatabaseHelper;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;

public class DefaultDatabaseHelper extends OrmLiteDatabaseHelper {

    public static final String DATABASE_NAME = "500px.db";
    public static final int DATABASE_VERSION = 1;

    public DefaultDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    public OrmLiteUriMatcher getUriMatcher() {
        return OrmLiteUriMatcher.getInstance(DefaultUriMatcher.class, DefaultContract.CONTENT_AUTHORITY);
    }
}
