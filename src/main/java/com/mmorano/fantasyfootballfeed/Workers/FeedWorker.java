package com.mmorano.fantasyfootballfeed.Workers;

import javafx.concurrent.Task;

public class FeedWorker extends Task<String> {
    @Override
    protected String call() throws Exception {
        updateMessage("Is this working");
        try{Thread.sleep(5000);}catch (InterruptedException e){}
        updateMessage("It Worked Cool!");
        return "";
    }
}
