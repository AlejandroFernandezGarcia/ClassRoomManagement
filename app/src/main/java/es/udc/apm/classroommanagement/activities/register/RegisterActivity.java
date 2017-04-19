package es.udc.apm.classroommanagement.activities.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import es.udc.apm.classroommanagement.GPSLocationActivity;
import es.udc.apm.classroommanagement.LoginActivity;
import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.model.Role;
import es.udc.apm.classroommanagement.services.RoleService;
import es.udc.apm.classroommanagement.services.UserService;

import static es.udc.apm.classroommanagement.utils.Utils.isOnline;
import static es.udc.apm.classroommanagement.utils.Utils.showToast;

public class RegisterActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static String userGoogleID;
    private static String userMail;
    private static String name;
    private static String surname;

    private RoleService roleService;
    private UserService userService;

    //Form elements for validation
    private Button registryButton;
    EditText editName, editSurname;
    private Spinner roleSpinner;

    // Confirmation of new user
    private boolean confirmed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        userGoogleID = intent.getStringExtra(LoginActivity.USER_GOOGLE_ID);
        userMail = intent.getStringExtra(LoginActivity.USER_MAIL);
        name = intent.getStringExtra(LoginActivity.USER_NAME);
        surname = intent.getStringExtra(LoginActivity.USER_SURNAME);

        roleService = new RoleService();
        userService = new UserService();

        addItemsToSpinner();

        editName = (EditText) findViewById(R.id.name_text);
        editSurname = (EditText) findViewById(R.id.surname_text);

        editName.setText(name);
        editSurname.setText(surname);

        registryButton = (Button) findViewById(R.id.register_button);

        registryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onRegisterButtonClick();
            }
        });
    }

    private void addItemsToSpinner() {
        roleSpinner = (Spinner) findViewById(R.id.role_spinner);
        List<Role> roles = null;

        if (!isOnline(getApplicationContext())) {
            showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }

        try {
            if (roleService != null) {
                roles = roleService.execute().get();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            showToast(getApplicationContext(), getString(R.string.connection_error));
            return;
        }

        ArrayAdapter<Role> dataAdapter = new ArrayAdapter<Role>(this,
                android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(dataAdapter);

    }

    private void onRegisterButtonClick() {
        boolean validated = true;
        final String name, surname;
        final Role selectedRole;
        short roleID;

        if (!isOnline(getApplicationContext())) {
            showToast(getApplicationContext(), getString(R.string.no_internet));
            return;
        }

        name = editName.getText().toString().trim();
        surname = editSurname.getText().toString().trim();
        selectedRole = (Role) roleSpinner.getSelectedItem();

        if (name.length() == 0) {
            validated = false;
            editName.setError(getString(R.string.no_name_registered));
            editName.requestFocus();
        }
        if (surname.length() == 0) {
            validated = false;
            editSurname.setError(getString(R.string.no_surname_registered));
            if (name.length() > 0) {
                editSurname.requestFocus();
            }
        }

        if (validated) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);
            alertDialogBuilder.setTitle(getString(R.string.confirm_registry));
            alertDialogBuilder.setMessage(getString(R.string.confirmation_message, name, surname, selectedRole.getRoleName())).setCancelable(false)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, save user in database
                            try {
                                if (userService != null) {
                                    userService.insertUser(userGoogleID, name, surname, userMail, selectedRole.getId());
                                    confirmed = true;
                                } else {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                confirmed = false;
                                showToast(getApplicationContext(), getString(R.string.connection_error));
                                return;
                            }
                            dialog.cancel();
                            if (confirmed) {
                                Intent gpsLocationIntent = new Intent(RegisterActivity.this, GPSLocationActivity.class);
                                startActivity(gpsLocationIntent);
                            }

                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

}
