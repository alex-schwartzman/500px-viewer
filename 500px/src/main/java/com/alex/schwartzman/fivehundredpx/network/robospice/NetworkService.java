
package com.alex.schwartzman.fivehundredpx.network.robospice;

import android.app.Application;

import com.alex.schwartzman.fivehundredpx.network.retrofit.FiveHundredPxApi;
import com.alex.schwartzman.fivehundredpx.network.retrofit.RetrofitHelper;
import com.alex.schwartzman.fivehundredpx.provider.DefaultContract;
import com.alex.schwartzman.fivehundredpx.provider.DefaultDatabaseHelper;
import com.alex.schwartzman.fivehundredpx.provider.DefaultUriMatcher;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

import retrofit.RestAdapter;

/**

 */
public class NetworkService extends BaseNetworkService {

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(FiveHundredPxApi.class, RetrofitHelper.createApi500pxRestAdapter(null).build());
    }

    protected RestAdapter.Builder createDefaultRestAdapterBuilder() {
        return RetrofitHelper.createApiGithubRestAdapter(null);
    }


    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        OrmLiteUriMatcher matcher = OrmLiteUriMatcher.getInstance(DefaultUriMatcher.class, DefaultContract.CONTENT_AUTHORITY);

        // init
        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, DefaultDatabaseHelper.DATABASE_NAME, DefaultDatabaseHelper.DATABASE_VERSION);
        @SuppressWarnings("unchecked") InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory( application, databaseHelper, matcher.getClassToNotificationUriMap() );
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }


}
