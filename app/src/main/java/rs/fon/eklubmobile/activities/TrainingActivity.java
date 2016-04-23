package rs.fon.eklubmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

import rs.fon.eklubmobile.R;
import rs.fon.eklubmobile.listeners.EKlubEventListener;

public class TrainingActivity extends AppCompatActivity implements EKlubEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
    }

    @Override
    public void onTaskStarted() {

    }

    @Override
    public void onDataReceived(JSONObject data) {

    }

    @Override
    public void onTaskFinished() {

    }

    @Override
    public void onNotificationReceived(String message) {

    }
}
