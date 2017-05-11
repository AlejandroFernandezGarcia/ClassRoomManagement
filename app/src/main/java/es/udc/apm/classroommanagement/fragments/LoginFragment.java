package es.udc.apm.classroommanagement.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import es.udc.apm.classroommanagement.MainActivity;
import es.udc.apm.classroommanagement.R;
import es.udc.apm.classroommanagement.model.User;
import es.udc.apm.classroommanagement.services.UserService;

import static es.udc.apm.classroommanagement.utils.Constants.USER_GOOGLE_ID;
import static es.udc.apm.classroommanagement.utils.Constants.USER_MAIL;
import static es.udc.apm.classroommanagement.utils.Constants.USER_NAME;
import static es.udc.apm.classroommanagement.utils.Constants.USER_SURNAME;
import static es.udc.apm.classroommanagement.utils.Utils.isOnline;
import static es.udc.apm.classroommanagement.utils.Utils.logError;
import static es.udc.apm.classroommanagement.utils.Utils.logInfo;
import static es.udc.apm.classroommanagement.utils.Utils.showToast;

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 7;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
    private UserService userService;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        userService = new UserService();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn = (SignInButton) view.findViewById(R.id.btn_sign_in);
        // Customizing G+ button
        //btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        //btnSignIn.setScopes(gso.getScopeArray());

        btnSignIn.setSize(SignInButton.SIZE_STANDARD);// listener for sign in button
        btnSignIn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
        }
    }

    private void signIn() {
        if (!isOnline(getActivity().getApplicationContext())) {
            showToast(getActivity().getApplicationContext(), getString(R.string.no_internet));
        } else {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        logInfo(this, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            loginSuccess(acct);
        } else {
            // Signed out, show unauthenticated UI.
            loginFail();
        }
    }


    /* @Override
     public void onStart() {
         super.onStart();

         if (!isOnline(getApplicationContext())) {
             showToast(getApplicationContext(), getString(R.string.no_internet));
         } else {

             OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
             if (opr.isDone()) {
                 // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                 // and the GoogleSignInResult will be available instantly.
                 Log.d(TAG, "Got cached sign-in");
                 GoogleSignInResult result = opr.get();
                 handleSignInResult(result);
             } else {
                 // If the user has not previously signed in on this device or the sign-in has expired,
                 // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                 // single sign-on will occur in this branch.
                 showProgressDialog();
                 opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                     @Override
                     public void onResult(GoogleSignInResult googleSignInResult) {
                         hideProgressDialog();
                         handleSignInResult(googleSignInResult);
                     }
                 });
             }
         }
     }
 */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        logInfo(this, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void loginSuccess(GoogleSignInAccount acct) {
        logInfo(this, "Login correcto");

        String name = acct.getGivenName();
        String surname = acct.getFamilyName();
        String email = acct.getEmail();
        String googleID = acct.getId();

        //region Store in session variables and set name to lateral menu
        TextView txtView = (TextView) getActivity().findViewById(R.id.user_name);
        txtView.setText(name);
        ((MainActivity) getActivity()).setUserName(name.length() != 0 ? name : getString(R.string.user_without_name));
        ((MainActivity) getActivity()).setGoogleId(googleID);

        //endregion

        User user = null;

        try {
            if (userService != null) {
                user = userService.getUser(googleID);
                if(user != null){
                    name = user.getName();
                    surname = user.getLastName();
                    email = user.getMail();
                    googleID=user.getGoogleId();
                }
            } else {
                throw new Exception("Error servicio usuario");
            }
        } catch (Exception e) {
            logError(this, e);
            showToast(getActivity().getApplicationContext(), getString(R.string.connection_error));
            return;
        }
        //Data to share with the new fragment
        Bundle bundle = new Bundle();
        bundle.putString(USER_GOOGLE_ID, googleID);
        bundle.putString(USER_MAIL, email);
        bundle.putString(USER_NAME, name);
        bundle.putString(USER_SURNAME, surname);
        Fragment fragment = null;

        if (user != null && user.getGoogleId() != null) {
            logInfo(this, "User registered");
            fragment = new GPSLocationFragment();
        } else {
            logInfo(this, "User not registered, creating db row");
            fragment = new RegisterFragment();
            fragment.setArguments(bundle);
        }

        //Logout from google
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private void loginFail() {
        logInfo(this, "Login incorrecto");
    }
}

