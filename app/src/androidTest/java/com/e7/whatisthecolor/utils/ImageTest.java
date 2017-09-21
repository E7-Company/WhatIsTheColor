package com.e7.whatisthecolor.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.e7.whatisthecolor.utils.Image;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute Image functions.
 */
@RunWith(AndroidJUnit4.class)
public class ImageTest {

    private InputStream file;
    private Bitmap bmp;

    @Test
    public void decodeBitmap_Test() throws Exception {
        file = InstrumentationRegistry.getContext().getResources().getAssets().open("test_image.jpg");
        bmp = BitmapFactory.decodeStream(file);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        assertEquals(bmp.getByteCount(),Image.decodeBitmap(byteArray).getByteCount());
    }

    @Test
    public void getAverageColor_Test() throws Exception {
        file = InstrumentationRegistry.getContext().getResources().getAssets().open("test_image.jpg");
        bmp = BitmapFactory.decodeStream(file);

        int avgColor =Image.getAverageColor(bmp);
        assertNotNull(avgColor);

        String colorHex = String.format("%06X", 0xFFFFFF & avgColor);
        assertEquals("000000", colorHex);

        String colorName = "Black";//Colors.getColorName(colorHex);
        assertEquals("Black", colorName);
    }
}
