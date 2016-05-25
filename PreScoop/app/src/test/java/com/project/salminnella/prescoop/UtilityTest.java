package com.project.salminnella.prescoop;

import com.project.salminnella.prescoop.utility.Utilities;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by anthony on 5/25/16.
 */
public class UtilityTest {
    @Test
    public void buildAddressString() {
        String expectedResult = "1234 Here St, San Francisco, Ca 94118";
        String actuResult = Utilities.buildAddressString("1234 Here St", "San Francisco", "Ca", "94118");

        assertEquals(expectedResult, actuResult);
    }
}
