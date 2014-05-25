package net.thequester.android.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.TextView;

import net.thequester.android.MainActivity;
import net.thequester.android.R;

/**
 * Author: Tomo
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    @SmallTest
    public void testTextViewNotNull(){
        TextView textView = (TextView) activity.findViewById(R.id.id_value);
        assertNotNull(textView);
    }


}
