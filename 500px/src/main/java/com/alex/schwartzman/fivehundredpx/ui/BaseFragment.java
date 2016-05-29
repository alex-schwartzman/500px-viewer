
package com.alex.schwartzman.fivehundredpx.ui;


import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.alex.schwartzman.fivehundredpx.network.robospice.DefaultSpiceManager;
import com.alex.schwartzman.fivehundredpx.network.robospice.NetworkService;
import com.blandware.android.atleap.util.ActivityHelper;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

@SuppressWarnings("SameParameterValue")
public abstract class BaseFragment<T> extends Fragment implements RequestListener<T> {

    private final DefaultSpiceManager spiceManager = new DefaultSpiceManager(NetworkService.class);

    @Override
    public void onStart() {
        super.onStart();
        spiceManager.start(getActivity());
        ActivityHelper.changeActionBarTitle(getActivity(), null);
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    private SpiceManager getSpiceManager() {
        return spiceManager;
    }

    void executeSpiceRequest(SpiceRequest<T> request, Object requestCacheKey, long cacheExpiryDuration) {
        getSpiceManager().execute(request, requestCacheKey, cacheExpiryDuration, this);
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        /*
                final ActionBarActivity actionBarActivity = (ActionBarActivity)getActivity();
                if (actionBarActivity != null) {
                    actionBarActivity.setSupportProgressBarIndeterminateVisibility(show);
                }
        */
        if (e instanceof NoNetworkException) {
            Toast.makeText(getActivity(), com.alex.schwartzman.fivehundredpx.R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestSuccess(Object o) {
    }

}
