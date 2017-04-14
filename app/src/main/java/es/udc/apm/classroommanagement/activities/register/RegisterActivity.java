package es.udc.apm.classroommanagement.activities.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

import es.udc.apm.classroommanagement.GPSLocationActivity;
import es.udc.apm.classroommanagement.LoginActivity;
import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.model.Role;
import es.udc.apm.classroommanagement.services.RoleService;
import es.udc.apm.classroommanagement.utils.Utils;

public class RegisterActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static String userGoogleID;
    private static String userMail;

    private RoleService roleService;

    private Spinner roleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        userGoogleID = intent.getStringExtra(LoginActivity.USER_GOOGLE_ID);
        userMail = intent.getStringExtra(LoginActivity.USER_MAIL);

        roleService = new RoleService();

        addItemsToSpinner();

        final Button button = (Button) findViewById(R.id.register_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onRegisterButtonClick();
            }
        });
    }

    private void addItemsToSpinner() {
        roleSpinner = (Spinner) findViewById(R.id.role_spinner);
        List<Role> roles = null;
        try {
            if (roleService != null) {
                roles = roleService.execute().get();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            Utils.showToast(getApplicationContext(), getString(R.string.connection_error));
            return;
        }

        ArrayAdapter<Role> dataAdapter = new ArrayAdapter<Role>(this,
                android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(dataAdapter);

    }

    private void onRegisterButtonClick() {
        Intent gpsLocationIntent = new Intent(this, GPSLocationActivity.class);
        startActivity(gpsLocationIntent);
    }

}
