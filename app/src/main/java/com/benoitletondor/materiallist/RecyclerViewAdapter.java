package com.benoitletondor.materiallist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.benoitletondor.materiallist.stub.Media;
import com.benoitletondor.materiallist.stub.MediaModerationStatus;
import com.benoitletondor.materiallist.stub.ModerateListener;
import com.benoitletondor.materiallist.stub.Webservices;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adapter for the list recycler view
 *
 * @author Benoit LETONDOR
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private final static String TAG = "RecyclerViewAdapter";

    @NonNull
    private List<MediaHolder> medias = new ArrayList<>(0);

    /**
     * Allows to remember the last item shown on screen
     */
    private int lastPosition = -1;

    /**
     * A marker that an holder can use to know if content has been replaced
     */
    private long currentMarker = System.nanoTime();

    /**
     * An handler to perform operations on the UI thread
     */
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * The recycler view attached to this adapter (note that the current implementation assume that's
     * the one from the {@link MainActivity}.
     */
    @Nullable
    private RecyclerView recyclerView;

    /**
     * The currently moderated media
     */
    @Nullable
    private MediaHolder currentlyModerated;

    @Inject
    Webservices webservices;

// ---------------------------------------->

    public RecyclerViewAdapter(@NonNull Activity activity)
    {
        ((App) activity.getApplication()).getComponent().inject(this);
    }

    public void setData(@NonNull List<Media> medias)
    {
        currentMarker = System.nanoTime();
        this.medias = MediaHolder.from(medias);
        notifyDataSetChanged();
    }

    public boolean isEmpty()
    {
        return medias.isEmpty();
    }

    /**
     * Called when the detail view returns to handle user selection
     *
     * @param result the activity result
     */
    public void onActivityResult(int result)
    {
        MediaHolder moderated = currentlyModerated;
        currentlyModerated = null;

        if (moderated != null && result != Activity.RESULT_CANCELED && recyclerView != null)
        {
            moderate(recyclerView.getContext(), moderated, result == DetailActivity.RESULT_APPROVED ? MediaModerationStatus.APPROVED : MediaModerationStatus.DENIED);
        }
    }

// ---------------------------------------->

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_cell, parent, false);
        return new RecyclerViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, final int position)
    {
        final MediaHolder mediaHolder = medias.get(position);

        if (mediaHolder.moderating)
        {
            holder.loaderView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.loaderView.setVisibility(View.GONE);
        }

        holder.previewImageView.setImageDrawable(null);

        // Load full size then resize for transition
        Picasso.with(holder.view.getContext())
            .load(mediaHolder.media.getThumbnailUrl())
            .fetch(new Callback()
            {
                @Override
                public void onSuccess()
                {
                    Picasso.with(holder.view.getContext())
                        .load(mediaHolder.media.getThumbnailUrl())
                        .resize(100, 100)
                        .centerInside()
                        .into(holder.previewImageView);
                }

                @Override
                public void onError()
                {

                }
            });

        holder.titleTextView.setText(mediaHolder.media.getTitle());

        holder.denyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moderate(holder.view.getContext(), mediaHolder, MediaModerationStatus.DENIED);
            }
        });

        holder.approveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moderate(holder.view.getContext(), mediaHolder, MediaModerationStatus.APPROVED);
            }
        });

        holder.container.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Activity activity = (Activity) holder.view.getContext(); // FIXME not the ideal way to get the activity

                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_MEDIA, mediaHolder.media);

                @SuppressWarnings("unchecked")
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.previewImageView, "thumb");

                currentlyModerated = mediaHolder;

                activity.startActivityForResult(intent, MainActivity.DETAIL_ACTIVITY_REQUEST_CODE, options.toBundle());
            }
        });

        setAnimation(holder.view, position);
    }

    @Override
    public int getItemCount()
    {
        return medias.size();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerViewAdapter.ViewHolder holder)
    {
        holder.view.clearAnimation();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        if( this.recyclerView == recyclerView )
        {
            this.recyclerView = null;
        }
    }

    /**
     * Animate appearance view of the recycler at the given position of the
     *
     * @param viewToAnimate the view to animate
     * @param position the position of the view
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    /**
     * Moderate the given media by calling the Webservice
     *
     * @param context non null context
     * @param mediaHolder holder of the media
     * @param status the new moderation status
     */
    private void moderate(@NonNull final Context context, @NonNull final MediaHolder mediaHolder, @NonNull final MediaModerationStatus status)
    {
        final long marker = currentMarker;

        try
        {
            webservices.moderate(mediaHolder.media, status, new ModerateListener()
            {
                @Override
                public void onSucceed(@NonNull Media media, @NonNull MediaModerationStatus moderationStatus)
                {
                    mediaHolder.moderating = false;

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (marker == currentMarker && recyclerView != null)
                            {
                                final int index = medias.indexOf(mediaHolder);
                                medias.remove(index);
                                notifyItemRemoved(index);

                                final Snackbar snackbar = Snackbar.make(recyclerView,
                                    context.getString(R.string.moderated_snackbar_text, status == MediaModerationStatus.APPROVED ? context.getString(R.string.moderation_approved) : context.getString(R.string.moderation_denied)),
                                    Snackbar.LENGTH_LONG);

                                snackbar.setAction(android.R.string.cancel, new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        if (marker == currentMarker)
                                        {
                                            unmoderate(context, mediaHolder, index);
                                        }

                                        snackbar.dismiss();
                                    }
                                });

                                snackbar.show();
                            }
                        }
                    });
                }

                @Override
                public void onError(@NonNull Media media, final @NonNull Exception e)
                {
                    mediaHolder.moderating = false;

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (marker == currentMarker)
                            {
                                notifyItemChanged(medias.indexOf(mediaHolder));

                                Log.e(TAG, "Error while building update WS", e);
                                showError(context, e);
                            }
                        }
                    });
                }
            });

            mediaHolder.moderating = true;
            notifyItemChanged(medias.indexOf(mediaHolder));
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error while building update WS", e);

            showError(context, e);
        }
    }

    /**
     * Cancel moderation of a media by calling the webservices and update its status
     *
     * @param context non null context
     * @param mediaHolder the media to cancel moderation to
     * @param index the original index of the media
     */
    private void unmoderate(@NonNull final Context context, @NonNull final MediaHolder mediaHolder, final int index)
    {
        final long marker = currentMarker;

        medias.add(index, mediaHolder);
        notifyItemInserted(index);

        webservices.moderate(mediaHolder.media, MediaModerationStatus.UNMODERATED, new ModerateListener()
        {
            @Override
            public void onSucceed(@NonNull Media media, @NonNull MediaModerationStatus moderationStatus)
            {

            }

            @Override
            public void onError(@NonNull Media media, final @NonNull Exception e)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (marker == currentMarker)
                        {
                            showError(context, e);

                            medias.remove(index);
                            notifyItemRemoved(index);
                        }
                    }
                });
            }
        });
    }

    /**
     * Show an error to the user
     *
     * @param context non null context (should be an activity)
     * @param e the encountered error
     */
    private void showError(@NonNull Context context, @NonNull Exception e)
    {
        final String error;
        if (e instanceof IOException || (e.getCause() != null && (e.getCause() instanceof IOException)))
        {
            error = context.getString(R.string.login_error_connection_message);
        }
        else
        {
            error = e.getLocalizedMessage();
        }

        new AlertDialog.Builder(context)
            .setTitle(R.string.error_dialog_title)
            .setMessage(context.getString(R.string.error_dialog_message, error))
            .setPositiveButton(android.R.string.ok, null)
            .show();
    }

    /**
     * Helper that uses the {@link #mainHandler} to run operation on the UI thread
     *
     * @param runnable non null runnable
     */
    private void runOnUiThread(@NonNull Runnable runnable)
    {
        mainHandler.post(runnable);
    }

    /**
     * Holder for media with its current state of moderation
     */
    public static class MediaHolder
    {
        @NonNull
        public Media media;
        public boolean moderating = false;

        public MediaHolder(@NonNull Media media)
        {
            this.media = media;
        }

        /**
         * Create a list of media holder from a list of media
         *
         * @param medias non null list of media
         * @return the medias in their holders
         */
        public static List<MediaHolder> from(@NonNull List<Media> medias)
        {
            List<MediaHolder> holders = new ArrayList<>(medias.size());

            for(Media media : medias)
            {
                holders.add(new MediaHolder(media));
            }

            return holders;
        }
    }

    /**
     * View holder for the recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public View view;

        @Bind(R.id.recycler_view_cell_image_view)
        public ImageView previewImageView;

        @Bind(R.id.recycler_view_cell_title_tv)
        public TextView titleTextView;

        @Bind(R.id.recycler_view_cell_deny_button)
        public View denyButton;

        @Bind(R.id.recycler_view_cell_approve_button)
        public View approveButton;

        @Bind(R.id.recycler_view_container)
        public View container;

        @Bind(R.id.recycler_view_loader)
        public View loaderView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
