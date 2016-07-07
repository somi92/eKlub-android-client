package rs.fon.eklubmobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rs.fon.eklubmobile.R;

public class StartActivity extends AppCompatActivity {

    private Button mNewTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mNewTraining = (Button) findViewById(R.id.btnNewTraining);
        mNewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });
    }
}
