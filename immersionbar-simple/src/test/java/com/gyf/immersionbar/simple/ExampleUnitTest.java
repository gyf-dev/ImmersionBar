package com.gyf.immersionbar.simple;

import com.apkfuns.logutils.LogUtils;
import com.gyf.immersionbar.simple.utils.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(2));
        }
    }
}