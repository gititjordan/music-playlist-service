package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeException;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the CreatePlaylistActivity for the MusicPlaylistService's CreatePlaylist API.
 *
 * This API allows the customer to create a new playlist with no songs.
 */
public class CreatePlaylistActivity implements RequestHandler<CreatePlaylistRequest, CreatePlaylistResult> {
    private final Logger log = LogManager.getLogger();
    private PlaylistDao playlistDao;


    public CreatePlaylistActivity() {

    }
    /**
     * Instantiates a new CreatePlaylistActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlists table.
     */
    public CreatePlaylistActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }



    /**
     * This method handles the incoming request by persisting a new playlist
     * with the provided playlist name and customer ID from the request.
     * <p>
     * It then returns the newly created playlist.
     * <p>
     * If the provided playlist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param createPlaylistRequest request object containing the playlist name and customer ID
     *                              associated with it
     * @return createPlaylistResult result object containing the API defined {@link PlaylistModel}
     */
    @Override
    public CreatePlaylistResult handleRequest(final CreatePlaylistRequest createPlaylistRequest, Context context) {
        log.info("Received CreatePlaylistRequest {}", createPlaylistRequest);
        Set<String> tags = new HashSet<>(createPlaylistRequest.getTags());
        tags.add(" ");

        try {
            MusicPlaylistServiceUtils.isValidString(createPlaylistRequest.getCustomerId());
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
            throw new InvalidAttributeValueException("Invalid ID entered." );

        }
        try {
            MusicPlaylistServiceUtils.isValidString(createPlaylistRequest.getName());
        } catch (InvalidAttributeException e) {
            throw new InvalidAttributeValueException("Invalid Playlist name entered." );

        }

        String playlistId = MusicPlaylistServiceUtils.generatePlaylistId();

        // populate playlist object
        Playlist playlistObject = new Playlist();
        playlistObject.setId(playlistId);
        playlistObject.setName(createPlaylistRequest.getName());
        playlistObject.setCustomerId(createPlaylistRequest.getCustomerId());
        //playlistObject.getSongCount();
        playlistObject.setTags(tags);

        // save playlist
        try {
            playlistDao.savePlaylist(playlistObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PlaylistModel playlistModel = new ModelConverter().toPlaylistModel(playlistObject);
        return CreatePlaylistResult.builder()
                .withPlaylist(playlistModel)
                .build();
    }
}
