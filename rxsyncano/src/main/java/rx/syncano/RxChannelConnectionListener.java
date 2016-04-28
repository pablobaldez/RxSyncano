package rx.syncano;

import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;

import rx.Subscriber;

/**
 * Created by pablobaldez on 4/28/16.
 */
public class RxChannelConnectionListener implements ChannelConnectionListener{

    private final Subscriber<? super Notification> subscriber;

    public RxChannelConnectionListener(Subscriber<? super Notification> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onNotification(Notification notification) {
        subscriber.onNext(notification);
    }

    @Override
    public void onError(Response<Notification> response) {
        subscriber.onError(new RxSyncanoException(response));
    }
}
