package popularmovies.troychuinard.com.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import popularmovies.troychuinard.com.popularmovies.Model.Movie;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movieList;



    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movieList = database.movieDao().loadAllMovies();
        Log.v("VIEWMODel", "VIEWMODEL");

    }


    public LiveData<List<Movie>> getMovies(){
        return movieList;
    }


}
