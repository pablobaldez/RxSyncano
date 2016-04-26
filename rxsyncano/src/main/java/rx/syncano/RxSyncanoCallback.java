package rx.syncano;

import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;

import rx.Subscriber;

/**
 * Created by pablobaldez on 24/04/16.
 */
class RxSyncanoCallback<T> implements SyncanoCallback<T> {

    private final Subscriber<? super  T> subscriber;

    public RxSyncanoCallback(Subscriber<? super T> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void success(Response<T> response, T result) {
        if(response.isSuccess()) {
            subscriber.onNext(result);
            subscriber.onCompleted();
        }
        else {
            failure(response);
        }
    }

    @Override
    public void failure(Response<T> response) {
        subscriber.onError(new RxSyncanoException(response));
    }
}
