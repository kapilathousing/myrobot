package com.kapil.robot.model;

/**
 * Created by housing on 11/9/15.
 */
public class AirNews {
    private int id;
    private String name;
    private String url;
    private String created;
    private int isReady;
    private Long downloadId;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setIsReady(int isReady) {
        this.isReady = isReady;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCreated() {
        return created;
    }

    public int getIsReady() {
        return isReady;
    }

    public Long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }
}
