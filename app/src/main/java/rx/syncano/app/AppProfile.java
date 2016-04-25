package rx.syncano.app;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.Profile;
import com.syncano.library.data.SyncanoFile;

/**
 * Created by pablobaldez on 4/25/16.
 */
public class AppProfile extends Profile {
    @SyncanoField(name = "avatar")
    public SyncanoFile avatar;
}
