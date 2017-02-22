package com.example.mrwen.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mrwen on 2017/2/19.
 */

public class TimeUtils {
    public static String Format(Date date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        return  mFormat.format(date);
    }
}
