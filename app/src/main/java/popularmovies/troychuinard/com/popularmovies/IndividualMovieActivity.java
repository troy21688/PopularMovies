package popularmovies.troychuinard.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import popularmovies.troychuinard.com.popularmovies.Model.Movie;

public class IndividualMovieActivity extends AppCompatActivity {

    private TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_movie);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("Movie");
        String title = movie.getOriginal_title();

        testText = findViewById(R.id.movie_name);
        testText.setText(title);



    }
}
