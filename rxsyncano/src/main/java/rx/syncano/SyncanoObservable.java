package rx.syncano;

import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Created by pablobaldez on 24/04/16.
 */
public class SyncanoObservable {

    public static <T extends SyncanoObject> Observable<T> save(T t){
        return Observable.create((OnSubscribe<T>) subscriber -> t.save(new SyncanoCallback<T>() {
            @Override
            public void success(Response<T> response, T result) {
                if (response.isSuccess()){
                    subscriber.onNext(t);
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
        })).subscribeOn(Schedulers.io());
    }

    public static <T extends SyncanoObject> Observable<T> where(Where<T> where) {
        return Observable.create((OnSubscribe<T>) subscriber -> where.get(new SyncanoListCallback<T>() {
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
        })).subscribeOn(Schedulers.io());
    }

    public static <T extends SyncanoObject> Observable<T> get(Class<T> clazz) {
        return Observable.create((OnSubscribe<T>) subscriber -> Syncano.please(clazz).get(
                new SyncanoListCallback<T>() {
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
        })).subscribeOn(Schedulers.io());
    }

    public static <T extends SyncanoObject> Observable<T> get(Class<T> clazz, int id){
         return Observable.create((OnSubscribe<T>) subscriber -> Syncano.please(clazz).get(id, new SyncanoCallback<T>() {
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
         })).subscribeOn(Schedulers.io());
    }



}
