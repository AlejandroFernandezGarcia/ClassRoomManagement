package es.udc.apm.classroommanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IndoorLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button change_mode_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_location);

        change_mode_btn = (Button) findViewById(R.id.change_outdoor_btn);
        change_mode_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id){
            case R.id.change_outdoor_btn:
                Intent gpsLocationIntent = new Intent(this, GPSLocationActivity.class);
                startActivity(gpsLocationIntent);
                break;
        }
    }
}
