package com.example.psyrq.runningtracker;

import android.net.Uri;

/**
 * Created by qianruofei on 2018/1/10.
 */

public class TrackerProviderContract {

    final static String AUTHORITY = "com.example.psyrq.trackerprovider";
    final static Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public interface URI {
        Uri ID_INSERT = Uri.parse("content://" + AUTHORITY + "/tracker/insert");
        Uri ID_QUERY = Uri.parse("content://" + AUTHORITY + "/tracker/query");
    }
}
