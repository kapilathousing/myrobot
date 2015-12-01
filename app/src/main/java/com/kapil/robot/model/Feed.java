package com.kapil.robot.model;

/**
 * Created by kapilboss on 06/05/15.
 */
public class Feed {
   public String link;
    public String title;
    private int id;
    private int website;

    public String getTitle() {
        return title;
    }


    public String getLink() {
        return link;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setWebsite(int website) {
        this.website = website;
    }
}
