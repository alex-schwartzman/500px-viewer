package com.alex.schwartzman.fivehundredpx.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DefaultContract {

    private DefaultContract() {}

    public static final String CONTENT_AUTHORITY = "com.alex.schwartzman.fivehundredpx.authority";

    private static final Uri BASE_CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(CONTENT_AUTHORITY).build();

    public static final String PATH_PHOTOS = "photos";
    public static final String PATH_PAGES = "pages";
    public static final String PATH_PHOTO = "photo/*";
    public static final String PATH_AUTHOR = "author/*";
    private static final String PATH_PHOTO_MATCH = "photo";

    public static class ImageColumns implements BaseColumns {
        public static Uri getImageInfoUri(int id) {
            return BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTO_MATCH).appendPath(Integer.toString(id)).build();
        }
        public static final String TABLE = "photo";
        public static final String NAME = "name";
        public static final String CAMERA = "camera";
        public static final String URI = "image_url";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String AUTHOR_NAME = "author_name";
        public static final String RATING = "rating";
    }

    public static class PageColumns implements BaseColumns {
        public static final String TABLE = "page";
        public static final String TOTAL_PAGES = "total_pages";
    }

    public static class AuthorColumns implements BaseColumns {

        public static final String TABLE = "author";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
    }


    public static final Uri CONTENT_URI_PHOTOS =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHOTOS).build();

    public static final Uri CONTENT_URI_PAGES =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_PAGES).build();

}
