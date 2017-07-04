package sffmobile.cesar.com.br.sffmobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] mMenuOptions;
    private CharSequence mTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configDrawerMenu();
    }

    private void configDrawerMenu() {

        mMenuOptions = getResources().getStringArray(R.array.menu_options);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
                //closeSearchBar();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                if (mDrawerLayout.isDrawerOpen(MainActivity.this.mDrawerList)) {
//                    yearmonthComponent.setVisibility(View.VISIBLE);
//                } else {
//                    yearmonthComponent.setVisibility(View.GONE);
//                }
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Menu menu = navigationView.getMenu();

        for (int i =0; i < mMenuOptions.length; i++) {
            //id do g rupo, id do item, ordem do item, nome do item
            menu.add(1, i, i, mMenuOptions[i]);
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int position = item.getItemId();
        SFFApp.setMenuPosition(position);

        if (mMenuOptions[position].equals("Alterar Usuario")) {

//            SFFApp.setMenuPosition(0);
//
//            Editor editor = prefs.edit();
//            editor.putBoolean(LoginActivity.AUTHORIZED_USER, false);
//            editor.putString(LoginActivity.USER_ID, "");
//            editor.putString(LoginActivity.USER_PASSWORD, "");
//            editor.commit();
//
//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//
//            finish();
//
//            Intent loginActivitycaller = new Intent(MainActivity.this,
//                    LoginActivity.class);
//            startActivityForResult(loginActivitycaller, 1);
//
//            if (mDrawerLayout.isDrawerOpen(mDrawerList))
//                mDrawerLayout.closeDrawer(mDrawerList);

//            return;
        }else if(mMenuOptions[position].equals("Sair")){

            SFFApp.setMenuPosition(0);
            finish();

        }

//        contentFragment = new ContentFragment();
//        listSearchListener = contentFragment;
//        Bundle args = new Bundle();
//        args.putInt(ContentFragment.OPTION_NUMBER, position);
//        contentFragment.setArguments(args);

//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.content_frame, contentFragment).commit();

        setTitle(mMenuOptions[position]);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}