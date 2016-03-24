package com.benoitletondor.materiallist.stub;

/**
 * Enum that represents the moderation state of a Media.
 *
 * @author Benoit LETONDOR
 */
public enum MediaModerationStatus
{
    /**
     * The media is not yet moderated
     */
    UNMODERATED(0),

    /**
     * The media has been approved
     */
    APPROVED(1),

    /**
     * The media has been denied
     */
    DENIED(2);

// ---------------------------------------->

    private final int value;

    MediaModerationStatus(final int value)
    {
        this.value = value;
    }

    /**
     * Get the value associated with this status. You shouldn't call this method unless you know
     * what you are doing
     *
     * @return the value associated with this status
     */
    public int getValue()
    {
        return value;
    }

// ---------------------------------------->

    /**
     * Get the enum for the given value, returns null if not found.
     *
     * @param value the webservice value
     * @return ModerationStatus if found, null otherwise
     */
    public static MediaModerationStatus fromValue(int value)
    {
        for( MediaModerationStatus status : values() )
        {
            if( value == status.value )
            {
                return status;
            }
        }

        return null;
    }
}
