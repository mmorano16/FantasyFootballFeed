package com.mmorano.fantasyfootballfeed.Workers;

import com.mmorano.fantasyfootballfeed.DataFetching.EspnDataFetcher;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayCell;
import com.mmorano.fantasyfootballfeed.Processing.DataInitializer;
import javafx.concurrent.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class FeedWorker extends Task<ArrayList<Play>> {
    DataInitializer initializer;

    public FeedWorker(DataInitializer initializer){
        this.initializer = initializer;
    }
    @Override
    protected ArrayList<Play> call() {
        updateMessage("Fetching Data...");
        EspnDataFetcher dataFetcher = new EspnDataFetcher();
        ArrayList<Play> plays = dataFetcher.getPlays(initializer, null);
        updateMessage("Received " + plays.size() + " Plays");
        plays.sort(Comparator.comparing(Play::getIdInt).reversed());
        if(plays.size() > 50)
            plays.subList(50, plays.size()).clear();
        ArrayList<PlayCell> cells = new ArrayList<>();
        return plays;
    }
}
