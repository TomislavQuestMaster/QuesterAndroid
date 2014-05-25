package net.thequester.android.tests;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import net.thequester.android.NumberAdder;

/**
 * Author: Tomo
 */
public class NumberAdderTest extends TestCase {

    @SmallTest
    public void testAdding(){
        assertEquals(5, NumberAdder.add(2,3));
    }
}
