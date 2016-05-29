
package com.alex.schwartzman.fivehundredpx.provider;

import com.alex.schwartzman.fivehundredpx.model.Author;
import com.alex.schwartzman.fivehundredpx.model.ImageInfo;
import com.alex.schwartzman.fivehundredpx.model.PageWithPhotos;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;

public class DefaultUriMatcher extends OrmLiteUriMatcher {
    public DefaultUriMatcher(String authority) {
        super(authority);
    }

    @Override
    public void instantiate() {
        addClass(PageWithPhotos.class, DefaultContract.PATH_PHOTOS);
        addClass(DefaultContract.PATH_PHOTO, ImageInfo.class);
        addClass(DefaultContract.PATH_AUTHOR, Author.class);
        addClass(DefaultContract.PATH_PHOTOS, ImageInfo.class);
    }
}
