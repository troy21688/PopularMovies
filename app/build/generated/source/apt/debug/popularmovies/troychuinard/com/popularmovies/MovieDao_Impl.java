package popularmovies.troychuinard.com.popularmovies;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.lang.Integer;
import java.lang.Number;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import popularmovies.troychuinard.com.popularmovies.Model.Movie;
import popularmovies.troychuinard.com.popularmovies.Model.NumberConverter;

@SuppressWarnings("unchecked")
public class MovieDao_Impl implements MovieDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfMovie;

  public MovieDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovie = new EntityInsertionAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Movie`(`vote_count`,`id`,`video`,`title`,`popularity`,`poster_path`,`original_language`,`original_title`,`backdrop_path`,`adult`,`overview`,`release_date`,`vote_average`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        stmt.bindLong(1, value.getVote_count());
        stmt.bindLong(2, value.getId());
        final int _tmp;
        _tmp = value.isVideo() ? 1 : 0;
        stmt.bindLong(3, _tmp);
        if (value.getTitle() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTitle());
        }
        stmt.bindDouble(5, value.getPopularity());
        if (value.getPoster_path() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPoster_path());
        }
        if (value.getOriginal_language() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getOriginal_language());
        }
        if (value.getOriginal_title() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getOriginal_title());
        }
        if (value.getBackdrop_path() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getBackdrop_path());
        }
        final int _tmp_1;
        _tmp_1 = value.isAdult() ? 1 : 0;
        stmt.bindLong(10, _tmp_1);
        if (value.getOverview() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getOverview());
        }
        if (value.getRelease_date() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getRelease_date());
        }
        final Integer _tmp_2;
        _tmp_2 = NumberConverter.toInt(value.getVote_average());
        if (_tmp_2 == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindLong(13, _tmp_2);
        }
      }
    };
    this.__deletionAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Movie` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `Movie` SET `vote_count` = ?,`id` = ?,`video` = ?,`title` = ?,`popularity` = ?,`poster_path` = ?,`original_language` = ?,`original_title` = ?,`backdrop_path` = ?,`adult` = ?,`overview` = ?,`release_date` = ?,`vote_average` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        stmt.bindLong(1, value.getVote_count());
        stmt.bindLong(2, value.getId());
        final int _tmp;
        _tmp = value.isVideo() ? 1 : 0;
        stmt.bindLong(3, _tmp);
        if (value.getTitle() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getTitle());
        }
        stmt.bindDouble(5, value.getPopularity());
        if (value.getPoster_path() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getPoster_path());
        }
        if (value.getOriginal_language() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getOriginal_language());
        }
        if (value.getOriginal_title() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getOriginal_title());
        }
        if (value.getBackdrop_path() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getBackdrop_path());
        }
        final int _tmp_1;
        _tmp_1 = value.isAdult() ? 1 : 0;
        stmt.bindLong(10, _tmp_1);
        if (value.getOverview() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getOverview());
        }
        if (value.getRelease_date() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getRelease_date());
        }
        final Integer _tmp_2;
        _tmp_2 = NumberConverter.toInt(value.getVote_average());
        if (_tmp_2 == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindLong(13, _tmp_2);
        }
        stmt.bindLong(14, value.getId());
      }
    };
  }

  @Override
  public void insertMovie(Movie movie) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMovie.insert(movie);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteMovie(Movie movie) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfMovie.handle(movie);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateMovie(Movie movie) {
    __db.beginTransaction();
    try {
      __updateAdapterOfMovie.handle(movie);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Movie> loadAllMovies() {
    final String _sql = "SELECT * FROM Movie";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfVoteCount = _cursor.getColumnIndexOrThrow("vote_count");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfVideo = _cursor.getColumnIndexOrThrow("video");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfPopularity = _cursor.getColumnIndexOrThrow("popularity");
      final int _cursorIndexOfPosterPath = _cursor.getColumnIndexOrThrow("poster_path");
      final int _cursorIndexOfOriginalLanguage = _cursor.getColumnIndexOrThrow("original_language");
      final int _cursorIndexOfOriginalTitle = _cursor.getColumnIndexOrThrow("original_title");
      final int _cursorIndexOfBackdropPath = _cursor.getColumnIndexOrThrow("backdrop_path");
      final int _cursorIndexOfAdult = _cursor.getColumnIndexOrThrow("adult");
      final int _cursorIndexOfOverview = _cursor.getColumnIndexOrThrow("overview");
      final int _cursorIndexOfReleaseDate = _cursor.getColumnIndexOrThrow("release_date");
      final int _cursorIndexOfVoteAverage = _cursor.getColumnIndexOrThrow("vote_average");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Movie _item;
        final int _tmpVote_count;
        _tmpVote_count = _cursor.getInt(_cursorIndexOfVoteCount);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final boolean _tmpVideo;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfVideo);
        _tmpVideo = _tmp != 0;
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final float _tmpPopularity;
        _tmpPopularity = _cursor.getFloat(_cursorIndexOfPopularity);
        final String _tmpPoster_path;
        _tmpPoster_path = _cursor.getString(_cursorIndexOfPosterPath);
        final String _tmpOriginal_language;
        _tmpOriginal_language = _cursor.getString(_cursorIndexOfOriginalLanguage);
        final String _tmpOriginal_title;
        _tmpOriginal_title = _cursor.getString(_cursorIndexOfOriginalTitle);
        final String _tmpBackdrop_path;
        _tmpBackdrop_path = _cursor.getString(_cursorIndexOfBackdropPath);
        final boolean _tmpAdult;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfAdult);
        _tmpAdult = _tmp_1 != 0;
        final String _tmpOverview;
        _tmpOverview = _cursor.getString(_cursorIndexOfOverview);
        final String _tmpRelease_date;
        _tmpRelease_date = _cursor.getString(_cursorIndexOfReleaseDate);
        final Number _tmpVote_average;
        final Integer _tmp_2;
        if (_cursor.isNull(_cursorIndexOfVoteAverage)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getInt(_cursorIndexOfVoteAverage);
        }
        _tmpVote_average = NumberConverter.toNumber(_tmp_2);
        _item = new Movie(_tmpVote_count,_tmpId,_tmpVideo,_tmpTitle,_tmpPopularity,_tmpPoster_path,_tmpOriginal_language,_tmpOriginal_title,_tmpBackdrop_path,_tmpAdult,_tmpOverview,_tmpRelease_date,_tmpVote_average);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
