package net.thequester.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.plus.model.people.Person;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import net.thequester.android.R;
import net.thequester.android.model.QuestDetails;

import java.sql.SQLException;
import java.util.List;

/**
 * Author: Tomo
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context context;
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "helloAndroid.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<QuestDetails, Integer> simpleDao = null;
    private RuntimeExceptionDao<QuestDetails, Integer> simpleRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        this.context = context;
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, QuestDetails.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

        // here we try inserting data in the on-create as a test
        RuntimeExceptionDao<QuestDetails, Integer> dao = getSimpleDataDao();
        long millis = System.currentTimeMillis();
        // create some entries in the onCreate
        QuestDetails simple = new QuestDetails();
        simple.setName("pero");
        dao.create(simple);
        simple = new QuestDetails();
        simple.setName("tomo");
        dao.create(simple);
        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, QuestDetails.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<QuestDetails, Integer> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(QuestDetails.class);
        }
        return simpleDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<QuestDetails, Integer> getSimpleDataDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(QuestDetails.class);
        }
        return simpleRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
    }


    //method for list of person
    public List<QuestDetails> GetData()
    {
        DatabaseHelper helper = new DatabaseHelper(context);
        RuntimeExceptionDao<QuestDetails, Integer> dao = helper.getSimpleDataDao();
        return dao.queryForAll();
    }

    //method for insert data
    public int addData(QuestDetails details)
    {
        RuntimeExceptionDao<QuestDetails, Integer> dao = getSimpleDataDao();
        return dao.create(details);
    }

    public void delete(QuestDetails details)
    {
        RuntimeExceptionDao<QuestDetails, Integer> dao = getSimpleDataDao();
        dao.delete(details);
    }

    //method for delete all rows
    public void deleteAll()
    {
        RuntimeExceptionDao<QuestDetails, Integer> dao = getSimpleDataDao();
        List<QuestDetails> list = dao.queryForAll();
        dao.delete(list);
    }
}
