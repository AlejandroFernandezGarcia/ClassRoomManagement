package es.udc.apm.classroommanagement;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import es.udc.apm.classroommanagement.fragments.GPSLocationFragment;
import es.udc.apm.classroommanagement.fragments.IndoorLocationFragment;
import es.udc.apm.classroommanagement.fragments.LoginFragment;
import es.udc.apm.classroommanagement.fragments.RegisterFragment;

import static es.udc.apm.classroommanagement.utils.Utils.logInfo;

public class MainActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar appbar;
    private String userName;
    private String googleId;

    //region Getters and setters

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        showLateralMenu(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navView = (NavigationView) findViewById(R.id.navview);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new LoginFragment())
                .commit();

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Fragment fragment = null;
                        boolean changeFragment = false;

                        switch (menuItem.getItemId()) {
                            case R.id.GpsLocation:
                                logInfo(this, "Menu gps location option clicked");
                                fragment = new GPSLocationFragment();
                                changeFragment = true;
                                getSupportActionBar().setTitle(menuItem.getTitle());
                                break;
                            case R.id.IndoorLocation:
                                logInfo(this, "Menu indoor location option clicked");
                                fragment = new IndoorLocationFragment();
                                changeFragment = true;
                                getSupportActionBar().setTitle(menuItem.getTitle());
                                break;
                            case R.id.UpdateProfile:
                                logInfo(this, "Menu update profile option clicked");
                                fragment = new RegisterFragment();
                                changeFragment = true;
                                getSupportActionBar().setTitle(menuItem.getTitle());
                                break;
                            case R.id.exit:
                                logInfo(this, "Menu exit option clicked");
                                closeApp();
                                break;
                        }
                        if (changeFragment) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();
                        }

                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void closeApp() {
        //Different code path in function of the sdk version.
        //FinishAffinity need api level >= 16
        if (android.os.Build.VERSION.SDK_INT < 16) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            this.finishAffinity();
        }
    }

    public void showLateralMenu(boolean show){
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
    }
}
