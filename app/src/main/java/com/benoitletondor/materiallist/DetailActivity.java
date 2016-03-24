package com.benoitletondor.materiallist;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.benoitletondor.materiallist.stub.Media;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity containing details of the activity
 *
 * @author Benoit LETONDOR
 */
public class DetailActivity extends BaseActivity
{
    public final static String EXTRA_MEDIA = "extra_media";
    public final static int RESULT_APPROVED = 10;
    public final static int RESULT_DENIED = 11;

    private Media media;

    @Bind(R.id.detail_media_thumbnail)
    ImageView mediaImageView;

    @Bind(R.id.date_textview)
    TextView dateTextView;

    @Bind(R.id.author_textview)
    TextView authorTextView;

    @Bind(R.id.deny_button)
    FloatingActionButton denyButton;

    @Bind(R.id.approve_button)
    FloatingActionButton approveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewAndBind(this, R.layout.activity_detail);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        media = (Media) getIntent().getSerializableExtra(EXTRA_MEDIA);
        setTitle(media.getTitle());

        Picasso.with(this)
            .load(media.getThumbnailUrl())
            .into(mediaImageView);

        dateTextView.setText(getString(R.string.media_date, new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm", Locale.getDefault()).format(media.getDate())));
        authorTextView.setText(getString(R.string.media_creator, media.getUserName()));

        setResult(RESULT_CANCELED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            final Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
            sharedElementEnterTransition.addListener(new Transition.TransitionListener()
            {
                @Override
                public void onTransitionStart(Transition transition)
                {}

                @Override
                public void onTransitionEnd(Transition transition)
                {
                    animateEnter();
                }

                @Override
                public void onTransitionCancel(Transition transition)
                {}

                @Override
                public void onTransitionPause(Transition transition)
                {}

                @Override
                public void onTransitionResume(Transition transition)
                {}
            });
        }
        else
        {
            approveButton.post(new Runnable()
            {
                @Override
                public void run()
                {
                    animateEnter();
                }
            });
        }
    }

    private void animateEnter()
    {
        animateFAB(true, denyButton);
        animateFAB(true, approveButton);
    }

    private void animateExit()
    {
        animateFAB(false, denyButton);
        animateFAB(false, approveButton);
    }

    private void animateFAB(boolean start, View fab)
    {
        ViewCompat.animate(fab)
            .scaleX(start ? 1.0f : 0.0f)
            .scaleY(start ? 1.0f : 0.0f)
            .alpha(start ? 1.0f : 0.0f)
            .setDuration(start ? 300 : 100)
            .setInterpolator(new AnticipateOvershootInterpolator())
            .withLayer()
            .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if( id == android.R.id.home ) // Back button of the actionbar
        {
            supportFinishAfterTransition();
            return true;
        }
        else if( id == R.id.action_download )
        {
            Intent httpIntent = new Intent(Intent.ACTION_VIEW);
            httpIntent.setData(Uri.parse(media.getUrl()));

            startActivity(httpIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.deny_button)
    public void onDenyButtonClicked()
    {
        setResult(RESULT_DENIED);
        finish();
    }

    @OnClick(R.id.approve_button)
    public void onApproveButtonClicked()
    {
        setResult(RESULT_APPROVED);
        finish();
    }

    @Override
    public void supportFinishAfterTransition()
    {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP )
        {
            animateExit();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    DetailActivity.super.supportFinishAfterTransition();
                }
            }, 50);
        }
        else
        {
            super.supportFinishAfterTransition();
        }
    }
}
