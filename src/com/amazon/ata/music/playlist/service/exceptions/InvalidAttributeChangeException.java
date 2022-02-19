package com.amazon.ata.music.playlist.service.exceptions;

import com.amazon.ata.music.playlist.service.activity.UpdatePlaylistException;

public class InvalidAttributeChangeException extends InvalidAttributeException {

    public InvalidAttributeChangeException() {
        super();
    }

    public InvalidAttributeChangeException(String message) {

    }

    InvalidAttributeChangeException(String message, Throwable cause) {

    }

    InvalidAttributeChangeException(Throwable cause) {

    }

    public void printStackTrace() {
    }
}
