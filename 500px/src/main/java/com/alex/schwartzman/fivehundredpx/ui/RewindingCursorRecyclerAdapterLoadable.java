package com.alex.schwartzman.fivehundredpx.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader;

import com.blandware.android.atleap.loader.SimpleCursorRecyclerAdapterLoadable;

class RewindingCursorRecyclerAdapterLoadable extends SimpleCursorRecyclerAdapterLoadable {
    private Loader.OnLoadCompleteListener<Cursor> onLoadCompleteListener;

    public RewindingCursorRecyclerAdapterLoadable(Context context, Uri uri, int layoutId) {
        super(context, uri, layoutId);
    }

    public RewindingCursorRecyclerAdapterLoadable(Context context, Uri uri, int layoutId, String[] fromFieldNames, int[] toLayoutViewIds) {
        super(context, uri, layoutId, fromFieldNames, toLayoutViewIds);
    }

    public RewindingCursorRecyclerAdapterLoadable(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int layoutId, String[] fromFieldNames, int[] toLayoutViewIds) {
        super(context, uri, projection, selection, selectionArgs, sortOrder, layoutId, fromFieldNames, toLayoutViewIds);
    }

    public RewindingCursorRecyclerAdapterLoadable(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int layoutId, Cursor cursor, String[] fromFieldNames, int[] toLayoutViewIds, int flags) {
        super(context, uri, projection, selection, selectionArgs, sortOrder, layoutId, cursor, fromFieldNames, toLayoutViewIds, flags);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        if (onLoadCompleteListener != null) {
            onLoadCompleteListener.onLoadComplete(loader, data);
            onLoadCompleteListener = null;
        }
    }

    public void setOnLoadCompleteListener(Loader.OnLoadCompleteListener<Cursor> onLoadCompleteListener) {
        this.onLoadCompleteListener = onLoadCompleteListener;
    }
}
