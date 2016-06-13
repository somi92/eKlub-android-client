package rs.fon.eklubmobile.listeners;

import org.json.JSONObject;

/**
 * Created by milos on 4/23/16.
 */
public interface EKlubEventListener<T> {

    void onTaskStarted();
    void onDataReceived(T data);
    void onTaskFinished();
    void onNotificationReceived(String message);
}
