package es.udc.apm.classroommanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import es.udc.apm.classroommanagement.activities.register.RegisterActivity;
import es.udc.apm.classroommanagement.model.User;
import es.udc.apm.classroommanagement.services.UserService;

import static es.udc.apm.classroommanagement.utils.Utils.isOnline;
import static es.udc.apm.classroommanagement.utils.Utils.showToast;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    //Constants
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 7;
    public final static String USER_GOOGLE_ID = "es.udc.apm.classroommanagement.USER_GOOGLE_ID";
    public final static String USER_MAIL = "es.udc.apm.classroommanagement.USER_MAIL";
    public final static String USER_NAME = "es.udc.apm.classroommanagement.USER_NAME";
    public final static String USER_SURNAME = "es.udc.apm.classroommanagement.USER_SURNAME";

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton btnSignIn;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userService = new UserService();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);// listener for sign in button
        btnSignIn.setOnClickListener(this);
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
        if (!isOnline(getApplicationContext())) {
            showToast(getApplicationContext(), getString(R.string.no_internet));
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
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
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
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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
        Log.i(TAG, "Login correcto");

        String name = acct.getGivenName();
        String surname = acct.getFamilyName();
        String email = acct.getEmail();
        String googleID = acct.getId();
        User user = null;

        try {
            if (userService != null) {
                user = userService.getUser(googleID);
            } else {
                throw new Exception("Error servicio usuario");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            showToast(getApplicationContext(), getString(R.string.connection_error));
            return;
        }

        if (user != null && user.getGoogleId() != null) {
            Log.d(TAG, "Name: " + name + "Surname: " + surname + ", email: " + email
                    + ", Id: " + googleID);
            Intent gpsLocationIntent = new Intent(this, GPSLocationActivity.class);
            /*
            Como ya tenemos los datos de login podemos hacer un logout
             */
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
            startActivity(gpsLocationIntent);
        } else {
            Log.d(TAG, "Usuario no registrado: " + "Name: " + name + "Surname: " + surname + ", email: " + email
                    + ", Id: " + googleID);
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);
            registerIntent.putExtra(USER_GOOGLE_ID, googleID);
            registerIntent.putExtra(USER_MAIL, email);
            registerIntent.putExtra(USER_NAME, name);
            registerIntent.putExtra(USER_SURNAME, surname);
            startActivity(registerIntent);
        }
    }

    private void loginFail() {
        Log.i(TAG, "Login incorrecto");
    }
}
