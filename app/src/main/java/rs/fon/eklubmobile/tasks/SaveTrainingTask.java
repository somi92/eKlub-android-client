package rs.fon.eklubmobile.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import rs.fon.eklubmobile.entities.Training;
import rs.fon.eklubmobile.listeners.EKlubEventListener;

/**
 * Created by milos on 4/22/16.
 */
public class SaveTrainingTask extends AsyncTask<String, Integer, Boolean> {

    private EKlubEventListener<String> mListener;
    private Training mTraining;
    private String mResult;

    public SaveTrainingTask(EKlubEventListener listener, Training training) {
        mListener = listener;
        mTraining = training;
    }

    @Override
    protected void onPreExecute() {
        mListener.onTaskStarted();
    }

    @Override
    protected Boolean doInBackground(String... url) {

        String resourceUrl = "http://" + url[0] + "/trainings";
        HttpURLConnection connection = null;

        try {

            String params = getJsonString(mTraining);
            Log.d("APP", "params: " + params);
            byte[] postData = params.getBytes("UTF-8");
            connection = (HttpURLConnection) (new URL(resourceUrl).openConnection());
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");

            DataOutputStream dao = new DataOutputStream(connection.getOutputStream());
            dao.write(postData);
            dao.flush();
            dao.close();

            int statusCode = connection.getResponseCode();
            if(statusCode != HttpURLConnection.HTTP_OK) {
                mResult = "HTTP greška, status kod: " + statusCode;
                return false;
            }
            InputStream inStream = new BufferedInputStream(connection.getInputStream());
            mResult = getResponseText(inStream);
            Log.d("APP", "mResult: " + mResult);

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            mResult = "Greška! Konekcija je istekla: "+e.getMessage();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            mResult = "Greška! I/O sistem ne može preuzeti podatke: "+e.getMessage();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
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
                mListener.onDataReceived(mResult);
            } else {
                mListener.onNotificationReceived(mResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("APP", "error: " + e.getMessage());
            mListener.onNotificationReceived("Greška! Prikaz podataka neuspešan: " + e.getMessage());
        }
        mListener.onTaskFinished();
    }

    private String getJsonString(Training training) {
        Gson gson = new Gson();
        return gson.toJson(training, Training.class);
    }

    private JSONObject getJsonObject(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        return obj;
    }

    private static String getResponseText(InputStream in) {
        Scanner scan = new Scanner(in);
        String s = scan.useDelimiter("\\A").next();
        scan.close();
        return s;
    }
}
