package rx.syncano;

import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

import rx.Subscriber;

/**
 * Created by pablobaldez on 24/04/16.
 */
class RxSyncanoListCallback<T extends SyncanoObject> extends SyncanoListCallback<T>{

    private final Subscriber<? super T> subscriber;

    public RxSyncanoListCallback(Subscriber<? super T> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void success(ResponseGetList<T> response, List<T> result) {
        if(response.isSuccess()) {
            for(T t: result) {
                subscriber.onNext(t);
            }
            subscriber.onCompleted();
        }
        else {
            failure(response);
        }
    }

    @Override
    public void failure(ResponseGetList<T> response) {
        subscriber.onError(new RxSyncanoException(response));
    }
}
