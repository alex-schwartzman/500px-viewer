package com.alex.schwartzman.fivehundredpx.network.robospice;

import android.util.Log;

import com.alex.schwartzman.fivehundredpx.BuildConfig;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

import roboguice.util.temp.Ln;

public class DefaultSpiceManager extends SpiceManager {
    public DefaultSpiceManager(Class<? extends SpiceService> spiceServiceClass) {
        super(spiceServiceClass);
        if(BuildConfig.DEBUG) {
            Ln.getConfig().setLoggingLevel(Log.VERBOSE);
        } else {
            Ln.getConfig().setLoggingLevel(Log.ERROR);
        }
    }
}
