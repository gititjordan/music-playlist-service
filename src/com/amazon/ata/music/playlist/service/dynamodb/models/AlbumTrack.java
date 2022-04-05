package com.amazon.ata.music.playlist.service.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Represents a record in the album_tracks table.
 */
@DynamoDBTable(tableName = "album_tracks")
public class AlbumTrack {
    private String asin;
    private String album;
    private int trackNumber;
    private String title;

    @DynamoDBHashKey(attributeName = "asin")
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @DynamoDBRangeKey(attributeName = "track_number")
    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int track_number) {
        this.trackNumber = trackNumber;
    }

    @DynamoDBAttribute(attributeName = "album_name")
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album_name) {
        this.album = album;
    }

    @DynamoDBAttribute(attributeName = "song_title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String song_title) {
        this.title = title;
    }
}
