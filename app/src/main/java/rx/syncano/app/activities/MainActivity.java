package rx.syncano.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.syncano.library.Syncano;

import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.syncano.SyncanoObservable;
import rx.syncano.app.R;
import rx.syncano.app.models.TestModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SyncanoObservable.get(TestModel.class)
                .subscribeOn(Schedulers.io())
                .subscribe(subscriber());
    }

    private Subscriber<TestModel> subscriber(){
        return new Subscriber<TestModel>() {
            @Override
            public void onStart() {
                super.onStart();
                Log.d("pablo","Started----------------------");
            }

            @Override
            public void onCompleted() {
                Log.d("pablo","Completed----------------------");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("pablo", "error", e);
            }

            @Override
            public void onNext(TestModel testModel) {
                Log.d("pablo",testModel.getName());
            }
        };
    }
}
