package com.FileUploader.Dropbox.GlobalException;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String msg) {
        super(msg);
    }
}
