package rx.syncano;

import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.Syncano;
import com.syncano.library.api.IncrementBuilder;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.Where;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.Notification;
import com.syncano.library.data.Profile;
import com.syncano.library.data.PushDevice;
import com.syncano.library.data.Script;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;
import com.syncano.library.data.User;
import com.syncano.library.simple.RequestBuilder;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.subscriptions.Subscriptions;

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
    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will save the data
     * object
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param t Data object to be saved
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> save(T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.save(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will delete the
     * data object
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param t Data object to be deleted
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> delete(T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.delete(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will fetch the
     * data object
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param t Data object to be fetched
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> fetch(T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.fetch(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will increment some
     * field from the data object
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t Data object to be fetched
     * @param incrementBuilder builder used to increment the field
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> addition(Syncano syncano, T t, IncrementBuilder incrementBuilder) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.addition(t, incrementBuilder).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will increment some
     * field from the data object referent to id and type passed
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param id identifier used as reference
     * @param incrementBuilder builder used to increment the field
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> addition(Syncano syncano, Class<T> clazz, int id, IncrementBuilder incrementBuilder){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.addition(clazz, id, incrementBuilder).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will load an object
     * using an Syncano instance for it
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t Data object to be fetched loaded
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> getObject(Syncano syncano, T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObject(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will load an object
     * using an Syncano instance for it
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param id identifier used as reference
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> getObject(Syncano syncano, Class<T> clazz,  int id) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObject(clazz, id).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will load all data
     * objects of this class
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> getObjects(Syncano syncano, Class<T> clazz){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObjects(clazz).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will load all data
     * objects of this class from specific page.
     * You can get this url by calling ResponseGetList.getNextPageUrl() or getPreviousPageUrl()
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param pageUrl page to request
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> getObjects(Syncano syncano, Class<T> clazz, String pageUrl){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObjects(clazz, pageUrl).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Delete a Data
     * Object on Syncano using an id as reference
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param id the id of object to be deleted
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> deleteObject(Syncano syncano, Class<T> clazz, int id) {
        return Observable.create((OnSubscribe<T>) subscriber -> syncano.deleteObject(clazz, id));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Delete a Data
     * Object on Syncano
     *
     * @param syncano Syncano instance used in this operation
     * @param t Syncano object to be deleted
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> deleteObject(Syncano syncano, T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.deleteObject(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Create an
     * object on Syncano
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t Syncano object to be created
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> createObject(Syncano syncano,T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.createObject(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Create an
     * object on Syncano an update the properties of the given object
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t Syncano object to be created
     * @param updateGivenObject Should update fields in passed object, or only return the new
     *                          created object
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> createObject(Syncano syncano, T t, boolean updateGivenObject) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.createObject(t, updateGivenObject).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Update Data
     * Object on Syncano
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t Syncano object to be updated
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> updateObject(Syncano syncano, T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.updateObject(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Update Data
     * Object on Syncano
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t Syncano object to be updated
     * @param updateGivenObject Should update fields in passed object, or only return the new
     *                          created object
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> updateObject(Syncano syncano, T t, boolean updateGivenObject) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.updateObject(t, updateGivenObject).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get a list of
     * Data Objects associated with a given Class
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param dataEndpoint name of endpoint to be executed
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> getObjectsDataEndpoint(Syncano syncano, Class<T> clazz, String dataEndpoint) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getObjectsDataEndpoint(clazz, dataEndpoint).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Renders a
     * template with objects got from given request
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param requestGet request that will be used to get objects
     * @param templateName name of a template that will be used to render result
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<String> getObjectsWithTemplate(Syncano syncano, RequestGet requestGet, String templateName){
        return Observable.create((OnSubscribe<String>) subscriber ->
                syncano.getObjectsWithTemplate(requestGet, templateName).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // AbstractUser Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Create a new
     * custom User.
     *
     * To be able to register Users you'll have to create an API Key that has allow_user_create flag
     * set to true.
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param t User to be registered
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> register(T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.register(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will fetch the user
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param t User to be fetched
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> fetch(T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.fetch(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will fetch the user
     * profile
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param u user profile to be fetched
     * @param <T> type of Profile and items emitted by Observable
     * @param <U> type of Abstract user object
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends Profile, U extends AbstractUser<? extends T>> Observable<T> fetchProfile(U u) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                u.fetchProfile(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will authenticate a
     * User
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param t user to be authenticated
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> login(T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                t.login(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will authenticate a
     * User from social networks
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param user user to be authenticated
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> loginSocialUser(T user) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                user.loginSocialUser(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get details of
     * previously created User using an id as reference
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param id id used as reference
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> getUser(Syncano syncano, Class<T> clazz, int id){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.getUser(clazz, id).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get details of
     * previously created User using an id as reference
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param id id used as reference
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<User> getUser(Syncano syncano, int id) {
        return Observable.create((OnSubscribe<User>) subscriber ->
                syncano.getUser(id).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get details of
     * previously created User
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> fetchCurrentUser(Syncano syncano, Class<T> clazz){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.fetchCurrentUser(clazz).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get details of
     * previously created User
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param user user to be fetched
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> fetchCurrentUser(Syncano syncano, T user) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.fetchCurrentUser(user).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Create a new
     * custom User
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t user to be created
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> registerUser(Syncano syncano, T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.registerUser(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Update a User
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param user user to be updated
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<User> updateUser(Syncano syncano, User user) {
        return Observable.create((OnSubscribe<User>) subscriber ->
                syncano.updateUser(user).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Update a
     * custom User
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t object to be updated
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> updateCustomUser(Syncano syncano, T t) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.updateCustomUser(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Authenticate a
     * User
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t user to log in
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> loginUser(Syncano syncano, T t) {
        t.login();
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.loginUser(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Authenticate a
     * User by username and password
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param username user name from registration.
     * @param password user password.
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<User> loginUser(Syncano syncano, String username, String password) {
        return Observable.create((OnSubscribe<User>) subscriber ->
                syncano.loginUser(username, password).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Authenticate a
     * custom User by username and password
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param username user name from registration.
     * @param password user password.
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> loginUser(Syncano syncano, Class<T> clazz, String username, String password) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.loginUser(clazz, username, password));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will AAuthenticate
     * a social user
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param clazz Syncano class that will be requested
     * @param socialAuthBackend social network authentication backend
     * @param authToken authentication token.
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> loginSocialUser(Syncano syncano, Class<T> clazz, SocialAuthBackend socialAuthBackend, String authToken) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.loginSocialUser(clazz, socialAuthBackend, authToken));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will AAuthenticate
     * a social user
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param socialAuthBackend social network authentication backend
     * @param authToken authentication token.
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<User> loginSocialUser(Syncano syncano, SocialAuthBackend socialAuthBackend, String authToken){
        return Observable.create((OnSubscribe<User>) subscriber ->
                syncano.loginSocialUser(socialAuthBackend, authToken).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will AAuthenticate
     * a social user
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param t user to log in
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends AbstractUser> Observable<T> loginSocialUser(Syncano syncano, T t){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.loginSocialUser(t).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RequestBuilder and Where Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Load data with
     * a predefined Where instance
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param where instance load the objects
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> get(Where<T> where) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                where.get(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Load data with
     * a predefined RequestBuilder instance
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param clazz Syncano class that will be requested
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> get(Class<T> clazz){
        return get(Syncano.please(clazz));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Load data with
     * a predefined RequestBuilder instance. Use it when you want to apply limits, filters,
     * ordering...
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param builder instance load the objects
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> get(RequestBuilder<T> builder) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                builder.get(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will load one
     * object using an id as reference
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param clazz Syncano class that will be requested.
     * @param id id of syncano object
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<T> get(Class<T> clazz, int id){
        return Observable.create((OnSubscribe<T>) subscriber ->
                Syncano.please(clazz).get(id, new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will load the count
     * estimation of a current object.
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param clazz Syncano class that will be requested.
     * @param <T> the type of the Data Object that will be counted
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T extends SyncanoObject> Observable<Integer> getCountEstimation(Class<T> clazz) {
        return Observable.create((OnSubscribe<Integer>) subscriber ->
                Syncano.please(clazz).getCountEstimation(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ScriptEndpoint Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will run a ScriptEndpoint
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param se Endpoint that will be executed
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> run(ScriptEndpoint se) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                se.run(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will run a
     * ScriptEndpoint using an json as payload
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param se endpoint that will be executed
     * @param payload payload to send
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> run(ScriptEndpoint se, JsonObject payload) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                se.run(payload, new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will run a
     * ScriptEndpoint. Use this approach when you want to parse the response by yourself
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>Ø@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param se endpoint that will be executed
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<String> runCustomResponse(ScriptEndpoint se) {
        return Observable.create((OnSubscribe<String>) subscriber ->
                se.runCustomResponse(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will run a
     * ScriptEndpoint. Use this approach when you want any serialized custom response
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param se endpoint that will be executed
     * @param clazz Syncano class that will be requested.
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T> Observable<T> runCustomResponse(ScriptEndpoint se, Class<T> clazz) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                se.runCustomResponse(clazz, new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will run a
     * ScriptEndpoint. Use this approach when you want any serialized custom response and
     * pass any payload as parameter
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param se endpoint that will be executed
     * @param clazz Syncano class that will be requested.
     * @param payload payload to send
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T> Observable<T> runCustomResponse(ScriptEndpoint se, Class<T> clazz, JsonObject payload) {
        return Observable.create((OnSubscribe<T>) subscriber ->
                se.runCustomResponse(clazz, payload, new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param scriptEndpoint ScriptEndpoint to run
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<String> runScriptEndpointCustomResponse(Syncano syncano, ScriptEndpoint scriptEndpoint){
        return Observable.create((OnSubscribe<String>) subscriber ->
                syncano.runScriptEndpointCustomResponse(scriptEndpoint).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param scriptEndpoint ScriptEndpoint to run
     * @param payload params to pass to ScriptEndpoint
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<String> runScriptEndpointCustomResponse(Syncano syncano, ScriptEndpoint scriptEndpoint, JsonObject payload){
        return Observable.create((OnSubscribe<String>) subscriber ->
                syncano.runScriptEndpointCustomResponse(scriptEndpoint, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint with custom responses
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param scriptEndpoint ScriptEndpoint to run
     * @param clazz Syncano class that will be requested.
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T> Observable<T> runScriptEndpointCustomResponse(Syncano syncano, ScriptEndpoint scriptEndpoint, Class<T> clazz){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.runScriptEndpointCustomResponse(scriptEndpoint, clazz).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint with custom responses
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param scriptEndpoint ScriptEndpoint to run
     * @param clazz Syncano class that will be requested.
     * @param payload params to pass to ScriptEndpoint
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T> Observable<T>  runScriptEndpointCustomResponse(Syncano syncano, ScriptEndpoint scriptEndpoint, Class<T> clazz, JsonObject payload){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.runScriptEndpointCustomResponse(scriptEndpoint, clazz, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint with custom responses
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param name name of ScriptEndpoint
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<String> runScriptEndpointCustomResponse(Syncano syncano, String name){
        return Observable.create((OnSubscribe<String>) subscriber ->
                syncano.runScriptEndpointCustomResponse(name).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint with custom responses
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param name name of ScriptEndpoint to run
     * @param payload params to pass to ScriptEndpoint
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<String> runScriptEndpointCustomResponse(Syncano syncano, String name, JsonObject payload){
        return Observable.create((OnSubscribe<String>) subscriber ->
                syncano.runScriptEndpointCustomResponse(name, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint with custom responses
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param name name of ScriptEndpoint to run
     * @param clazz Syncano class that will be requested.
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T> Observable<T> runScriptEndpointCustomResponse(Syncano syncano, String name, Class<T> clazz){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.runScriptEndpointCustomResponse(name, clazz).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint with custom responses
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param name name of ScriptEndpoint to run
     * @param clazz Syncano class that will be requested.
     * @param payload params to pass to ScriptEndpoint
     * @param <T> the type of the items that this Observable emits
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static <T> Observable<T>  runScriptEndpointCustomResponse(Syncano syncano, String name, Class<T> clazz, JsonObject payload){
        return Observable.create((OnSubscribe<T>) subscriber ->
                syncano.runScriptEndpointCustomResponse(name, clazz, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param scriptEndpoint ScriptEndpoint to run
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScriptEndpoint(Syncano syncano, ScriptEndpoint scriptEndpoint) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.runScriptEndpoint(scriptEndpoint).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param scriptEndpoint ScriptEndpoint to run
     * @param payload params to pass to ScriptEndpoint
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScriptEndpoint(Syncano syncano, ScriptEndpoint scriptEndpoint, JsonObject payload) {
        return Observable.create((OnSubscribe<Trace>) subscriber -> syncano.
                runScriptEndpoint(scriptEndpoint, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param name name of ScriptEndpoint to run
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScriptEndpoint(Syncano syncano, String name) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.runScriptEndpoint(name).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run a
     * ScriptEndpoint
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param name name of ScriptEndpoint to run
     * @param payload params to pass to ScriptEndpoint
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScriptEndpoint(Syncano syncano, String name, JsonObject payload) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.runScriptEndpoint(name, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Script Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run Script
     * asynchronously
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param script script to run
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> run(Script script){
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                script.run(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run Script
     * asynchronously
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param script script to run
     * @param payload params params to pass to Script
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> run(Script script, JsonObject payload){
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                script.run(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run Script
     * asynchronously
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param id id of syncano script to run
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScript(Syncano syncano, int id){
        return Observable.create((OnSubscribe<Trace>)
                subscriber -> syncano.runScript(id).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run Script
     * asynchronously
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param id id of syncano script to run
     * @param payload params params to pass to Script
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScript(Syncano syncano, int id, JsonObject payload){
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.runScript(id, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run Script
     * asynchronously
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param script syncano script to run
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScript(Syncano syncano, Script script){
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.runScript(script).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Run Script
     * asynchronously
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param script syncano script to run
     * @param payload params params to pass to Script
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> runScript(Syncano syncano, Script script, JsonObject payload){
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.runScript(script, payload).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Trace Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will fetch an trace
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param trace trace to be fetched
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> fetch(Trace trace) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                trace.fetch(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get trace,
     * result of Script execution. Refreshes values in given trace object.
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param trace object to be loaded
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> getTrace(Syncano syncano, Trace trace) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.getTrace(trace).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get trace,
     * result of Script execution. Refreshes values in given trace object.
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param scriptId id of script executed
     * @param traceId id of trace to be loaded
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Trace> getTrace(Syncano syncano, int scriptId, int traceId) {
        return Observable.create((OnSubscribe<Trace>) subscriber ->
                syncano.getTrace(scriptId, traceId).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Channel Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will start a
     * channel connection. When the returned observable was be unsubscribed the channel connection will
     * be stopped
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param channelName name of channel to be connected
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Notification> startChannelConnection(Syncano syncano, String channelName){
        ChannelConnection channelConnection = new ChannelConnection(syncano);
        return Observable.create((OnSubscribe<Notification>) subscriber -> {
            RxChannelConnectionListener listener = new RxChannelConnectionListener(subscriber);
            channelConnection.setChannelConnectionListener(listener);
            channelConnection.start(channelName);
            subscriber.add(Subscriptions.create(channelConnection::stop));
        });
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will start a
     * channel connection. When the returned observable was be unsubscribed the channel connection will
     * be stopped
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param channelName name of channel to be connected
     * @param roomName name of room to connect
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Notification> startChannelConnection(Syncano syncano, String channelName, String roomName){
        ChannelConnection channelConnection = new ChannelConnection(syncano);
        return Observable.create((OnSubscribe<Notification>) subscriber -> {
            RxChannelConnectionListener listener = new RxChannelConnectionListener(subscriber);
            channelConnection.setChannelConnectionListener(listener);
            subscriber.add(Subscriptions.create(channelConnection::stop));
        });
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will start a
     * channel connection. When the returned observable was be unsubscribed the channel connection will
     * be stopped
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param channelName name of channel to be connected
     * @param roomName name of room to connect
     * @param lastId last notification id
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Notification> startChannelConnection(Syncano syncano, String channelName, String roomName, int lastId){
        ChannelConnection channelConnection = new ChannelConnection(syncano);
        return Observable.create((OnSubscribe<Notification>) subscriber -> {
            RxChannelConnectionListener listener = new RxChannelConnectionListener(subscriber);
            channelConnection.setChannelConnectionListener(listener);
            channelConnection.start(channelName, roomName, lastId);
            subscriber.add(Subscriptions.create(channelConnection::stop));
        });
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Publish custom
     * message
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param channelName name of channel to be connected
     * @param notification notification to be published
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Notification> publishOnChannel(Syncano syncano, String channelName, Notification notification) {
        return Observable.create((OnSubscribe<Notification>) subscriber ->
                syncano.publishOnChannel(channelName, notification).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get a list of
     * Notifications
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param channelName name of channel to be connected
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Notification> getChannelHistory(Syncano syncano, String channelName) {
        return Observable.create((OnSubscribe<Notification>) subscriber ->
                syncano.getChannelsHistory(channelName).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will Get a list of
     * Notifications
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param channelName name of channel to be connected
     * @param roomName name of room to connect
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<Notification> getChannelHistory(Syncano syncano, String channelName, String roomName) {
        return Observable.create((OnSubscribe<Notification>) subscriber ->
                syncano.getChannelsHistory(channelName).sendAsync(new RxSyncanoListCallback<>(subscriber)));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // PushDevice Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will register a
     * push device to send and receive push notifications
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param pushDevice device to be registered
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<PushDevice> registerPushDevice(Syncano syncano, PushDevice pushDevice) {
        return Observable.create((OnSubscribe<PushDevice>) subscriber ->
                syncano.registerPushDevice(pushDevice).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    /**
     * Generate the Observable that, when a Subscriber subscribes to it, will delete a push
     * device
     *
     * <dl>
     *  <dt><b>Scheduler:</b></dt>
     *  <dd>{@code create} does not operate by default on a particular Scheduler.</dd>
     * </dl>
     *
     * @param syncano Syncano instance used in this operation
     * @param pushDevice device to be deleted
     * @return an Observable that, when a Subscriber subscribes to it, will execute the
     * specified function
     */
    public static Observable<PushDevice> deletePushDevice(Syncano syncano, PushDevice pushDevice){
        return Observable.create((OnSubscribe<PushDevice>) subscriber ->
                syncano.deletePushDevice(pushDevice).sendAsync(new RxSyncanoCallback<>(subscriber)));
    }

    private SyncanoObservable(){
        // disable instances
    }
}
