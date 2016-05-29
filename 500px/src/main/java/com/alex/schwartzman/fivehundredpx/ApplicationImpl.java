
package com.alex.schwartzman.fivehundredpx;

import com.blandware.android.atleap.BaseApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

public class ApplicationImpl extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
    }
}
