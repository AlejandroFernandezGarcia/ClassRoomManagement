package es.udc.apm.classroommanagement.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import es.udc.apm.classroommanagement.MainActivity;
import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.model.Role;
import es.udc.apm.classroommanagement.services.RoleService;
import es.udc.apm.classroommanagement.services.UserService;
import es.udc.apm.classroommanagement.utils.Constants;

import static es.udc.apm.classroommanagement.utils.Utils.isOnline;
import static es.udc.apm.classroommanagement.utils.Utils.logError;
import static es.udc.apm.classroommanagement.utils.Utils.showToast;

public class RegisterFragment extends Fragment {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).showLateralMenu(true);
    }


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        //FIXME Si se llega aqui a traves del menu estos arguments no existen
        userGoogleID = getArguments().getString(Constants.USER_GOOGLE_ID);
        userMail = getArguments().getString(Constants.USER_MAIL);
        name = getArguments().getString(Constants.USER_NAME);
        surname = getArguments().getString(Constants.USER_SURNAME);

        roleService = new RoleService();
        userService = new UserService();

        roleSpinner = (Spinner) view.findViewById(R.id.role_spinner);

        addItemsToSpinner();

        editName = (EditText) view.findViewById(R.id.name_text);
        editSurname = (EditText) view.findViewById(R.id.surname_text);

        editName.setText(name);
        editSurname.setText(surname);


        registryButton = (Button) view.findViewById(R.id.register_button);

        registryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onRegisterButtonClick();
            }
        });

        return view;
    }

    private void addItemsToSpinner() {
        List<Role> roles = null;

        if (!isOnline(getActivity().getApplicationContext())) {
            showToast(getActivity().getApplicationContext(), getString(R.string.no_internet));
            return;
        }

        try {
            if (roleService != null) {
                roles = roleService.execute().get();
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            logError(this,e);
            showToast(getActivity().getApplicationContext(), getString(R.string.connection_error));
            return;
        }

        ArrayAdapter<Role> dataAdapter = new ArrayAdapter<Role>(this.getActivity(), android.R.layout.simple_spinner_item, roles);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(dataAdapter);

    }

    private void onRegisterButtonClick() {
        boolean validated = true;
        final String name, surname;
        final Role selectedRole;
        short roleID;

        if (!isOnline(getActivity().getApplicationContext())) {
            showToast(getActivity().getApplicationContext(), getString(R.string.no_internet));
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.confirm_registry));
            alertDialogBuilder.setMessage(getString(R.string.confirmation_message, name, surname, selectedRole.getRoleName())).setCancelable(false)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, save user in database
                            try {
                                if (userService != null) {
                                    //TODO Comprobar si existe, si existe actualiza sino inserta
                                    userService.insertUser(userGoogleID, name, surname, userMail, selectedRole.getId());
                                    confirmed = true;
                                } else {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                logError(this,e);
                                confirmed = false;
                                showToast(getActivity().getApplicationContext(), getString(R.string.connection_error));
                                return;
                            }
                            dialog.cancel();
                            if (confirmed) {
//                                Intent gpsLocationIntent = new Intent(RegisterFragment.this, GPSLocationFragment.class);
//                                startActivity(gpsLocationIntent);
                                //TODO Gps fragment
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
