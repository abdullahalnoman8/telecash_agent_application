package com.datasoft_bd.telecashagent;

import android.app.Application;
import android.content.Context;

import com.datasoft_bd.telecashagent.utils.LocaleHelper;

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
