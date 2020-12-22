package com.lkj.weatherforecast;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    }

    @Test
    public void dateformat() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d", Locale.ENGLISH);
        String dayOfWeek = simpleDateFormat.format(date);
        System.out.println(dayOfWeek);
    }

    @Test
    public void dateTest() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date("2020-12-22");
        System.out.println(date.toString());
    }

    @Test
    public void stringTest() {
        String s = "2020-12-22";
        System.out.println("2020-12-22".replaceAll("-", "/"));
    }
}