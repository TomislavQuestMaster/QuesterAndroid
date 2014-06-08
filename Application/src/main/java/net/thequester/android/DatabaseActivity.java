package net.thequester.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import net.thequester.android.database.QuestDetailsDao;
import net.thequester.android.model.QuestDetails;

import java.util.List;
import java.util.Random;

/**
 * Author: Tomo
 */
public class DatabaseActivity extends ActionBarActivity {

    QuestDetailsDao helper;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);

        helper = new QuestDetailsDao(getApplicationContext());

        doSampleDatabaseStuff("onCreate", tv);
        setContentView(tv);
    }

    /**
     * Do our sample database stuff.
     */
    private void doSampleDatabaseStuff(String action, TextView tv) {
        // get our dao
        // query for all of the data objects in the database
        List<QuestDetails> list = helper.getAll();
        // our string builder for building the content-view
        StringBuilder sb = new StringBuilder();
        sb.append("got ").append(list.size()).append(" entries in ").append(action).append("\n");

        // if we already have items in the database
        int simpleC = 0;
        for (QuestDetails simple : list) {
            sb.append("------------------------------------------\n");
            sb.append("[").append(simpleC).append("] = ").append(simple.getName()).append("\n");
            simpleC++;
        }
        sb.append("------------------------------------------\n");
        String LOG_TAG = "DBA";
        for (QuestDetails simple : list) {
            helper.delete(simple);
            sb.append("deleted id ").append(simple.getName()).append("\n");
            Log.i(LOG_TAG, "deleting simple(" + simple.getName() + ")");
            simpleC++;
        }

        int createNum;
        do {
            createNum = new Random().nextInt(3) + 1;
        } while (createNum == list.size());
        for (int i = 0; i < createNum; i++) {
            // create a new simple object
            long millis = System.currentTimeMillis();
            QuestDetails simple = new QuestDetails();
            simple.setName("ivo");
            // store it in the database
            helper.insert(simple);
            Log.i(LOG_TAG, "created simple(" + millis + ")");
            // output it
            sb.append("------------------------------------------\n");
            sb.append("created new entry #").append(i + 1).append(":\n");
            sb.append(simple.getName()).append("\n");
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            // ignore
            }
        }

        tv.setText(sb.toString());
        Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
    }
}
