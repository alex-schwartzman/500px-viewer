
package com.alex.schwartzman.fivehundredpx;

import com.blandware.android.atleap.BaseApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import roboguice.RoboGuice;

public class ApplicationImpl extends BaseApplication {
    static {
        //Even if mentioned in proguard config, annotation database somehow manages to escape and get "optimized"
        //To prevent it, let's use this:
        RoboGuice.setUseAnnotationDatabases(false);
        //We're not alone: https://github.com/roboguice/roboguice/issues/246
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
    }
}
