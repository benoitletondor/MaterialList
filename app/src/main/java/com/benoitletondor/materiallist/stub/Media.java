package com.benoitletondor.materiallist.stub;

import java.io.Serializable;
import java.util.Date;

/**
 * Object that contains data of a media
 *
 * @author Benoit LETONDOR
 */
public class Media implements Serializable
{
    /**
     * Id of the media
     */
    private final int mId;
    /**
     * Type of the media
     */
    private final MediaType mType;
    /**
     * Url of the media (source, in full size)
     */
    private final String mUrl;
    /**
     * Url of the media thumbnail
     */
    private final String mThumbnailUrl;
    /**
     * Title of the media
     */
    private final String mTitle;
    /**
     * Date the media was created
     */
    private final Date mDate;
    /**
     * Moderation status
     */
    private MediaModerationStatus mModerationStatus;
    /**
     * Name of the uploader
     */
    private String mUserName;

// ----------------------------------------->

    /**
     *
     * @param id id of the media
     * @param type Type of the media (shall not be null)
     * @param url Url of the media (source, in full size) (shall not be null)
     * @param thumbnailUrl Url of the media thumbnail (shall not be null)
     * @param title Title of the media (shall not be null)
     * @param moderationStatus moderation status (shall not be null)
     * @param date date the media was created
     * @param userName the name of the user who uploaded the media
     * @throws NullPointerException if {@code type}, {@code url}, {@code thumbnailUrl}, {@code title}, {@code date}, {@code userName} or {@code moderationStatus} is null
     */
    public Media(final int id,
                 final MediaType type,
                 final String url,
                 final String thumbnailUrl,
                 final String title,
                 final MediaModerationStatus moderationStatus,
                 final Date date,
                 final String userName) throws NullPointerException
    {
        if( type == null )
        {
            throw new NullPointerException("type==null");
        }

        if( url == null )
        {
            throw new NullPointerException("url==null");
        }

        if( thumbnailUrl == null )
        {
            throw new NullPointerException("thumbnailUrl==null");
        }

        if( title == null )
        {
            throw new NullPointerException("title==null");
        }

        if( moderationStatus == null )
        {
            throw new NullPointerException("moderationStatus==null");
        }

        if( date == null )
        {
            throw new NullPointerException("date==null");
        }

        if( userName == null )
        {
            throw new NullPointerException("userName==null");
        }

        mId = id;
        mType = type;
        mUrl = url;
        mThumbnailUrl = thumbnailUrl;
        mTitle = title;
        mModerationStatus = moderationStatus;
        mDate = date;
        mUserName = userName;
    }

// ----------------------------------------->

    public int getId()
    {
        return mId;
    }

    public MediaType getType()
    {
        return mType;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public String getThumbnailUrl()
    {
        return mThumbnailUrl;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public MediaModerationStatus getModerationStatus()
    {
        return mModerationStatus;
    }

    public Date getDate()
    {
        return mDate;
    }

    public String getUserName()
    {
        return mUserName;
    }

    void setModerationStatus(MediaModerationStatus status) throws NullPointerException
    {
        if( status == null )
        {
            throw new NullPointerException("status==null");
        }

        mModerationStatus = status;
    }

// ----------------------------------------->

    /**
     * Enum defining possible types of media
     */
    public enum MediaType
    {
        /**
         * Video media (mp4, mpeg)
         */
        VIDEO,
        /**
         * Image media (png, jpeg)
         */
        IMAGE
    }
}
