package com.hanlie.mobisuas_2111500077;

import android.app.Application;

public class ClassGlobal extends Application {

    public static String global_ipaddress = "http://10.0.2.2";
    private static String URL = global_ipaddress + "/webmobisuas_2111500077/android/";

    private String idUser = ""; // <-- ini yang dipakai

    public String getUrl() { return URL; }
    public void setURL(String url) { URL = url; }

    public String getIdUser() { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }
}
