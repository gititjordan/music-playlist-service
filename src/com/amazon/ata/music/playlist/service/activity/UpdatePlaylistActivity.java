package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeChangeException;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.models.requests.UpdatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.UpdatePlaylistResult;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Implementation of the UpdatePlaylistActivity for the MusicPlaylistService's UpdatePlaylist API.
 *
 * This API allows the customer to update their saved playlist's information.
 */
public class UpdatePlaylistActivity implements RequestHandler<UpdatePlaylistRequest, UpdatePlaylistResult> {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

    /**
     * Instantiates a new UpdatePlaylistActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlist table.
     */
    @Inject
    public UpdatePlaylistActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }


    /**
     * This method handles the incoming request by retrieving the playlist, updating it,
     * and persisting the playlist.
     * <p>
     * It then returns the updated playlist.
     * <p>
     * If the playlist does not exist, this should throw a PlaylistNotFoundException.
     * <p>
     * If the provided playlist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     * <p>
     * If the request tries to update the customer ID,
     * this should throw an InvalidAttributeChangeException
     *
     * @param updatePlaylistRequest request object containing the playlist ID, playlist name, and customer ID
     *                              associated with it
     * @return updatePlaylistResult result object containing the API defined {@link PlaylistModel}
     */
    @Override
    public UpdatePlaylistResult handleRequest(final UpdatePlaylistRequest updatePlaylistRequest, Context context) {
        log.info("Received UpdatePlaylistRequest {}", updatePlaylistRequest);

        Playlist playlistObj = new Playlist();
        String playlistIdRequest = updatePlaylistRequest.getId();
        String customerIdRequest = updatePlaylistRequest.getCustomerId();

        //If the playlist id to update is not found, throws PlaylistNotFoundException.
        try {
            playlistDao.getPlaylist(playlistIdRequest);
        } catch (PlaylistNotFoundException exception) {
            throw exception;
        }

        /*If the customer ID or playlist name contains any of the invalid characters,
         *throws an InvalidAttributeValueException else updates playlist name.
         */
        if (!MusicPlaylistServiceUtils.isValidString(updatePlaylistRequest.getName()) ||
                !MusicPlaylistServiceUtils.isValidString(updatePlaylistRequest.getCustomerId())) {
            throw new InvalidAttributeValueException(" is not a valid name");
        } else {
            playlistObj.setName(updatePlaylistRequest.getName());
            playlistObj.setCustomerId(updatePlaylistRequest.getCustomerId());
        }

        /*If the customerIdRequest is different from the playlist customerId requesting to be changed
         *throw InvalidAttributeException
         */
        if (!customerIdRequest.equals(playlistDao.getPlaylist(playlistIdRequest).getCustomerId())) {
            throw new InvalidAttributeChangeException();
        }

        playlistDao.savePlaylist(playlistObj);
        PlaylistModel playlistModel = new ModelConverter().toPlaylistModel(playlistObj);

        return UpdatePlaylistResult.builder()
                .withPlaylist(playlistModel)
                .build();
    }
}

