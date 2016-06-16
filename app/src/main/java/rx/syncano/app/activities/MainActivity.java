package rx.syncano.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.Syncano;
import com.syncano.library.api.RequestPost;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.Notification;
import com.syncano.library.data.Script;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;

import java.util.List;

import rx.Subscriber;
import rx.syncano.RxSyncanoException;
import rx.syncano.SyncanoObservable;
import rx.syncano.app.R;
import rx.syncano.app.models.MyClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SyncanoObservable.get(MyClass.class).subscribe(subscriber());
    }

    private Subscriber<MyClass> subscriber() {
        return new Subscriber<MyClass>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.d("pablo", "on start---------------------");
            }

            @Override
            public void onCompleted() {
                Log.d("pablo", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("pablo", "error", e);
            }

            @Override
            public void onNext(MyClass myClass) {
                Log.d("pablo", "id: " + myClass.getId());
            }
        };
    }
}
