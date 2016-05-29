package com.alex.schwartzman.fivehundredpx;

import android.os.Build;

import com.blandware.android.atleap.AppContext;

public class Constants {
    public static final String PHOTO_API_BASE_URL = AppContext.getContext().getString(R.string.photo_api_base_url);
    public static final String DEAFULT_API_BASE_URL = AppContext.getContext().getString(R.string.deafult_api_base_url);

    public static final String ACCOUNT_TYPE = "com.alex.schwartzman.fivehundredpx.user_account_type";
    public static final String ACCOUNT_TOKEN_TYPE = "ACCOUNT_TOKEN_TYPE";

    public static final boolean HAS_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
}
