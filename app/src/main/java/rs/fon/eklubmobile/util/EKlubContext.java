package rs.fon.eklubmobile.util;

import android.app.Application;

/**
 * Created by milos on 8/27/16.
 */
public class EKlubContext extends Application {

    private String mAccessToken;

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }
}
