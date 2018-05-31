package popularmovies.troychuinard.com.popularmovies;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMovieResults;

    private ArrayList<String> mMovieURLS;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_spinner, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spiner_list_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieResults = findViewById(R.id.main_recyclerview_image_results);
        GridLayoutManager glm = new GridLayoutManager(this, 21);
        glm.setOrientation(LinearLayoutManager.VERTICAL);
        mMovieResults.setLayoutManager(glm);

        mMovieURLS = new ArrayList<>();




//        public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//
//
//            // Provide a reference to the views for each data item
//            // Complex data items may need more than one view per item, and
//            // you provide access to all the views for a data item in a view holder
//            public class ViewHolder extends RecyclerView.ViewHolder {
//                // each data item is just a string in this case
//                protected ImageView mResultImage;
//
//
//                public ViewHolder(View v) {
//                    super(v);
//                    mResultImage = (ImageView) v.findViewById(R.id.movie_poster_image);
//
//
//                }
//            }
//
//
//
//
//            // Provide a suitable constructor (depends on the kind of dataset)
//            public MyAdapter(ArrayList<String> mDataSet) {
//                mMovieURLS = mDataSet;
//            }
//
//
//            // Create new views (invoked by the layout manager)
//            @Override
//            public ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                 int viewType) {
//                // create a new view
//                View v = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.grid_item, parent, false);
//                // set the view's size, margins, paddings and layout parameters
//                return new ViewHolder(v);
//            }
//
//            // Replace the contents of a view (invoked by the layout manager)
//
//            //The OutOfBoundsException is pointing here
//            @Override
//            public void onBindViewHolder(ViewHolder holder, final int position) {
//                Log.v("ON_BIND", "ON_BINDVIEWHOLDER CALLED");
//                final String urlForPhoto = mMovieURLS.get(position);
//                if (mProgressBar.getVisibility() == View.VISIBLE) {
//                    mProgressBar.setVisibility(View.INVISIBLE);
//                }
//                Picasso.with(getApplicationContext())
//                        .load(urlForPhoto)
//                        .placeholder(R.drawable.progress_animation)
//                        .into(holder.mResultImage);
//
//
//                });
//
//            }
//            //test
//
//            //test
//            // Return the size of your dataset (invoked by the layout manager)
//            @Override
//            public int getItemCount() {
//                return mMovieURLS.size();
//            }
//        }


    }
}
