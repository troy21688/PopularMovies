package popularmovies.troychuinard.com.popularmovies.Model;

import java.util.ArrayList;
import java.util.List;

public class Movies {

    private int page;
    private int total_results;
    private int total_pages;
    private ArrayList<Movie> results = new ArrayList<Movie>();


    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }
}
