package com.alex.schwartzman.fivehundredpx.ui;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alex.schwartzman.fivehundredpx.Constants;
import com.alex.schwartzman.fivehundredpx.R;
import com.alex.schwartzman.fivehundredpx.model.PageWithPhotos;
import com.alex.schwartzman.fivehundredpx.network.robospice.DefaultSpiceManager;
import com.alex.schwartzman.fivehundredpx.network.robospice.GetImagesListRequest;
import com.alex.schwartzman.fivehundredpx.network.robospice.NetworkService;
import com.alex.schwartzman.fivehundredpx.provider.DefaultContract;
import com.blandware.android.atleap.loader.LoaderManagerCreator;
import com.blandware.android.atleap.loader.SimpleCursorRecyclerAdapter;
import com.blandware.android.atleap.loader.SimpleCursorRecyclerAdapterLoadable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fivehundredpx.greedolayout.GreedoLayoutManager;
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.roboguice.shaded.goole.common.base.Function;

import java.util.concurrent.Callable;

import roboguice.activity.CustomBaseActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_main)
public class MainActivity extends CustomBaseActivity implements RequestListener {

    private final DefaultSpiceManager spiceManager = new DefaultSpiceManager(NetworkService.class);

    private static final String CACHE_KEY_PHOTO = "photo_cache_key";

    @InjectView(R.id.toolbar)
    private
    AutoHideToolbar toolbar;

    @InjectView(R.id.swipe_refresh)
    private
    SwipeRefreshLayout swipeRefreshLayout;

    @InjectView(R.id.recycler)
    private
    RecyclerView recyclerView;

    @Override
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        setSupportActionBar(toolbar);
        initSwipeRefresh();
        GreedoCursorRecyclerAdapterLoadable adapter = new GreedoCursorRecyclerAdapterLoadable(
                this,
                DefaultContract.CONTENT_URI_PHOTOS,
                new String[]{DefaultContract.ImageColumns.WIDTH, DefaultContract.ImageColumns.HEIGHT, DefaultContract.ImageColumns._ID, DefaultContract.ImageColumns.URI, DefaultContract.ImageColumns.NAME, DefaultContract.ImageColumns.CAMERA},
                null, //selection
                null, //selectionArgs
                DefaultContract.ImageColumns.RATING + " DESC, " + DefaultContract.ImageColumns._ID + " DESC", //in the "popular" category the photos are ordered by "rating desc, id desc"
                R.layout.photo_thumbnail_item,
                new String[]{DefaultContract.ImageColumns.URI},
                new int[]{R.id.item_icon}
        );
        new LoaderManagerCreator(this, adapter);

        configureRecyclerView(adapter, recyclerView);
        configureAdapter(adapter);

        adapter.setNeedMoreDataListener(new Callable() {
            @Override
            public Object call() throws Exception {
                return mPagingCallback.apply(0);
            }
        });
    }

    private void configureRecyclerView(GreedoCursorRecyclerAdapterLoadable adapter, RecyclerView recyclerView) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int maxRowHeight = metrics.heightPixels / 3;
        GreedoLayoutManager layoutManager = new GreedoLayoutManager(adapter);
        layoutManager.setMaxRowHeight(maxRowHeight);

        int spacing = MeasUtils.dpToPx(4, getApplicationContext());
        recyclerView.addItemDecoration(new GreedoSpacingItemDecoration(spacing));

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(myScrollListener);
    }

    /////////////////
    ///HACK! This needs to be re-done, properly using the provider
    private final Function<Integer, Void> mPagingCallback = new Function<Integer, Void>() {
        public final int AVERAGE_COUNT_OF_IMAGES_PER_PAGE = 19;

        @Override
        public Void apply(Integer items) {
            final int page = items / AVERAGE_COUNT_OF_IMAGES_PER_PAGE + 1;
            executeSpiceRequest(new GetImagesListRequest(page), CACHE_KEY_PHOTO + page, DurationInMillis.ONE_MINUTE);
            return null;
        }
    };
////////////////

    private final SimpleCursorRecyclerAdapter.ViewBinder myViewBinder = new SimpleCursorRecyclerAdapterLoadable.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int i) {
            if (cursor.isLast()) {
                if (mPagingCallback != null) {
                    try {
                        mPagingCallback.apply(cursor.getCount());
                    } catch (Exception e) {
                        Ln.e(e);
                    }
                }
            }

            if (view.getId() == R.id.item_icon) {
                ImageView imageView = (ImageView) view;
                String thumbnailUri = cursor.getString(i);
                Glide.with(getApplicationContext()).load(thumbnailUri).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
                return true;
            } else {
                return false;
            }
        }
    };

    private void configureAdapter(SimpleCursorRecyclerAdapter adapter) {
        adapter.setViewBinder(myViewBinder);
        adapter.setOnItemClickListener(myAddapterClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Toast.makeText(MainActivity.this, "Not implemented (as per requirements for v0.1)", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "TODO: refresh", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        spiceManager.shouldStop();
    }

    private SpiceManager getSpiceManager() {
        return spiceManager;
    }

    private void executeSpiceRequest(SpiceRequest<PageWithPhotos> request, Object requestCacheKey, long cacheExpiryDuration) {
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
            Toast.makeText(this, com.alex.schwartzman.fivehundredpx.R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestSuccess(Object o) {
    }

    private final SimpleCursorRecyclerAdapter.OnItemClickListener myAddapterClickListener = new SimpleCursorRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(SimpleCursorRecyclerAdapter adapter, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class)
                    .putExtra(Intents.EXTRA_POSITION, position);
            Bundle options = null;
            if (Constants.HAS_LOLLIPOP && view instanceof ImageView) {
                options = createTransitionOptions(view);
            }
            ActivityCompat.startActivity(MainActivity.this, intent, options);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private Bundle createTransitionOptions(View view) {
            Drawable drawable = ((ImageView)view).getDrawable();
            if (drawable != null) {
                return ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view,
                        MainActivity.this.getString(R.string.transition_text)).toBundle();
            }
            return null;
        }
    };

    private final RecyclerView.OnScrollListener myScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (RecyclerView.SCROLL_STATE_DRAGGING != recyclerView.getScrollState()) {
                if (dy > 0 && toolbar.visible()) {
                    toolbar.hide();
                } else if (dy < 0 && !toolbar.visible()) {
                    toolbar.show();
                }
            }
        }
    };
}
