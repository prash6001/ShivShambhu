package com.pjadhav.myapplication.model;

/**
 * Created by pjadhav on 6/12/17.
 */

public class MusicFile {

    public int id;
    private String fileName;
    private String fileUrl;
    private int isDownloaded;

    public MusicFile() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String content;

    public MusicFile(int id, String fileName, String fileUrl, int isDownloaded) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.isDownloaded = isDownloaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(int downloaded) {
        isDownloaded = downloaded;
    }
}
