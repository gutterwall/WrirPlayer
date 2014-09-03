package org.wrir.WrirPlayer.model;

public class Show {
    private String title, thumbnailUrl, presenter, type, datestamp, mp3;

    public Show() {
    }

    public Show(String name, String thumbnailUrl, String type, String presenter,
                 String datestamp, String mp3) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.type = type;
        this.presenter = presenter;
        this.datestamp = datestamp;
        this.mp3 = mp3;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }
    public String getMp3() {
        return mp3;
    }
    public void setMp3 (String mp3){
        this.mp3 = mp3;
    }



}
