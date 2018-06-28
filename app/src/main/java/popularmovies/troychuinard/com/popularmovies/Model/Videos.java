package popularmovies.troychuinard.com.popularmovies.Model;

import java.util.ArrayList;

public class Videos {

    private int id;
    private ArrayList<Video> results;


    public void setId(int id) {
        this.id = id;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Video> getResults() {
        return results;
    }
}
