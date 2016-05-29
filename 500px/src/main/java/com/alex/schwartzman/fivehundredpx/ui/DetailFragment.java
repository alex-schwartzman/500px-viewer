package com.alex.schwartzman.fivehundredpx.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.schwartzman.fivehundredpx.R;
import com.alex.schwartzman.fivehundredpx.network.robospice.GetImagesListRequest;
import com.alex.schwartzman.fivehundredpx.provider.DefaultContract;
import com.blandware.android.atleap.loader.LoaderManagerCreator;
import com.blandware.android.atleap.loader.SimpleCursorRecyclerAdapterLoadable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.octo.android.robospice.persistence.DurationInMillis;

import org.roboguice.shaded.goole.common.base.Function;

import java.util.Map;
import java.util.TreeMap;

import roboguice.util.Ln;

public class DetailFragment extends BaseFragment {

    private int mItemId;
    private final Map<Integer, String> imagesVisited = new TreeMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.detail_content, container, false);
    }

    public void rewind(int position) {
        mItemId = position;
    }

    /////////////////
    ///HACK! This needs to be re-done, properly using the provider
    private final Function<Integer, Void> mPagingCallback = new Function<Integer, Void>() {
        public final int AVERAGE_COUNT_OF_IMAGES_PER_PAGE = 19;

        @Override
        public Void apply(Integer items) {
            final int page = items / AVERAGE_COUNT_OF_IMAGES_PER_PAGE + 1;
            executeSpiceRequest(new GetImagesListRequest(page), MainActivity.CACHE_KEY_PHOTO + page, DurationInMillis.ONE_MINUTE);
            return null;
        }
    };
////////////////

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RewindingCursorRecyclerAdapterLoadable adapter = new RewindingCursorRecyclerAdapterLoadable(
                getActivity(),
                DefaultContract.CONTENT_URI_PHOTOS,
                new String[]{DefaultContract.ImageColumns.WIDTH, DefaultContract.ImageColumns.HEIGHT, DefaultContract.ImageColumns._ID, DefaultContract.ImageColumns.URI, DefaultContract.ImageColumns.NAME, DefaultContract.ImageColumns.CAMERA, DefaultContract.ImageColumns.AUTHOR_NAME},
                null, //selection
                null, //selectionArgs
                DefaultContract.ImageColumns.RATING + " DESC, " + DefaultContract.ImageColumns._ID + " DESC", //in the "popular" category the photos are ordered by "rating desc, id desc"
                R.layout.photo_detail,
                new String[]{DefaultContract.ImageColumns.AUTHOR_NAME, DefaultContract.ImageColumns.NAME, DefaultContract.ImageColumns.CAMERA, DefaultContract.ImageColumns._ID, DefaultContract.ImageColumns.URI},
                new int[] {R.id.image_author, R.id.image_title, R.id.image_camera_model, R.id.image_id, R.id.image_frame}
        );
        new LoaderManagerCreator(this, adapter);

        final RecyclerViewPager recyclerView = (RecyclerViewPager) getView(); //yes, expect only pager in our layout

        //noinspection ConstantConditions because we want app to fail early
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false) );
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setViewBinder(new SimpleCursorRecyclerAdapterLoadable.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (view.getId() == R.id.image_frame) {
                    if (cursor.isLast()) {
                        if (mPagingCallback != null) {
                            try {
                                mPagingCallback.apply(cursor.getCount());
                            } catch (Exception e) {
                                Ln.e(e);
                            }
                        }
                    }

                    final String imageUrl = cursor.getString(i); //Could not use
                    Glide
                            .with(getContext().getApplicationContext())
                            .load(imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new PaletteAwareListener(view))
                            .into((ImageView) (view.findViewById(R.id.fullsize_image)));
                    imagesVisited.put(cursor.getPosition(), imageUrl);
                    return true;
                } else {
                    return false;
                }
            }

        });

        adapter.setOnLoadCompleteListener(new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
                recyclerView.scrollToPosition(mItemId);
            }
        });
    }

    public class PaletteAwareListener implements RequestListener<String, GlideDrawable>, Palette.PaletteAsyncListener{
        private final View rootView;

        PaletteAwareListener(View view){
            rootView = view;
        }

        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Palette.from(((GlideBitmapDrawable) resource).getBitmap()).generate(this);
            return false;
        }

        private void setPalette(View view, Palette palette) {
            int black = ContextCompat.getColor(getContext(), android.R.color.black);
            int white = ContextCompat.getColor(getContext(), android.R.color.white);
            int colorDarkMuted = palette.getDarkMutedColor(black);
            int colorLightMuted = palette.getLightMutedColor(white);
            int colorVibrant = palette.getVibrantColor(white);
            View fullSizeView = view.findViewById(R.id.fullsize_image);
            fullSizeView.setBackgroundColor(colorDarkMuted);
            TextView titleView = (TextView) view.findViewById(R.id.image_title);
            titleView.setTextColor(colorVibrant);
            TextView authorNameView = (TextView) view.findViewById(R.id.image_author);
            TextView cameraModelView = (TextView) view.findViewById(R.id.image_camera_model);
            cameraModelView.setTextColor(colorLightMuted);
            authorNameView.setTextColor(colorLightMuted);
        }

        @Override
        public void onGenerated(Palette palette) {
            setPalette(rootView, palette);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //we don't want to mess with onloadfinished after onPause
        RecyclerViewPager pager = (RecyclerViewPager) getView();
        if (pager != null) {
            RewindingCursorRecyclerAdapterLoadable adapter = (RewindingCursorRecyclerAdapterLoadable) (pager.getAdapter());
            if (adapter != null) {
                adapter.setOnLoadCompleteListener(null);
            }
        }
    }

    public String getShareableText() {
        RecyclerViewPager pager = (RecyclerViewPager) getView();
        if (pager != null) {
            String url = imagesVisited.get(pager.getCurrentPosition());
            if (url != null) {
                return url;
            }
        }
        return "https://www.google.es/search?q=lazy+cat&tbm=isch";
    }
}
