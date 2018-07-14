package popularmovies.troychuinard.com.popularmovies;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class AppDatabase_Impl extends AppDatabase {
  private volatile MovieDao _movieDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Movie` (`vote_count` INTEGER NOT NULL, `id` INTEGER NOT NULL, `video` INTEGER NOT NULL, `title` TEXT, `popularity` REAL NOT NULL, `poster_path` TEXT, `original_language` TEXT, `original_title` TEXT, `backdrop_path` TEXT, `adult` INTEGER NOT NULL, `overview` TEXT, `release_date` TEXT, `vote_average` INTEGER, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"f2d5e97cfc81d31dd5a9d7225163db11\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Movie`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsMovie = new HashMap<String, TableInfo.Column>(13);
        _columnsMovie.put("vote_count", new TableInfo.Column("vote_count", "INTEGER", true, 0));
        _columnsMovie.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsMovie.put("video", new TableInfo.Column("video", "INTEGER", true, 0));
        _columnsMovie.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsMovie.put("popularity", new TableInfo.Column("popularity", "REAL", true, 0));
        _columnsMovie.put("poster_path", new TableInfo.Column("poster_path", "TEXT", false, 0));
        _columnsMovie.put("original_language", new TableInfo.Column("original_language", "TEXT", false, 0));
        _columnsMovie.put("original_title", new TableInfo.Column("original_title", "TEXT", false, 0));
        _columnsMovie.put("backdrop_path", new TableInfo.Column("backdrop_path", "TEXT", false, 0));
        _columnsMovie.put("adult", new TableInfo.Column("adult", "INTEGER", true, 0));
        _columnsMovie.put("overview", new TableInfo.Column("overview", "TEXT", false, 0));
        _columnsMovie.put("release_date", new TableInfo.Column("release_date", "TEXT", false, 0));
        _columnsMovie.put("vote_average", new TableInfo.Column("vote_average", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMovie = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMovie = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMovie = new TableInfo("Movie", _columnsMovie, _foreignKeysMovie, _indicesMovie);
        final TableInfo _existingMovie = TableInfo.read(_db, "Movie");
        if (! _infoMovie.equals(_existingMovie)) {
          throw new IllegalStateException("Migration didn't properly handle Movie(popularmovies.troychuinard.com.popularmovies.Model.Movie).\n"
                  + " Expected:\n" + _infoMovie + "\n"
                  + " Found:\n" + _existingMovie);
        }
      }
    }, "f2d5e97cfc81d31dd5a9d7225163db11", "17e7f8dc41edd4c84a198648b86a083e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Movie");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Movie`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public MovieDao movieDao() {
    if (_movieDao != null) {
      return _movieDao;
    } else {
      synchronized(this) {
        if(_movieDao == null) {
          _movieDao = new MovieDao_Impl(this);
        }
        return _movieDao;
      }
    }
  }
}
