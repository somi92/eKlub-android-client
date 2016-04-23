package rs.fon.eklubmobile.listeners;

import org.json.JSONObject;

/**
 * Created by milos on 4/23/16.
 */
public interface EKlubEventListener {

    void onTaskStarted();
    void onDataReceived(JSONObject data);
    void onTaskFinished();
    void onNotificationReceived(String message);
}
