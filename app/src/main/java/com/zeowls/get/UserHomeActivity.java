package com.zeowls.get;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;

public class UserHomeActivity extends AppCompatActivity {

    public ActionBarDrawerToggle mDrawerToggle;
    public android.support.v7.app.ActionBar supportActionBar;
    public AppBarLayout appBarLayout;
    public Toolbar toolbar;
    NavigationView navigationView;
    public DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configureToolbar();
        configureNavigationView();
        configureDrawer();




    }


    public void configureToolbar() {
        // Adding Toolbar to Main screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        // Create Navigation drawer and inflate layout
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Adding menu icon to Toolbar
        supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void configureDrawer() {
        // Configure drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_closed) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void configureNavigationView() {
        // Set behavior of Navigation drawer
        assert navigationView != null;

        View header = navigationView.getHeaderView(0);
//        usernameNav = (TextView) header.findViewById(R.id.nameNavText);
//        userimageNav = (ImageView) header.findViewById(R.id.nameNavImage);
//
//        try {
//            userId = PrefUtils.getCurrentUser(this).getId();
//            picasso.load(PrefUtils.getCurrentUser(this).getProfilePic()).into(userimageNav);
//            usernameNav.setText(PrefUtils.getCurrentUser(this).getName());
//        } catch (Exception e) {
//            e.printStackTrace();
//            usernameNav.setText("Guest");
//            userimageNav.setImageDrawable(null);
//        }
//
//        loginButton = (LoginButton) header.findViewById(R.id._Facebook_login_button);
//
//        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
////        loginButton.setFragment(getBaseContext());
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                user.setFBToken(loginResult.getAccessToken().getToken());
//                // App code
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.e("response: ", response + "");
//                                try {
//                                    new UserFBLoginTask(object).execute();
//                                    Toast.makeText(getBaseContext(), "welcome " + object.getString("name"), Toast.LENGTH_LONG).show();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id, name, email, gender, birthday, picture.type(large)");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(MainActivity.this, "You Have CANCELED the Login Request", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Toast.makeText(MainActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
//            }
//        });


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

//                        // TODO: handle navigation
//                        if (menuItem.getItemId() == R.id.navHomeBTN) {
//                            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
//                                fragmentManager.popBackStack();
//                            }
//                            fragmentTransaction = fragmentManager.beginTransaction();
//                            if (fragmentManager.findFragmentByTag("homeFragment") != null) {
//                                fragmentTransaction.replace(R.id.fragment_main, fragmentManager.findFragmentByTag("homeFragment"), "homeFragment");
//                            }
//                            fragmentTransaction.commit();
//                            mDrawerLayout.closeDrawers();
//                            return true;
//                        }
//
//                        if (menuItem.getItemId() == R.id.navOrdersBTN) {
//                            //init home fragment
//                            fragmentManager = getSupportFragmentManager();
//                            fragmentTransaction = fragmentManager.beginTransaction();
//                            OrdersFragment fragment = new OrdersFragment();
//                            try {
//                                userId = PrefUtils.getCurrentUser(getBaseContext()).getId();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            fragment.setUserId(userId);
//                            fragmentTransaction.add(R.id.fragment_main, fragment, "orderFragment");
//                            fragmentTransaction.commit();
//                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

}
