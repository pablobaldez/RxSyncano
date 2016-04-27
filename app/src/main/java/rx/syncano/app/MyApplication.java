package rx.syncano.app;

import android.app.Application;

import com.syncano.library.Syncano;

/**
 * Created by pablobaldez on 4/27/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Syncano.init("", "", this);
    }


}
