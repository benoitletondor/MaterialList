package com.benoitletondor.materiallist.stub;

import android.support.annotation.NonNull;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Stub webservices that will return fake content simulating load times
 *
 * @author Benoit LETONDOR
 */
public class Webservices
{
    /**
     * Executor to run webservices on a background thread
     */
    public static ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Is the user logged
     */
    private boolean logged = false;

// ------------------------------------------>

    /**
     * Stub method that simulate login. Will be successful is login & pass are not empty
     *
     * @param login the login
     * @param pass the pass
     * @param listener listener
     */
    public void login(@NonNull final String login, @NonNull final String pass, @NonNull final LoginListener listener)
    {
        if( isLogged() )
        {
            listener.onSucceed();
            return;
        }

        executor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ignored) {}

                if( !login.trim().isEmpty() && !pass.trim().isEmpty() )
                {
                    logged = true;
                    listener.onSucceed();
                }
                else
                {
                    listener.onError(new JSONException("Invalid login or password"));
                }
            }
        });
    }

    /**
     * Check if the user is logged
     *
     * @return true if the user is logged in, false otherwise
     */
    public boolean isLogged()
    {
        return logged;
    }

    /**
     * Stub method that simulate media querying
     *
     * @param listener listener
     */
    public void getMedias(@NonNull final GetMediasListener listener)
    {
        if( !isLogged() )
        {
            listener.onError(new Exception("User not logged in"));
            return;
        }

        executor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException ignored) {}

                final List<Media> medias = new ArrayList<>(10);

                medias.add(StubMedias.media1());
                medias.add(StubMedias.media2());
                medias.add(StubMedias.media3());
                medias.add(StubMedias.media4());
                medias.add(StubMedias.media5());
                medias.add(StubMedias.media6());
                medias.add(StubMedias.media7());
                medias.add(StubMedias.media8());
                medias.add(StubMedias.media9());
                medias.add(StubMedias.media10());

                listener.onSuccess(medias);
            }
        });
    }

    /**
     * Stub method that simulates media moderation
     *
     * @param media the media to moderate
     * @param status the status to apply
     * @param listener listener
     */
    public void moderate(@NonNull final Media media, @NonNull final MediaModerationStatus status, @NonNull final ModerateListener listener)
    {
        if( !isLogged() )
        {
            listener.onError(media, new Exception("User not logged in"));
            return;
        }

        executor.submit(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ignored) {}

                listener.onSucceed(media, status);
            }
        });
    }
}
