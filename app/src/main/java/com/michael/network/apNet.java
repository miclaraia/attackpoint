package com.michael.network;

import com.michael.attackpoint.Singleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michael on 8/5/15.
 */
public class apNet {
    private static final String DEBUG_TAG = "attackpoint.apNet";
    private static final int LOG = 0;

    Singleton singleton = Singleton.getInstance();
    private static final String loginURL = "http://www.attackpoint.org/dologin.jsp";
    //private static Map<String, String> cookies;
    private static CookieManager cookies;

    //String url = "http://www.attackpoint.org/log.jsp/user_11778";
    String url = "http://www.attackpoint.org/viewlog.jsp/user_11778/period-7/enddate-2015-08-02";
    int type = LOG;
}
