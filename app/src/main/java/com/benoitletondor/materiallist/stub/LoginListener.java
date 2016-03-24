package com.benoitletondor.materiallist.stub;

import android.support.annotation.NonNull;

/**
 * Listener for login events. Those callback will be called on a worker thread.
 *
 * @author Benoit LETONDOR
 */
public interface LoginListener
{
    /**
     * Called when logging is successful.
     */
    void onSucceed();

    /**
     * Called on error while logging.
     *
     * @param e the encountered error
     */
    void onError(@NonNull final Exception e);
}
