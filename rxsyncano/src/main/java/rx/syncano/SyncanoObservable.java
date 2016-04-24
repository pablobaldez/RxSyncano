package rx.syncano;

import com.syncano.library.Syncano;
import com.syncano.library.api.Where;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;
import com.syncano.library.simple.RequestBuilder;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

/**
 * Created by pablobaldez on 24/04/16.
 */
public class SyncanoObservable {

    public static <T extends SyncanoObject> Observable<T> where(Where<T> where) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                where.get(new RxSyncanoListCallback<T>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> get(RequestBuilder<T> builder) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                builder.get(new RxSyncanoListCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> get(Class<T> clazz, int id){
        return Observable.create((OnSubscribe<T>) subscriber ->
                 Syncano.please(clazz).get(id, new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> save(T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.save(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> delete(T t) {
       return Observable.create((OnSubscribe<T>) subscriber ->
               t.delete(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<Integer> count(Class<T> clazz) {
        return Observable.create((OnSubscribe<Integer>) subscriber ->
                Syncano.please(clazz).getCountEstimation(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T> Observable<T> runEndpoint(ScriptEndpoint se){
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                // TODO call an endpoint
            }
        });
    }

}
