package com.alex.schwartzman.fivehundredpx.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.support.v4.content.Loader;

import com.blandware.android.atleap.loader.SimpleCursorRecyclerAdapterLoadable;
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator;

import java.util.concurrent.Callable;

import roboguice.util.Ln;

class GreedoCursorRecyclerAdapterLoadable extends SimpleCursorRecyclerAdapterLoadable implements GreedoLayoutSizeCalculator.SizeCalculatorDelegate {

    public GreedoCursorRecyclerAdapterLoadable(Context context, Uri uri, int layoutId) {
        super(context, uri, layoutId);
    }

    public GreedoCursorRecyclerAdapterLoadable(Context context, Uri uri, int layoutId, String[] fromFieldNames, int[] toLayoutViewIds) {
        super(context, uri, layoutId, fromFieldNames, toLayoutViewIds);
    }

    public GreedoCursorRecyclerAdapterLoadable(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int layoutId, String[] fromFieldNames, int[] toLayoutViewIds) {
        super(context, uri, projection, selection, selectionArgs, sortOrder, layoutId, fromFieldNames, toLayoutViewIds);
    }

    public GreedoCursorRecyclerAdapterLoadable(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int layoutId, Cursor cursor, String[] fromFieldNames, int[] toLayoutViewIds, int flags) {
        super(context, uri, projection, selection, selectionArgs, sortOrder, layoutId, cursor, fromFieldNames, toLayoutViewIds, flags);
    }

    @Override
    public double aspectRatioForIndex(int i) {
        if (i >= getItemCount()) {
            return 1;
        }
        Object item = getItem(i);
        if (item instanceof CursorWrapper) {
            CursorWrapper wrapper = (CursorWrapper) item;
            return getAspectRatio(wrapper);
        } else {
            return 1;
        }
    }

    private double getAspectRatio(CursorWrapper wrapper) {
        if (wrapper == null || wrapper.getColumnCount() < 3) {
            return 1;
        }
        int width = wrapper.getInt(0);
        int height = wrapper.getInt(1);
        if (width == 0 || height == 0) {
            throw new UnsupportedOperationException("First two columns of the query are expected to be WIDTH and HEIHGT");
        }
        return (double) width / (double) height;
    }

    private Callable needMoreDataListener;

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        if (needMoreDataListener != null && data.getCount() == 0) {
            try {
                needMoreDataListener.call();
            } catch (Exception e) {
                Ln.e(e);
            }
        }
    }

    public void setNeedMoreDataListener(Callable needMoreDataListener) {
        this.needMoreDataListener = needMoreDataListener;
    }
}
