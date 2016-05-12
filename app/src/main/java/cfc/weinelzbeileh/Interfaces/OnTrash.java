package cfc.weinelzbeileh.Interfaces;

public interface OnTrash {

    void OnTrashAdded(String id, double latitude, double longitude);

    void OnTrashRemoved(String id);

}
