package cfc.weinelzbeileh.interfaces;

import com.google.firebase.database.DataSnapshot;

public interface FirebaseUtilInterface {

    void OnAdd(DataSnapshot data);

    void OnRemove(DataSnapshot data);

}
