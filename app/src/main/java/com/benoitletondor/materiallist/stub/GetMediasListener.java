package com.benoitletondor.materiallist.stub;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Listener for querying medias. Those callback will be called on a worker thread.
 *
 * @author Benoit LETONDOR
 */
public interface GetMediasListener
{
    /**
     * Called on success with the media list retrieved.
     *
     * @param medias list of medias
     */
    void onSuccess(@NonNull final List<Media> medias);

    /**
     * Called on error.
     *
     * @param e the encountered error
     */
    void onError(@NonNull final Exception e);
}
