package rx.syncano.app.models;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

/**
 * Created by pablobaldez on 4/27/16.
 */
@SyncanoClass(name = TestModel.CLASS_NAME)
public class TestModel extends SyncanoObject {

    public static final String CLASS_NAME = "test_model";

    @SyncanoField(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
