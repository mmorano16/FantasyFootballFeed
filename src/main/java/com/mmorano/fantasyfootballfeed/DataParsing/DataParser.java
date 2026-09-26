package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.stream.StreamSupport;

public class DataParser {

    protected JsonObject findObjectByValue(JsonArray jsonArray, String targetKey, String targetValue, boolean exact){
        if(targetValue == null)
            targetValue = "";
        if(exact)
            return findObjectByExactValue(jsonArray, targetKey, targetValue);
        else
            return findObjectByValue(jsonArray, targetKey, targetValue);
    }

    protected ArrayList<JsonObject> findAllObjectsByValue(JsonArray jsonArray, String targetKey, String targetValue){
        return new ArrayList<>(StreamSupport.stream(jsonArray.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .filter(obj -> obj.has(targetKey) && obj.get(targetKey).getAsString().equals(targetValue)).toList());
    }

    private JsonObject findObjectByValue(JsonArray jsonArray, String targetKey, String targetValue) {
        return StreamSupport.stream(jsonArray.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .filter(obj -> obj.has(targetKey) && obj.get(targetKey).getAsString().contains(targetValue))
                .findFirst()
                .orElse(null); // Returns null if not found
    }

    private JsonObject findObjectByExactValue(JsonArray jsonArray, String targetKey, String targetValue) {
        return StreamSupport.stream(jsonArray.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .filter(obj -> obj.has(targetKey) && obj.get(targetKey).getAsString().equals(targetValue))
                .findFirst()
                .orElse(null); // Returns null if not found
    }

    public boolean tryParseInt(String st){
        try {
            Integer.parseInt(st);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    protected String getIdFromLink(String url){
        int start = url.lastIndexOf("/") + 1;
        int end = url.lastIndexOf("?");
        String id = "";
        if(start > -1 && start < end){
            id = url.substring(start, end);
        }
        return id;
    }
}
