package rx.syncano;

import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.api.Where;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;
import com.syncano.library.simple.RequestBuilder;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Subscriber;

/**
 * Created by pablobaldez on 24/04/16.
 *
 * Copyright (C) 2015 8tory, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Uses Observable to wraps all callback functions
 */
public class SyncanoObservable {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SyncanoObject Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static <T extends SyncanoObject> Observable<T> save(T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.save(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> delete(T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.delete(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> fetch(T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.fetch(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RequestBuilder and Where Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Load data with a predefined {@link Where} instance
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param where instance load the objects
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a {@link Subscriber} subscribes to it, will execute the specified
     *         function
     */
    public static <T extends SyncanoObject> Observable<T> where(Where<T> where) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                where.get(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Load data with a predefined {@link RequestBuilder} instance
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param clazz Syncano class that will be requested.
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a {@link Subscriber} subscribes to it, will execute the specified
     *         function
     */
    public static <T extends SyncanoObject> Observable<T> get(Class<T> clazz){
        return get(Syncano.please(clazz));
    }

    /**
     * Load data with a predefined {@link RequestBuilder} instance. Use it when you want to apply
     * limits, filters, ordering...
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param builder instance load the objects
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a {@link Subscriber} subscribes to it, will execute the specified
     *         function
     */
    public static <T extends SyncanoObject> Observable<T> get(RequestBuilder<T> builder) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                builder.get(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a {@link Subscriber} subscribes to it, will load one
     * object using an id as reference
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular {@link Scheduler}.</dd>
     * </dl>
     *
     * @param clazz Syncano class that will be requested.
     * @param id id of syncano object
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a {@link Subscriber} subscribes to it, will execute the specified
     *         function
     */
    public static <T extends SyncanoObject> Observable<T> get(Class<T> clazz, int id){
        return Observable.create((OnSubscribe<T>) subscriber ->
                Syncano.please(clazz).get(id, new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<Integer> count(Class<T> clazz) {
        return Observable.create((OnSubscribe<Integer>) subscriber ->
                Syncano.please(clazz).getCountEstimation(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ScriptEndpoint and Trace Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static Observable<Trace> run(ScriptEndpoint se) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                se.run(new RxSyncanoCallback<>(subscriber)));
    }

    public static Observable<Trace> run(ScriptEndpoint se, JsonObject payload) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                se.run(payload, new RxSyncanoCallback<>(subscriber)));
    }

    public static Observable<String> runCustomResponse(ScriptEndpoint se) {
        return Observable.create((OnSubscribe<String>) subscriber ->
                se.runCustomResponse(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T> Observable<T> runCustomResponse(ScriptEndpoint se, Class<T> clazz) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                se.runCustomResponse(clazz, new RxSyncanoCallback<>(subscriber)));
    }

    public static <T> Observable<T> runCustomResponse(ScriptEndpoint se, Class<T> clazz, JsonObject payload) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                se.runCustomResponse(clazz, payload, new RxSyncanoCallback<>(subscriber)));
    }

    public static Observable<Trace> fetch(Trace trace) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                trace.fetch(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Syncano Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static <T extends SyncanoObject> Observable<T> getObject(Syncano syncano, T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObject(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> getObject(Syncano syncano, Class<T> clazz,  int id) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObject(clazz, id).sendAsync(new RxSyncanoCallback<T>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> getObjects(Syncano syncano, Class<T> clazz){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObjects(clazz).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> getObjects(Syncano syncano, Class<T> clazz, String pageUrl){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObjects(clazz, pageUrl).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> deleteObject(Syncano syncano, Class<T> clazz, int id) {
        return Observable.create((OnSubscribe<T>) subscriber -> syncano.deleteObject(clazz, id));
    }

    public static <T extends SyncanoObject> Observable<T> deleteObject(Syncano syncano, T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.deleteObject(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> createObject(Syncano syncano,T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.createObject(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> createObject(Syncano syncano, T t, boolean updateGivenObject) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.createObject(t, updateGivenObject).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends AbstractUser> Observable<T> fetchCurrentUser(Syncano syncano, Class<T> clazz){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.fetchCurrentUser(clazz).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends AbstractUser> Observable<T> fetchCurrentUser(Syncano syncano, T user) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.fetchCurrentUser(user).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    public static <T extends SyncanoObject> Observable<T> getObjectsDataEndpoint(Syncano syncano, Class<T> clazz, String dataEndpoint) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObjectsDataEndpoint(clazz, dataEndpoint).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }
}
