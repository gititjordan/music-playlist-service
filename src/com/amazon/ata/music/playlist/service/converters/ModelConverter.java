package com.amazon.ata.music.playlist.service.converters;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.SongModel;
import org.testng.mustache.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelConverter {
    /**
     * Converts a provided {@link Playlist} into a {@link PlaylistModel} representation.
     * @param playlist the playlist to convert
     * @return the converted playlist
     */
    public PlaylistModel toPlaylistModel(Playlist playlist) {

        List<String> tags = null;
        if (playlist.getTags() != null) {
            tags = new ArrayList<>(playlist.getTags());
        }

        return PlaylistModel.builder()
            .withId(playlist.getId())
                .withCustomerId(playlist.getCustomerId())
                .withName(playlist.getName())
                .withSongCount(playlist.getSongCount())
                .withTags(tags)
                .build();
    }

    public SongModel  toSongModel(AlbumTrack albumTrack) {
        return SongModel.builder()
                .withAsin(albumTrack.getAsin())
                .withAlbum(albumTrack.getAlbum())
                .withTrackNumber(albumTrack.getTrackNumber())
                .withTitle(albumTrack.getTitle())
                .build();

    }

    public List<SongModel> toSongModelList(List<AlbumTrack> songList) {
        List<SongModel> songModelList = new ArrayList<>();
        for (AlbumTrack song : songList) {
           // SongModel songModel = new ModelConverter().toSongModel(song);
            songModelList.add(toSongModel(song));
        }
        return songModelList;
    }
}
