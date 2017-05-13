package es.udc.apm.classroommanagement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import es.udc.apm.classroommanagement.fragments.GPSLocationFragment;
import es.udc.apm.classroommanagement.fragments.IndoorLocationFragment;
import es.udc.apm.classroommanagement.fragments.LoginFragment;
import es.udc.apm.classroommanagement.fragments.RegisterFragment;

import static es.udc.apm.classroommanagement.utils.Utils.logInfo;

public class MainActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private String userName;
    private String googleId;
    private SmoothActionBarDrawerToggle drawerToggle;

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

        Toolbar appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new SmoothActionBarDrawerToggle(this, drawerLayout, appbar);
        drawerLayout.addDrawerListener(drawerToggle);

        showLateralMenu(false);

        NavigationView navView = (NavigationView) findViewById(R.id.navview);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new LoginFragment())
                .commit();

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {

                        //Fragment fragment = null;
                        //boolean changeFragment = false;

                        switch (menuItem.getItemId()) {
                            case R.id.GpsLocation:
                                logInfo(this, "Menu gps location option clicked");
                                drawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        Class fragmentClass = GPSLocationFragment.class;
                                        //changeFragment = true;
                                        drawerLayout.closeDrawers();
                                        Fragment fragment = null;
                                        try {
                                            fragment = (Fragment) fragmentClass.newInstance();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.content_frame, fragment)
                                                .commit();
                                        // Highlight the selected item has been done by NavigationView
                                        menuItem.setChecked(true);
                                        // Set action bar title
                                        setTitle(menuItem.getTitle());
                                    }
                                });

                                break;

                            case R.id.IndoorLocation:
                                logInfo(this, "Menu indoor location option clicked");

                                drawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        Class fragmentClass = IndoorLocationFragment.class;
                                        //changeFragment = true;
                                        drawerLayout.closeDrawers();
                                        Fragment fragment = null;
                                        try {
                                            fragment = (Fragment) fragmentClass.newInstance();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.content_frame, fragment)
                                                .commit();
                                        // Highlight the selected item has been done by NavigationView
                                        menuItem.setChecked(true);
                                        // Set action bar title
                                        setTitle(menuItem.getTitle());

                                    }
                                });

                                /*fragmentClass = IndoorLocationFragment.class;
                                changeFragment = true;*/
                                break;
                            case R.id.UpdateProfile:
                                logInfo(this, "Menu update profile option clicked");
                                /*fragmentClass = RegisterFragment.class;
                                changeFragment = true;*/
                                drawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        Class fragmentClass = RegisterFragment.class;
                                        //changeFragment = true;
                                        drawerLayout.closeDrawers();
                                        Fragment fragment = null;
                                        try {
                                            fragment = (Fragment) fragmentClass.newInstance();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.content_frame, fragment)
                                                .commit();
                                        // Highlight the selected item has been done by NavigationView
                                        menuItem.setChecked(true);
                                        // Set action bar title
                                        setTitle(menuItem.getTitle());

                                    }
                                });
                                break;
                            case R.id.exit:
                                logInfo(this, "Menu exit option clicked");
                                closeApp();
                                break;
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
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


    private void closeApp() {
        //Different code path in function of the sdk version.
        //FinishAffinity need api level >= 16
        if (android.os.Build.VERSION.SDK_INT < 16) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        } else {
            this.finishAffinity();
        }
    }

    public void showLateralMenu(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(show ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
            super(activity, drawerLayout, toolbar, R.string.open, R.string.close);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

}
