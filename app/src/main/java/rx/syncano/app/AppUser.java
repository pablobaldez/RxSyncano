package rx.syncano.app;

import com.syncano.library.data.AbstractUser;

/**
 * Created by pablobaldez on 4/25/16.
 */
public class AppUser extends AbstractUser<AppProfile> {

    public AppUser(String username, String password){
        super(username, password);
    }

}
