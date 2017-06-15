package com.e7.whatisthecolor;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.e7.whatisthecolor.Class.Color;
import com.e7.whatisthecolor.Controller.Colors;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute Colors functions.
 */
@RunWith(AndroidJUnit4.class)
public class ColorsTest {
    @Test
    public void loadColors_Test() throws Exception {
        Colors.loadColors(InstrumentationRegistry.getContext(), "test_colors.json");
        Color[] colorsTest = Colors.getColors();
        assertNotNull(colorsTest);
        assertEquals(2, colorsTest.length);
        assertEquals("Black", colorsTest[0].getName());
        assertEquals("White", colorsTest[1].getName());
    }

    @Test
    public void getColor_Test() throws Exception {
        String colorNameTest = Colors.getColorName("000000");
        assertEquals("Black", colorNameTest);
    }
}
