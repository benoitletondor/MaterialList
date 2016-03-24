package com.benoitletondor.materiallist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.benoitletondor.materiallist.stub.GetMediasListener;
import com.benoitletondor.materiallist.stub.Media;
import com.benoitletondor.materiallist.stub.MediaModerationStatus;
import com.benoitletondor.materiallist.stub.Webservices;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Main activity containing the list
 *
 * @author Benoit LETONDOR
 */
public class MainActivity extends BaseActivity
{
    private final static String TAG = "MainActivity";
    public final static int LOGIN_ACTIVITY_REQUEST_CODE = 1;
    public final static int DETAIL_ACTIVITY_REQUEST_CODE = 2;

    @Inject
    Webservices webservices;

    @Bind(R.id.main_activity_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.no_media_placeholder)
    View noMediaPlaceholder;

    @Bind(R.id.loading_progressbar)
    ProgressBar loadingProgressBar;

    RecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    MenuItem reloadMenuItem;

    volatile MediaLoadingState state = MediaLoadingState.NOT_LOADED;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getAppComponent().inject(this);
        if( !webservices.isLogged() )
        {
            startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_ACTIVITY_REQUEST_CODE);
        }

        super.onCreate(savedInstanceState);
        setContentViewAndBind(this, R.layout.activity_main);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "webservices: " + webservices.toString());

        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if( state == MediaLoadingState.NOT_LOADED && webservices.isLogged() )
        {
            loadMedias();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == LOGIN_ACTIVITY_REQUEST_CODE && resultCode != RESULT_OK )
        {
            finish();
        }
        else if( requestCode == DETAIL_ACTIVITY_REQUEST_CODE )
        {
            recyclerViewAdapter.onActivityResult(resultCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        reloadMenuItem = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh)
        {
            if( state != MediaLoadingState.LOADING )
            {
                loadMedias();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

// -------------------------------------------->

    /**
     * Load medias by calling the WS and updating UI
     */
    private void loadMedias()
    {
        state = MediaLoadingState.LOADING;
        showLoading(true);
        showReloadButton(false);

        webservices.getMedias(new GetMediasListener()
        {
            @Override
            public void onSuccess(@NonNull final List<Media> medias)
            {
                state = MediaLoadingState.LOADED;

                // Filter only not moderated
                Iterator<Media> iterator = medias.iterator();
                while (iterator.hasNext())
                {
                    Media media = iterator.next();
                    if (media.getModerationStatus() != MediaModerationStatus.UNMODERATED)
                    {
                        iterator.remove();
                    }
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        recyclerViewAdapter.setData(medias);
                        showPlaceholder(recyclerViewAdapter.isEmpty());
                        showLoading(false);
                        showReloadButton(true);
                    }
                });
            }

            @Override
            public void onError(@NonNull final Exception e)
            {
                state = MediaLoadingState.NOT_LOADED;

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        final String error;
                        if (e instanceof IOException || (e.getCause() != null && (e.getCause() instanceof IOException)))
                        {
                            error = getString(R.string.login_error_connection_message);
                        }
                        else
                        {
                            error = e.getLocalizedMessage();
                        }

                        showErrorDialog(error);
                        showPlaceholder(recyclerViewAdapter.isEmpty());
                        showLoading(false);
                        showReloadButton(true);
                    }
                });
            }
        });
    }

    /**
     * Show or not the loading status on the ActionBar
     *
     * @param show shall the loading be shown or not
     */
    private void showLoading(boolean show)
    {
        loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Show or not the placeholder for empty list
     *
     * @param show shall the placeholder be shown
     */
    private void showPlaceholder(boolean show)
    {
        if( show )
        {
            noMediaPlaceholder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            noMediaPlaceholder.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show or hide the reload button on the action bar
     *
     * @param show should reload button be shown
     */
    private void showReloadButton(boolean show)
    {
        if( reloadMenuItem != null )
        {
            reloadMenuItem.setVisible(show);
        }
    }

    /**
     * Enum of different states of media loading
     */
    private enum MediaLoadingState
    {
        NOT_LOADED,

        LOADING,

        LOADED
    }
}
