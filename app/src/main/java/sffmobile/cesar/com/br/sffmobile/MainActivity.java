package sffmobile.cesar.com.br.sffmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ActionMode;
import android.widget.TextView;
import android.widget.EditText;
import android.graphics.drawable.Drawable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SEARCH_OPENED = "search_opened";
    private static final String SEARCH_QUERY = "seach_qury";
    private static final String MENU_POSITION = "MENU_POSITION";
    public static final int loginRequestCode = 1;
    public static final int settingsRequestCode = 2;
    public static final int MovFinancEditRequestCode = 3;
    public static final int MovFinancInsertRequestCode = 4;
    private CharSequence mTitle;
    private String[] mMenuOptions;
    private boolean mSearchOpened;
    private String mSearchQuery;
    private Drawable mIconOpenSearch;
    private Drawable mIconCloseSearch;
    private EditText mSearchEt;
    private MenuItem mSearchAction;
    private MenuItem mInsertAction;
    private MenuItem mSettingAction;
    //private ContentFragment contentFragment;
   // private ListSearchListener listSearchListener;
    private ActionMode.Callback listItemActionMode;
    private SharedPreferences prefs;
    private static boolean authoriedUser = false;
    private static boolean ocurredError = false;
    private TextView yearMonthField;
    private View yearmonthComponent;



    public static boolean isOcurredError() {
        return ocurredError;
    }

    public static void setOcurredError(boolean ocurredError) {
        MainActivity.ocurredError = ocurredError;
    }

    public static boolean isAuthoriedUser() {
        return authoriedUser;
    }

    public static void setAuthoriedUser(boolean authoriedUser) {
        MainActivity.authoriedUser = authoriedUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configDrawerMenu();

        verifyWebServAddressesRecord();



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
        //Seleciona item do menu
        MenuItem selectedItem = menu.getItem(SFFApp.getMenuPosition());
        onNavigationItemSelected(selectedItem);

    }

    private void verifyWebServAddressesRecord() {

        this.prefs = getSharedPreferences(SettingsActivity.APP_PREFS,
                Context.MODE_PRIVATE);
        String webserviceAddress = prefs.getString(
                SettingsActivity.WEBSERV_ADRRES, null);
        String localWebserviceAddress = prefs.getString(
                SettingsActivity.LOCAL_WEBSERV_ADRRES, null);

        String errorMessage = "";
        boolean firstError = true;

        if (webserviceAddress == null || webserviceAddress.equals("")) {
            if (firstError)
                errorMessage += "Por favor, configure Endereço Webservice!";
            else
                errorMessage += "\nPor favor, configure Endereço Webservice!";

            firstError = false;
            setOcurredError(true);
        }

        if (localWebserviceAddress == null || localWebserviceAddress.equals("")) {
            if (firstError)
                errorMessage += "Por favor, configure Endereço Webservice Rede Local!";
            else
                errorMessage += "\nPor favor, configure Endereço Webservice Rede Local!";

            firstError = false;
            setOcurredError(true);
        }

        if (isOcurredError()) {

            AlertDialog dialog = new AlertDialog();
            dialog.setTitle("Erro");
            dialog.setContext( MainActivity.this);
            dialog.setMessage(errorMessage);
            dialog.setOnClickListener(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent settingActivityCaller = new Intent(
                            MainActivity.this, SettingsActivity.class);
                    startActivityForResult(settingActivityCaller,
                            settingsRequestCode);
                    setOcurredError(false);
                }
            });

            dialog.show(getFragmentManager(), "Erro");

            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == loginRequestCode) {

            if (resultCode == RESULT_OK) {
                //MainActivity.setAuthoriedUser(true);
                finish();
                startActivity(getIntent());

            } else {
                finish();
            }

        } else if (requestCode == settingsRequestCode) {

            finish();
            startActivity(getIntent());
        }

        else if (requestCode == MovFinancEditRequestCode) {

            if (resultCode == RESULT_OK) {

            } else {

            }

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

        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent settingActivityCaller = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivityForResult(settingActivityCaller, settingsRequestCode);
                break;
//            case R.id.action_search:
//                if (mSearchOpened) {
//                    closeSearchBar();
//                } else {
//                    openSearchBar(mSearchQuery);
//                }
//
//                break;
//            case R.id.action_insert:
//
//                switch (mMenuOptions[SFFApp.getMenuPosition()]) {
//                    case "Movimentação Finaceira":
//                        Intent movFinancActivityCaller = new Intent(MainActivity.this,
//                                MovFinancActivity.class);
//                        MovimentacaoFinanceira movFinanc = new MovimentacaoFinanceira();
//                        movFinanc.setNewRecord(true);
//                        movFinanc.setId(0L);
//                        movFinanc.setDataLancamento(Calendar.getInstance().getTime());
//                        movFinanc.setManual(true);
//                        movFinanc.setTipoMovimentacao('D');
//                        movFinanc.setTipoGastoId(0L);
//                        movFinanc.setSituacao('P');
//
//                        Bundle msg = new Bundle();
//                        msg.putSerializable("movFinanc", movFinanc);
//                        movFinancActivityCaller.putExtras(msg);
//                        contentFragment.startActivityForResult(movFinancActivityCaller,
//                                MovFinancInsertRequestCode);
//                        break;
//                    case "Entrada Variavel":
//
//                        break;
//                    case "Saida Variavel":
//
//                        break;
//                    case "Entrada Fixa":
//
//                        break;
//                    case "Saida Fixa":
//
//                        break;
//                    default:
//                        break;
//                }
//
//                break;

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
