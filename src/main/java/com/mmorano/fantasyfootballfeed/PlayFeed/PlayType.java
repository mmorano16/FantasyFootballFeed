package com.mmorano.fantasyfootballfeed.PlayFeed;
public class PlayType {
    private String id;
    private String text;
    private String abbrev;

    public PlayType(){}
    public PlayType(String id, String text, String abbrev){
        this.id = id;
        this.text = text;
        this.abbrev = abbrev;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }
}
