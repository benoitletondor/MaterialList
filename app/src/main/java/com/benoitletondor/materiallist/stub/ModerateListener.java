package com.benoitletondor.materiallist.stub;

import android.support.annotation.NonNull;

/**
 * Created by benoit on 24/03/16.
 */
public interface ModerateListener
{
    /**
     * Called when moderation is successful.
     */
    void onSucceed(@NonNull Media media, @NonNull MediaModerationStatus moderationStatus);

    /**
     * Called on error while moderating.
     *
     * @param e the encountered error
     */
    void onError(@NonNull Media media, @NonNull final Exception e);
}
