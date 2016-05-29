package com.alex.schwartzman.fivehundredpx.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.alex.schwartzman.fivehundredpx.R;

import roboguice.activity.CustomBaseActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_detail)
public class DetailActivity extends CustomBaseActivity {

    @InjectFragment(R.id.pager)
    private
    DetailFragment detailFragment;

    @InjectView(R.id.star)
    private
    FloatingActionButton star;

    @Override
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        int position = getIntent().getIntExtra(Intents.EXTRA_POSITION, 0);
        detailFragment.rewind(position);
        star.setEnabled(true);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, detailFragment.getShareableText());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
}
