package com.alex.schwartzman.fivehundredpx.auth;

import android.accounts.Account;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.alex.schwartzman.fivehundredpx.model.User;
import com.blandware.android.atleap.auth.BaseAuthActivity;

@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class AuthActivity extends BaseAuthActivity {

    private static final String TAG = AuthActivity.class.getSimpleName();

    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    public static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String KEY_USER_COMPANY = "KEY_USER_COMPANY";
    public static final String KEY_USER_AVATAR_URL = "KEY_USER_AVATAR_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);

        setContentView(com.alex.schwartzman.fivehundredpx.R.layout.activity_auth);

        Log.v(TAG, "Staring authorization page");
    }

    @Override
    public void onBackPressed() {
        //do not allow to go back
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = new User();
        user.email="user@user.user";
        user.name="anonymous 500px access";
        user.id=1;
        user.company="friends";
        user.avatarUrl="";
        user.login=user.name;
        finishAuth(user, "token");
        finish();
    }

    private void finishAuth(User user, String authToken) {
        Log.v(TAG, "Creating user account");
        Bundle options = new Bundle();
        options.putString(KEY_USER_EMAIL, user.email);
        options.putString(KEY_USER_NAME, user.name);
        options.putString(KEY_USER_ID, String.valueOf(user.id));
        options.putString(KEY_USER_COMPANY, user.company);
        options.putString(KEY_USER_AVATAR_URL, user.avatarUrl);

        Account account = updateOrCreateAccount(user.login, null, authToken, options);
        if (account != null) {
            sendSuccessResult(account, authToken);
        }
        Log.v(TAG, "User account created successfully");
    }
}
