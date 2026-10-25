package com.mmorano.fantasyfootballfeed.DataParsing.Plays;

import com.google.gson.JsonObject;
import com.mmorano.fantasyfootballfeed.DataParsing.DataParser;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayType;

public class PlayTypeParser extends DataParser {
    public PlayType parsePlayType(JsonObject jObject){
        PlayType type = new PlayType();
        type.setId(jObject.get("id").getAsString());
        type.setText(jObject.get("text").getAsString());
        if(jObject.has("abbreviation"))
            type.setAbbrev(jObject.get("abbreviation").getAsString());
        return type;
    }
}
