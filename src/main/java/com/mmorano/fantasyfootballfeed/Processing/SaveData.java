package com.mmorano.fantasyfootballfeed.Processing;

import java.io.Serializable;

public class SaveData implements Serializable {
    String sleeperId;

    public String getSleeperId() {
        return sleeperId;
    }

    public void setSleeperId(String sleeperId) {
        this.sleeperId = sleeperId;
    }
}
