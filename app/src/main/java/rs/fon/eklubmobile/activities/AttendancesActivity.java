package rs.fon.eklubmobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import rs.fon.eklubmobile.R;

public class AttendancesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendances);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("groupId");

        Toast.makeText(this, "GroupId: " + id, Toast.LENGTH_LONG).show();
    }
}
