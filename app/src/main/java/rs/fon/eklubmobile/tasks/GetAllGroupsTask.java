package rs.fon.eklubmobile.tasks;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import rs.fon.eklubmobile.listeners.EKlubEventListener;

/**
 * Created by milos on 4/22/16.
 */
public class GetAllGroupsTask extends AsyncTask<String, Integer, Boolean> {

    private EKlubEventListener mListener;
    private String mResult;

    public GetAllGroupsTask(EKlubEventListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.onTaskStarted();
    }

    @Override
    protected Boolean doInBackground(String... url) {
        String resourceUrl = "http://" + url[0] + "/groups";
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) (new URL(resourceUrl).openConnection());
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            int statusCode = connection.getResponseCode();
            if(statusCode != HttpURLConnection.HTTP_OK) {
                mResult = "HTTP greška, status kod: " + statusCode;
                return false;
            }
            InputStream inStream = new BufferedInputStream(connection.getInputStream());
            mResult = getResponseText(inStream);
        } catch (SocketTimeoutException e) {
            mResult = "Greška! Konekcija je istekla: "+e.getMessage();
            return false;
        } catch (IOException e) {
            mResult = "Greška! I/O sistem ne može preuzeti podatke: "+e.getMessage();
            return false;
        }
        catch (Exception e) {
            mResult = "Greška! Sistem ne može preuzeti podatke: "+e.getMessage();
            return false;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean isSuccessful) {
        try {
            if(isSuccessful) {
                mListener.onDataReceived(new JSONObject(mResult));
            } else {
                mListener.onNotificationReceived(mResult);
            }
        } catch (JSONException e) {
            mListener.onNotificationReceived("Greška! JSON parsiranje neuspešno.");
        } catch (Exception e) {
            mListener.onNotificationReceived("Greška! Prikaz podataka neuspešan.");
        }
        mListener.onTaskFinished();
    }

    private static String getResponseText(InputStream in) {
        Scanner scan = new Scanner(in);
        String s = scan.useDelimiter("\\A").next();
        scan.close();
        return s;
    }
}
