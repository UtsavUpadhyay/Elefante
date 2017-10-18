package com.elefante;

public class Elefante extends MyApp {

    public static final String TAG = Elefante.class
            .getSimpleName();

    private static Elefante mInstance;

    public static synchronized Elefante getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

}

