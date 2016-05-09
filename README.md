RxSyncano
===================

Reactive framework based on **RxJava** to work with **Syncano** features.

Features
----------
SyncanoObservable class covers all Syncano features, wrapping calls and returning Observables< T >, where T is the correspondent generic value to Syncano methods.

 - Complete SyncanoObject CRUD
 -  Support gets with Where (for custom critetias), RequestBuilder (for fields filtering and pagination) and get by id
 - Channel connectios
 - Channel publications
 - Registers and deletes devices for pushs
 - Run Scripts and ScriptEndpoints
 - Fetching Traces
 - Login, social login and registration for custom user classes (extends AbstractUser) and the default syncano user class
 - All calls are asynchronous
 - Support for default Syncano instance or some custom Syncano instance
 - Error handling with RxSyncanoException

Usage
----------

#### Getting Objects
You can also get by id, get using Where (to custom criterias), get using BuildRequest (to filter fields and paging). All of them using the current or a custom Syncano instance.

**Before:**

	Syncano.please(MyClass.class).get(
    	new SyncanoCallback<List< MyClass >>() {
        	@Override
	     	public void success(Response<List<MyClass>> response, List<MyClass> result) {
                
       		}
       		@Override
       		public void failure(Response<List<MyClass>> response) {
       		}
	});

**After:**

	SyncanoObservable.get(MyClass.class).subscribe();
	
-------------

#### Saving Objects
**Before:**

    MyClass myClass = new MyClass();
    myClass.save(new SyncanoCallback<SyncanoObject>() {
        @Override
        public void success(Response<SyncanoObject> response, SyncanoObject result) {
        }
        @Override
        public void failure(Response<SyncanoObject> response) {
        }
    });

**After:**

Delete an object follows the same logic

	MyClass myClass = new MyClass();
	SyncanoObservable.save(myClass).subscribe();

-------------

#### Channels connection

**Before:**
	
	// connecting a channel
	ChannelConnectionListener listener = createSomeListener();
    ChannelConnection connection = new ChannelConnection(Syncano.getInstance(), listener);
    connection.start("channelName", "roomName", 1);
    ...
    // disconnecting
    connection.stop();

**After:**

	// connecting a channel
    Subscriber<Notification> subscriber = createSomeSubscriber();
    SyncanoObservable.startChannelConnection(Syncano.getInstance(), "channelName", "roomName", 1).subscribe(subscriber);
    ...
    // disconnecting
    subscriber.unsubscribe();

#### Channels publishment
**Before:**

	  Notification notification = createSomeNotification();
      RequestPost<Notification> requestPost = Syncano.getInstance().publishOnChannel("channelName", notification);
	  requestPost.sendAsync(new SyncanoCallback<Notification>() {
            @Override
            public void success(Response<Notification> response, Notification result) {
                
            }
            @Override
            public void failure(Response<Notification> response) {
            }
        });

**After:**

	SyncanoObservable.publishOnChannel(Syncano.getInstance(), "channelName", notification).subscribe();

#### Scripts and ScriptEndpoints
Scripts and script endpoints have support for all run and runCustomResponse variations. Have support too for a custom Syncano instance.

#### Running a script (not recommended by Syncano)
**Before:**

	Script script = new Script(1);
    JsonObject payload = new JsonObject();
    script.run(new SyncanoCallback<Trace>() {
	    Override
        public void success(Response<Trace> response, Trace result) {
        }
        @Override
        public void failure(Response<Trace> response) {
        }
    }, payload);

**After:**

	Script script = new Script(1);
    JsonObject payload = new JsonObject();
    // returns an Observable<Trace>
    SyncanoObservable.run(script).subscribe();

#### Running ScriptEndpoint
**Before:**

	ScriptEndpoint scriptEndpoint = new ScriptEndpoint("name");
    scriptEndpoint.runCustomResponse(MyClass.class, new SyncanoCallback<MyClass>() {
	    @Override
        public void success(Response<MyClass> response, MyClass result) {
        }
        @Override
        public void failure(Response<MyClass> response) {
        }
    });

**After:**

	SyncanoObservable.runCustomResponse(scriptEndpoint, MyClass.class).subscribe();

Error handling
----------
All syncano erros will be wrapped to RxSyncanoException and sent to subscriber.onError(Exception) method. 

	exception.getMessage(); // == response.getError
    exception.getResultCode(); // == response.getResultCode 
    exception.getHttpCode(); // == response.getHttpResultCode 
    exception.getHttpMessage(); // == response.getHttpReasonPhrase

Threads
----------
All calls are asynchronous and works with Syncano callbacks, but is strongly recommended to use rx schedulers to receive the responses on worker threads. Take a look in [RxAndroid](https://github.com/ReactiveX/RxAndroid) to work with Android main thread

Installation
----------
It's avaialable on jcenter. Just put it as your dependency in build.gradle file

	compile 'com.pablobaldez:rxsyncano:1.0.0'

Acknowledgment
----------
Thanks for Syncano and all syncano comunity to give me the necessary support for implement it. I believe this library will be very powerful and popular in a short period of time. And thanks too for [RxParse](https://github.com/yongjhih/RxParse) to inpire me as a library to make reative support for a Baas framework.

License
----------
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
