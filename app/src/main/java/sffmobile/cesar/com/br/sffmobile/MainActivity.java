package sffmobile.cesar.com.br.sffmobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SEARCH_OPENED = "SEARCH_OPENED";
    private static final String SEARCH_QUERY = "SEARCH_QUERY";
    private static final String MENU_POSITION = "MENU_POSITION";
    public static final int loginRequestCode = 1;
    public static final int settingsRequestCode = 2;
    public static final int MovFinancEditRequestCode = 3;
    public static final int MovFinancInsertRequestCode = 4;
    private String[] mMenuOptions;
    private boolean mSearchOpened;
    private String mSearchQuery;
    private EditText mSearchEt;
    private MenuItem mSearchAction;
    private MenuItem mCloseSearchAction;
    private MenuItem mInsertAction;
    private MenuItem mSettingAction;
    View filterSearch;
    private ContentFragment contentFragment;
    private ListSearchListener listSearchListener;
    private ActionMode.Callback listItemActionMode;
    private SharedPreferences prefs;
    private static boolean authoriedUser = false;
    private static boolean ocurredError = false;
    private TextView yearMonthField;
    private View yearmonthComponent;
    private static boolean mDrawerOpen = false;


    public static boolean isDrawerOpen() {
        return MainActivity.mDrawerOpen;
    }

    public static void setDrawerOpen(boolean isOpen) {
        MainActivity.mDrawerOpen = isOpen;
    }


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
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setYearMonthComponent();

        configDrawerMenu();

        verifyWebServAddressesRecord();

        if (!isOcurredError())
            verifyLoginRecord();


        if (savedInstanceState == null) {
            mSearchOpened = false;
            mSearchQuery = "";
        } else {
            mSearchOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
            mSearchQuery = savedInstanceState.getString(SEARCH_QUERY);
        }

    }

    private void configDrawerMenu() {

        mMenuOptions = getResources().getStringArray(R.array.menu_options);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {


            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
                yearmonthComponent.setVisibility(View.VISIBLE);
                MainActivity.setDrawerOpen(false);

            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                yearmonthComponent.setVisibility(View.GONE);
                MainActivity.setDrawerOpen(true);
                closeSearchBar();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        filterSearch = findViewById(R.id.filterSearch);
        if (filterSearch != null)
            filterSearch.setVisibility(View.GONE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Menu menu = navigationView.getMenu();


        for (int i = 0; i < mMenuOptions.length; i++) {
            //id do g rupo, id do item, ordem do item, nome do item
            menu.add(1, i, i, mMenuOptions[i]);
        }
        //Seleciona item do menu
        MenuItem selectedItem = menu.getItem(SFFApp.getMenuPosition());
        onNavigationItemSelected(selectedItem);

    }

    private void verifyLoginRecord() {
        prefs = getSharedPreferences(SettingsActivity.APP_PREFS, MODE_PRIVATE);
        setAuthoriedUser(prefs.getBoolean(LoginActivity.AUTHORIZED_USER, false));

        if (!isAuthoriedUser()) {
            Intent loginActivitycaller = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivityForResult(loginActivitycaller, loginRequestCode);

        }

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
            if (!firstError)
                errorMessage += "\n";

            errorMessage += getResources().getString(R.string.msg_config_webservice);

            firstError = false;
            setOcurredError(true);
        }

        if (localWebserviceAddress == null || localWebserviceAddress.equals("")) {
            if (!firstError)
                errorMessage += "\n";

            errorMessage += getResources().getString(R.string.msg_config_webservice_local);

            firstError = false;
            setOcurredError(true);
        }

        if (isOcurredError()) {

            AlertDialog dialog = new AlertDialog();
            dialog.setTitle(getResources().getString(R.string.error));
            dialog.setContext(MainActivity.this);
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

            dialog.show(getFragmentManager(), getResources().getString(R.string.error));

            return;
        }

    }

    private void renderYearMonthComponent() {
        this.yearmonthComponent = findViewById(R.id.yearmonth_component);
        this.yearMonthField = (TextView) findViewById(R.id.year_month_field);
        String yearMonthStr = Integer.toString(SFFApp.getYearMonth());
        this.yearMonthField.setText(yearMonthStr.substring(4) + "/" + yearMonthStr.substring(0, 3));
    }

    private void resetActivity() {
        finish();
        startActivity(getIntent());
    }

    private void setYearMonthComponent() {
        if (SFFApp.getYearMonth() <= 0) {
            SFFApp.setYearMonth(Calendar.getInstance());
        }
        renderYearMonthComponent();
        ImageButton nextYearMonthButton = (ImageButton) findViewById(R.id.next_yearmonth_button);
        ((ImageButton) findViewById(R.id.previews_yearmonth_button)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();
                date.set(SFFApp.getYear(),
                        SFFApp.getMonth() - 1,
                        1);
                date.add(Calendar.MONTH, -1);
                SFFApp.setYearMonth(date);
                resetActivity();
            }
        });
        nextYearMonthButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar date = Calendar.getInstance();
                date.set(SFFApp.getYear(),
                        SFFApp.getMonth() - 1,
                        1);
                date.add(Calendar.MONTH, +1);
                SFFApp.setYearMonth(date);
                resetActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == loginRequestCode) {

            if (resultCode == RESULT_OK) {
                MainActivity.setAuthoriedUser(true);
                finish();
                startActivity(getIntent());

            } else {
                finish();
            }

        } else if (requestCode == settingsRequestCode) {

            finish();
            startActivity(getIntent());
        } else if (requestCode == MovFinancEditRequestCode) {

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        mSearchAction = menu.findItem(R.id.action_search);
        mCloseSearchAction = menu.findItem(R.id.action_close_search);
        mInsertAction = menu.findItem(R.id.action_insert);
        mSettingAction = menu.findItem(R.id.action_settings);

        mSearchAction.setVisible(!MainActivity.isDrawerOpen());

        if (mSearchOpened) {
            mSearchAction.setVisible(false);
            mInsertAction.setVisible(false);
            mSettingAction.setVisible(false);
            mCloseSearchAction.setVisible(true);
            filterSearch.setVisibility(View.VISIBLE);

        } else {
            mInsertAction.setVisible(true);
            mSettingAction.setVisible(true);
            mSearchAction.setVisible(true);
            mCloseSearchAction.setVisible(false);
            filterSearch.setVisibility(View.GONE);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent settingActivityCaller = new Intent(MainActivity.this,
                        SettingsActivity.class);
                startActivityForResult(settingActivityCaller, settingsRequestCode);
                break;
            case R.id.action_search:
                openSearchBar(mSearchQuery);
                break;
            case R.id.action_close_search:
                closeSearchBar();
                break;
            case R.id.action_insert:

                switch (mMenuOptions[SFFApp.getMenuPosition()]) {
                    case "Movimentação Finaceira":
                        Intent movFinancActivityCaller = new Intent(MainActivity.this,
                                MovFinancActivity.class);
                        MovimentacaoFinanceira movFinanc = new MovimentacaoFinanceira();
                        movFinanc.setNewRecord(true);
                        movFinanc.setId(0L);
                        movFinanc.setDataLancamento(Calendar.getInstance().getTime());
                        movFinanc.setManual(true);
                        movFinanc.setTipoMovimentacao('D');
                        movFinanc.setTipoGastoId(0L);
                        movFinanc.setSituacao('P');

                        Bundle msg = new Bundle();
                        msg.putSerializable("movFinanc", movFinanc);
                        movFinancActivityCaller.putExtras(msg);
                        contentFragment.startActivityForResult(movFinancActivityCaller,
                                MovFinancInsertRequestCode);
                        break;
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
                    default:
                        break;
                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText) {


        mSearchEt = (EditText) findViewById(
                R.id.filterSearch);

        if (mSearchEt != null) {

            mSearchEt.addTextChangedListener(new SearchWatcher());
            mSearchEt.setText(queryText);
            mSearchEt.requestFocus();
            mSearchEt.setVisibility(View.VISIBLE);
        }

        // Change search icon accordingly.
        if (mSearchAction != null) {

            mSearchOpened = true;

            mInsertAction.setVisible(false);
            mSettingAction.setVisible(false);
            mSearchAction.setVisible(false);
            mCloseSearchAction.setVisible(true);
            filterSearch.setVisibility(View.VISIBLE);
            listSearchListener.startSearch();

        }

    }

    private void closeSearchBar() {

        listSearchListener.stopSearch();
        mSearchEt = (EditText) findViewById(
                R.id.filterSearch);

        mInsertAction.setVisible(true);
        mSettingAction.setVisible(true);
        mSearchAction.setVisible(true);
        mCloseSearchAction.setVisible(false);
        filterSearch.setVisibility(View.GONE);
        mSearchOpened = false;
        mSearchQuery = "";

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SEARCH_OPENED, mSearchOpened);
        outState.putString(SEARCH_QUERY, mSearchQuery);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int position = item.getItemId();
        SFFApp.setMenuPosition(position);

        if (mMenuOptions[position].equals("Alterar Usuario")) {

            SFFApp.setMenuPosition(0);

            Editor editor = prefs.edit();
            editor.putBoolean(LoginActivity.AUTHORIZED_USER, false);
            editor.putString(LoginActivity.USER_ID, "");
            editor.putString(LoginActivity.USER_PASSWORD, "");
            editor.commit();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);

            finish();

            Intent loginActivitycaller = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivityForResult(loginActivitycaller, 1);

            return true;
        } else if (mMenuOptions[position].equals("Sair")) {

            SFFApp.setMenuPosition(0);
            finish();

            return true;
        }

        contentFragment = new ContentFragment();
        listSearchListener = contentFragment;
        Bundle args = new Bundle();
        args.putInt(ContentFragment.OPTION_NUMBER, position);
        contentFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, contentFragment).commit();

        setTitle(mMenuOptions[position]);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mSearchQuery = mSearchEt.getText().toString();

            listSearchListener.performSearch(mSearchQuery);

        }

    }

    public static class ContentFragment extends ListFragment implements
            ActionMode.Callback, TaskListener, ListSearchListener {
        public static final String OPTION_NUMBER = "option_number";
        private static final int MOVFINANC_CONSULT_REQUEST_TASK = 1;
        private static final int MOVFINANC_DELETE_REQUEST_TASK = 2;
        private static final int MOVFINANC_ACCOMPLISH_REQUEST_TASK = 3;
        private static final int CHART_REQUEST_TASK = 4;
        private ArrayList<ChartItem> chartList;

        private GenericWSTask task;
        private ProgressDialog pDialog;
        private List<MovimentacaoFinanceira> listMovFinac = new ArrayList<MovimentacaoFinanceira>();
        private List<MovimentacaoFinanceira> filtedListMovFinac = new ArrayList<MovimentacaoFinanceira>();
        private ListView listView;
        private boolean inSearch = false;
        private String[] options;
        private ContentFragment contentFragment;

        public ContentFragment() {

        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {

            super.onListItemClick(l, v, position, id);
        }

        private void configItemLongClickListener() {
            getListView().setOnItemLongClickListener(
                    new OnItemLongClickListener() {
                        private ActionMode mActionMode;

                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent,
                                                       View view, int position, long id) {
                            MovimentacaoFinanceira movFinac = null;

                            mActionMode = getActivity()
                                    .startActionMode(
                                            ((MainActivity) getActivity()).listItemActionMode);

                            if (inSearch)
                                movFinac = filtedListMovFinac.get(position);
                            else
                                movFinac = listMovFinac.get(position);

                            mActionMode.setTag(movFinac);
                            view.setSelected(true);
                            return true;
                        }
                    });

        }

        private void renderMovFinacListView() {

            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
                    : android.R.layout.simple_list_item_1;

            if (inSearch)
                setListAdapter(new MovFinacArrayAdapter(getActivity(), layout,
                        R.id.textview1, filtedListMovFinac));
            else
                setListAdapter(new MovFinacArrayAdapter(getActivity(), layout,
                        R.id.textview1, listMovFinac));
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            configItemLongClickListener();
            ((MainActivity) getActivity()).contentFragment = this;
            this.contentFragment = this;
            ((MainActivity) getActivity()).listSearchListener = this;
            ((MainActivity) getActivity()).listItemActionMode = this;

            int i = SFFApp.getMenuPosition();
            options = getResources().getStringArray(R.array.menu_options);
            String menu = options[i];
            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
                    : android.R.layout.simple_list_item_1;

            switch (menu) {
                case "Dashboard":
                    if ((this.chartList == null || this.chartList.size() <= 0)
                            && MainActivity.isAuthoriedUser()
                            && !MainActivity.isOcurredError()) {

                        initLayout();
                        task = new WSChartTask(getActivity(), this,
                                CHART_REQUEST_TASK);
                        task.execute(this.chartList);
                        setListAdapter(new ChartDataAdapter(getActivity(), layout, R.id.textview1,
                                this.chartList));
                    }
                    if (this.chartList == null)
                        this.chartList = new ArrayList();

                    setListAdapter(new ChartDataAdapter(getActivity(), layout, R.id.textview1, this.chartList));
                    break;
                case "Movimentação Finaceira":
                    if ((this.listMovFinac == null || this.listMovFinac.size() <= 0)
                            && MainActivity.isAuthoriedUser()
                            && !MainActivity.isOcurredError()) {

                        initLayout();
                        task = new WSMovFinacTask(getActivity(), this,
                                this.listMovFinac, MOVFINANC_CONSULT_REQUEST_TASK);
                        task.execute(WSMovFinacTask.WebServiceMethod.CONSULTING);

                        setListAdapter(new MovFinacArrayAdapter(getActivity(),
                                layout, R.id.textview1, listMovFinac));
                    }

                    renderMovFinacListView();

                    break;
                case "Entrada Variavel":
                    initLayout();
                    this.listView.setAdapter(new ArrayAdapter<String>(
                            getActivity(), layout, new String[]{""}));
                    break;
                case "Saida Variavel":
                    initLayout();
                    this.listView.setAdapter(new ArrayAdapter<String>(
                            getActivity(), layout, new String[]{""}));
                    break;
                case "Entrada Fixa":
                    initLayout();
                    this.listView.setAdapter(new ArrayAdapter<String>(
                            getActivity(), layout, new String[]{""}));
                    break;
                case "Saida Fixa":
                    initLayout();
                    this.listView.setAdapter(new ArrayAdapter<String>(
                            getActivity(), layout, new String[]{""}));
                    break;

                default:
                    break;
            }

        }

        private void initLayout() {
            if (getActivity().getIntent() != null) {
                getActivity().getIntent().getIntExtra("id", 0);
            } else {
                return;
            }
            this.listView = getListView();
            this.filtedListMovFinac = new ArrayList<MovimentacaoFinanceira>();
            listMovFinac = new ArrayList<MovimentacaoFinanceira>();
            this.chartList = new ArrayList();
        }

        @Override
        public void onStart() {
            super.onStart();

            if (getFragmentManager().findFragmentById(R.id.content_frame) != null) {
                if (listView != null)
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }
        }

        @Override
        public void onDetach() {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            super.onDetach();
        }

        @Override
        public void onTaskStarted(int requestCode) {
            lockScreenOrientation();

            if (requestCode == MOVFINANC_DELETE_REQUEST_TASK)
                pDialog = ProgressDialog.show(getActivity(), getContext().getResources().getString(R.string.deleting),
                        getContext().getResources().getString(R.string.msg_deleting_wait), true);
            else
                pDialog = ProgressDialog.show(getActivity(), getContext().getResources().getString(R.string.loading),
                        getContext().getResources().getString(R.string.msg_loading_wait), true);

        }

        @Override
        public void onTaskFinished(int requestCode, List<String> errorMessages) {

            if (this.pDialog.isShowing())
                this.pDialog.dismiss();

            if (errorMessages == null || errorMessages.size() <= 0) {

                int i = SFFApp.getMenuPosition();
                String menu = options[i];

                switch (menu) {
                    case "Dashboard":
                        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
                                : android.R.layout.simple_list_item_1;
                        setListAdapter(new ChartDataAdapter(getActivity(), layout, R.id.textview1, this.chartList));
                        break;
                    case "Movimentação Finaceira":

                        Collections.sort(this.listMovFinac,
                                MovimentacaoFinanceira.getComparator());

                        renderMovFinacListView();

                        break;
                    case "Entrada Variavel":

                        break;
                    case "Saida Variavel":

                        break;
                    case "Entrada Fixa":

                        break;
                    case "Saida Fixa":

                        break;

                    default:
                        break;
                }

                unlockScreenOrientation();
            } else {

                String errorMessage = "";
                for (String msg : errorMessages) {

                    if (msg.equals(errorMessages.get(0)))
                        errorMessage += msg;
                    else
                        errorMessage += "\n" + msg;

                }

                AlertDialog dialog = new AlertDialog();
                dialog.setTitle(getResources().getString(R.string.error));
                dialog.setContext(getActivity());
                dialog.setMessage(errorMessage);
                dialog.setOnClickListener(new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        unlockScreenOrientation();
                    }
                });

                dialog.show(getFragmentManager(), getResources().getString(R.string.error));
            }

        }

        private void lockScreenOrientation() {
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                getActivity().setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        private void unlockScreenOrientation() {
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        @Override
        public void startSearch() {
            inSearch = true;
        }

        @Override
        public void stopSearch() {
            inSearch = false;
            this.listView = getListView();

            int i = SFFApp.getMenuPosition();
            String menu = options[i];

            switch (menu) {
                case "Movimentação Finaceira":

                    Collections.sort(this.listMovFinac,
                            MovimentacaoFinanceira.getComparator());

                    this.listView.setAdapter(new MovFinacArrayAdapter(
                            getActivity(), R.layout.movfinac_list_item,
                            R.id.textview1, this.listMovFinac));

                    break;
                case "Entrada Variavel":

                    break;
                case "Saida Variavel":

                    break;
                case "Entrada Fixa":

                    break;
                case "Saida Fixa":

                    break;

                default:
                    break;
            }

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent data) {

            if (requestCode == MovFinancEditRequestCode) {

                if (resultCode == RESULT_OK) {
                    MovimentacaoFinanceira movFinanc = (MovimentacaoFinanceira) data
                            .getSerializableExtra("movFinanc");

                    Date dataReferencia = movFinanc.getDataRealizada() != null ? movFinanc
                            .getDataRealizada() : movFinanc.getDataPrevista();

                    MovimentacaoFinanceira oldMovFinanc = MovimentacaoFinanceira
                            .findMovimentacaoFinanceira(listMovFinac,
                                    movFinanc.getId());

                    if (SFFUtil.isDateThatYearMonth(dataReferencia,
                            SFFApp.getYear(), SFFApp.getMonth()))
                        listMovFinac.set(listMovFinac.indexOf(oldMovFinanc),
                                movFinanc);
                    else
                        listMovFinac.remove(listMovFinac.indexOf(oldMovFinanc));

                    Collections.sort(this.listMovFinac,
                            MovimentacaoFinanceira.getComparator());

                    if (inSearch) {

                        oldMovFinanc = MovimentacaoFinanceira
                                .findMovimentacaoFinanceira(filtedListMovFinac,
                                        movFinanc.getId());

                        if (SFFUtil.isDateThatYearMonth(dataReferencia,
                                SFFApp.getYear(), SFFApp.getMonth()))
                            filtedListMovFinac.set(
                                    filtedListMovFinac.indexOf(oldMovFinanc),
                                    movFinanc);
                        else
                            filtedListMovFinac.remove(filtedListMovFinac.indexOf(oldMovFinanc));

                        Collections.sort(this.filtedListMovFinac,
                                MovimentacaoFinanceira.getComparator());
                    }

                    renderMovFinacListView();

                } else {

                }

            } else if (requestCode == MovFinancInsertRequestCode) {

                if (resultCode == RESULT_OK) {
                    MovimentacaoFinanceira movFinanc = (MovimentacaoFinanceira) data
                            .getSerializableExtra("movFinanc");

                    Date dataReferencia = movFinanc.getDataRealizada() != null ? movFinanc
                            .getDataRealizada() : movFinanc.getDataPrevista();

                    if (SFFUtil.isDateThatYearMonth(dataReferencia,
                            SFFApp.getYear(), SFFApp.getMonth()))
                        listMovFinac.add(movFinanc);

                    Collections.sort(this.listMovFinac,
                            MovimentacaoFinanceira.getComparator());

                    if (inSearch) {

                        if (SFFUtil.isDateThatYearMonth(dataReferencia,
                                SFFApp.getYear(), SFFApp.getMonth()))
                            filtedListMovFinac.add(movFinanc);

                        Collections.sort(this.filtedListMovFinac,
                                MovimentacaoFinanceira.getComparator());
                    }

                    renderMovFinacListView();

                } else {

                }
            }

        }

        @Override
        public void performSearch(String query) {
            this.listView = getListView();
            boolean isQueryNullOrBlank = SFFUtil.isNullOrBlank(query);
            int i = SFFApp.getMenuPosition();
            String menu = options[i];


            switch (menu) {
                case "Movimentação Finaceira":
                    this.filtedListMovFinac = new ArrayList<MovimentacaoFinanceira>();

                    for (MovimentacaoFinanceira movFinac : this.listMovFinac) {

                        String descr = movFinac.getDescricao();
                        if (!isQueryNullOrBlank) {
                            if (descr.toUpperCase(new Locale("pt", "BR")).contains(
                                    query.toUpperCase(new Locale("pt", "BR"))))
                                filtedListMovFinac.add(movFinac);
                        } else
                            filtedListMovFinac.add(movFinac);
                    }

                    Collections.sort(this.filtedListMovFinac,
                            MovimentacaoFinanceira.getComparator());

                    this.listView.setAdapter(new MovFinacArrayAdapter(
                            getActivity(), R.layout.movfinac_list_item,
                            R.id.textview1, this.filtedListMovFinac));

                    break;
                case "Entrada Variavel":

                    break;
                case "Saida Variavel":

                    break;
                case "Entrada Fixa":

                    break;
                case "Saida Fixa":

                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            String confirmMsg = "";
            ConfirmDialog dialog = null;
            final MovimentacaoFinanceira movFinac = (MovimentacaoFinanceira) mode
                    .getTag();
            switch (item.getItemId()) {
                case R.id.action_accomplish:
                    confirmMsg = SFFUtil.buildMessage(getContext().getResources().getString(R.string.msg_confirm_accomplish),
                            movFinac.getDescricao());
                    dialog = new ConfirmDialog();
                    dialog.setTitle(getContext().getResources().getString(R.string.confirm));
                    dialog.setMessage(confirmMsg);
                    dialog.setContext(getActivity());
                    dialog.setOnOkClickListener(
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    task = new WSMovFinacTask(getActivity(),
                                            contentFragment, listMovFinac,
                                            MOVFINANC_ACCOMPLISH_REQUEST_TASK);
                                    task.execute(
                                            WSMovFinacTask.WebServiceMethod.ACCOMPLISHING,
                                            movFinac, listMovFinac,
                                            filtedListMovFinac, inSearch);

                                }
                            });
                    dialog.setOnCancelClickListener(
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            });

                    dialog.show(getFragmentManager(), getContext().getResources().getString(R.string.confirm));

                    mode.finish();
                    return true;
                case R.id.action_edit:

                    Intent movFinancActivityCaller = new Intent(this.getActivity(),
                            MovFinancActivity.class);
                    Bundle msg = new Bundle();
                    msg.putSerializable("movFinanc", movFinac);
                    movFinancActivityCaller.putExtras(msg);
                    this.startActivityForResult(movFinancActivityCaller,
                            MovFinancEditRequestCode);
                    mode.finish();
                    return true;
                case R.id.action_erase:
                    confirmMsg = SFFUtil.buildMessage(getContext().getResources().getString(R.string.msg_confirm_delete),
                            movFinac.getDescricao());
                    dialog = new ConfirmDialog();
                    dialog.setTitle(getContext().getResources().getString(R.string.confirm));
                    dialog.setMessage(confirmMsg);
                    dialog.setContext(getActivity());
                    dialog.setOnOkClickListener(new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                            task = new WSMovFinacTask(getActivity(),
                                    contentFragment, listMovFinac,
                                    MOVFINANC_DELETE_REQUEST_TASK);
                            task.execute(
                                    WSMovFinacTask.WebServiceMethod.DELETING,
                                    movFinac, listMovFinac,
                                    filtedListMovFinac, inSearch);
                        }
                    });

                    dialog.setOnCancelClickListener(new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {

                        }
                    });

                    dialog.show(getFragmentManager(), getContext().getResources().getString(R.string.confirm));
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }

    }

    private static class ChartDataAdapter extends ArrayAdapter<ChartItem> {
        public ChartDataAdapter(Context context, int resource,
                                int textViewResourceId, List<ChartItem> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return ((ChartItem) getItem(position)).getView(position, convertView, getContext());
        }

        public int getItemViewType(int position) {
            return ((ChartItem) getItem(position)).getItemType();
        }

        public int getViewTypeCount() {
            return 3;
        }
    }

    public static class MovFinacArrayAdapter extends
            ArrayAdapter<MovimentacaoFinanceira> {

        private final Context context;
        private final List<MovimentacaoFinanceira> listMovFinac;

        public MovFinacArrayAdapter(Context context, int resource,
                                    int textViewResourceId,
                                    List<MovimentacaoFinanceira> listMovFinac) {
            super(context, resource, textViewResourceId, listMovFinac);
            this.context = context;
            this.listMovFinac = listMovFinac;
        }

        @SuppressLint({"ViewHolder", "NewApi"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.movfinac_list_item,
                    parent, false);

            MovimentacaoFinanceira movFinanc = listMovFinac.get(position);

            ImageView tipoMovImage = (ImageView) rowView
                    .findViewById(R.id.list_item_image);

            TextView descField = (TextView) rowView
                    .findViewById(R.id.list_item_descr);

            TextView coinSymbolField = (TextView) rowView
                    .findViewById(R.id.list_item_coin_symbol);

            TextView saldoField = (TextView) rowView
                    .findViewById(R.id.list_item_saldo);

            TextView expectedDateField = (TextView) rowView
                    .findViewById(R.id.list_expected_date);

            TextView fulfillmentDateField = (TextView) rowView
                    .findViewById(R.id.list_fulfillment_date);

            ImageView fulfillmentImage = (ImageView) rowView
                    .findViewById(R.id.list_item_fulfillment_image);

            descField.setText(movFinanc.getDescricao());
            saldoField
                    .setText(SFFUtil.getFormattedNumber(movFinanc.getSaldo()));
            expectedDateField.setText(SFFUtil.getFormattedData(
                    movFinanc.getDataPrevista(), SFFUtil.DateFormat.SHORT));
            fulfillmentDateField.setText(SFFUtil.getFormattedData(
                    movFinanc.getDataRealizada(), SFFUtil.DateFormat.SHORT));

            if (movFinanc.getTipoMovimentacao().equals(Character.valueOf('C'))) {
                tipoMovImage.setImageResource(R.drawable.money);
                descField.setTextColor(Color.BLUE);
                saldoField.setTextColor(Color.BLUE);
                expectedDateField.setTextColor(Color.BLUE);
                fulfillmentDateField.setTextColor(Color.BLUE);
                coinSymbolField.setTextColor(Color.BLUE);
            } else {
                tipoMovImage.setImageResource(R.drawable.paper_plane);
                descField.setTextColor(Color.RED);
                saldoField.setTextColor(Color.RED);
                expectedDateField.setTextColor(Color.RED);
                fulfillmentDateField.setTextColor(Color.RED);
                coinSymbolField.setTextColor(Color.RED);
            }

            if (movFinanc.getSituacao().equals('R'))
                fulfillmentImage.setVisibility(ImageView.VISIBLE);
            else
                fulfillmentImage.setVisibility(ImageView.INVISIBLE);
            return rowView;
        }

    }
}
