
package com.alex.schwartzman.fivehundredpx.provider;

import com.blandware.android.atleap.provider.ormlite.OrmLiteProvider;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class DefaultProvider extends OrmLiteProvider {

    @Override
    protected OrmLiteSqliteOpenHelper createHelper() {
        return new DefaultDatabaseHelper(getContext());
    }

    @Override
    public OrmLiteUriMatcher getUriMatcher() {
        return OrmLiteUriMatcher.getInstance(DefaultUriMatcher.class, DefaultContract.CONTENT_AUTHORITY);
    }

    @Override
    public String getAuthority() {
        return DefaultContract.CONTENT_AUTHORITY;
    }
}
