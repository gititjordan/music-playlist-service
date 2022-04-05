package com.amazon.ata.music.playlist.service.dependency;

import com.amazon.ata.aws.dynamodb.DynamoDbClientProvider;
import com.amazon.ata.music.playlist.service.dynamodb.AlbumTrackDao;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DaoModule {
    DynamoDBMapper mapper;

    @Provides
    @Singleton
    public AlbumTrackDao provideAlbumTrackDao() {
        return new AlbumTrackDao(provideDynamoDBMapper());
    }


    @Provides
    @Singleton
    public PlaylistDao providePlaylistDao() {
        return new PlaylistDao(provideDynamoDBMapper());
    }

    @Provides
    @Singleton
    public DynamoDBMapper provideDynamoDBMapper() {
        if (mapper == null) {
           mapper = new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient(Regions.US_WEST_2));
        }
        return mapper;
        }

}
