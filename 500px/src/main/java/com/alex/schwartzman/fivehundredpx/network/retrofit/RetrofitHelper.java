package com.alex.schwartzman.fivehundredpx.network.retrofit;

import com.alex.schwartzman.fivehundredpx.BuildConfig;
import com.alex.schwartzman.fivehundredpx.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

@SuppressWarnings("SameParameterValue")
public class RetrofitHelper {

    public static RestAdapter.Builder createApiGithubRestAdapter(RestAdapter.Builder builder) {
        return createBaseRestAdapter(builder)
                .setEndpoint(Constants.DEAFULT_API_BASE_URL);
    }

    public static RestAdapter.Builder createApi500pxRestAdapter(RestAdapter.Builder builder) {
        return createBaseRestAdapter(builder)
                .setEndpoint(Constants.PHOTO_API_BASE_URL);
    }

    private static RestAdapter.Builder createBaseRestAdapter(RestAdapter.Builder builder) {
        if (builder == null)
            builder = new RestAdapter.Builder();

        builder.setConverter(new JacksonConverter(new ObjectMapper()));

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        } else {
            builder.setLogLevel(RestAdapter.LogLevel.NONE);
        }

        return builder;
    }
}
