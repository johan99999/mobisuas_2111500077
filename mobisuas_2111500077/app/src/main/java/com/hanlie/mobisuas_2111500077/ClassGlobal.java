package com.hanlie.mobisuas_2111500077;
import android.app.Application;

public class ClassGlobal extends Application {
    public static String global_ipaddress="http://10.0.2.2";
    private static String URL=global_ipaddress+"/webmobisuas_2111500077/android/";
    public String getUrl() { return URL; }
    public void setURL (String url) { URL = url; }
    public static String global_gambar="gambar";
    public static Integer global_idlaporan=0;
    public static Integer global_iduser=0;
}
