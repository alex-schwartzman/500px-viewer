
package com.alex.schwartzman.fivehundredpx.network.robospice;

import com.octo.android.robospice.retrofit.RetrofitJackson2SpiceService;

import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;


public abstract class BaseNetworkService extends RetrofitJackson2SpiceService {

    private Map<Class<?>, RestAdapter> mRetrofitInterfaceToRestAdapter;
    private final Map<Class<?>, Object> mRetrofitInterfaceToServiceMap = new HashMap<>();

    private RestAdapter mDefaultRestAdapter;

    @Override
    protected String getServerUrl() {
        //because we create RestAdapter ourself
        return null;
    }

    /**
     * Create default restAdapter
     * @return rest adapter
     */
    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = createDefaultRestAdapterBuilder();
        mDefaultRestAdapter = builder.build();
        return builder;
    }

    protected abstract RestAdapter.Builder createDefaultRestAdapterBuilder();

    void addRetrofitInterface(Class<?> serviceClass, RestAdapter restAdapter) {
        if(mRetrofitInterfaceToRestAdapter == null)
            mRetrofitInterfaceToRestAdapter = new HashMap<>();
        mRetrofitInterfaceToRestAdapter.put(serviceClass, restAdapter);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getRetrofitService(Class<T> serviceClass) {
        T service = (T) mRetrofitInterfaceToServiceMap.get(serviceClass);
        if (service == null) {
            service = createRetrofitService(serviceClass);
            mRetrofitInterfaceToServiceMap.put(serviceClass, service);
        }
        return service;
    }

    private <T> T createRetrofitService(Class<T> serviceClass) {
        if (mRetrofitInterfaceToRestAdapter != null && mRetrofitInterfaceToRestAdapter.containsKey(serviceClass)) {
            RestAdapter restAdapter = mRetrofitInterfaceToRestAdapter.get(serviceClass);
            return restAdapter.create(serviceClass);
        } else {
            return mDefaultRestAdapter.create(serviceClass);
        }
    }
}
