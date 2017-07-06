package sffmobile.cesar.com.br.sffmobile;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements TaskListener {

	final static String AUTHORIZED_USER = "AUTHORIZED_USER";
	final static String USER_ID = "USER_ID";
	final static String USER_PASSWORD = "USER_PASSWORD";
	private SharedPreferences prefs;
	private boolean ocurredError = false;
	private String userID = "";
	private String password = "";
	private ProgressDialog pDialog = null;
	private static final int LOGIN_REQUEST_TASK = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		verifyWebServAddressesRecord();
		prefs = getSharedPreferences(SettingsActivity.APP_PREFS, MODE_PRIVATE);
		Button loginButton = (Button) findViewById(R.id.login_button);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				EditText userField = (EditText) findViewById(R.id.login_user_field);
				EditText passwordField = (EditText) findViewById(R.id.login_password_field);

				userID = userField.getEditableText().toString();
				password = passwordField.getEditableText().toString();
				String errorMessage = "";
				boolean firstError = true;

				if (userID.equals("")) {
					if (firstError)
						errorMessage += "*Campo Usuário é obrigatório";
					else
						errorMessage += "\n*Campo Usuário é obrigatório";

					firstError = false;
					ocurredError = true;
				}

				if (password.equals("")) {
					if (firstError)
						errorMessage += "*Campo Senha é obrigatório";
					else
						errorMessage += "\n*Campo Senha é obrigatório";

					firstError = false;
					ocurredError = true;
				}

				if (!userID.equals("")
						&& (userID.length() < 3 || userID.length() > 30)) {
					if (firstError)
						errorMessage += "*Nome do Usuario deve conter de 3 a 30 caracteres";
					else
						errorMessage += "\n*Nome do Usuario deve conter de 3 a 30 caracteres";

					firstError = false;
					ocurredError = true;
				}

				if (!password.equals("")
						&& (password.length() < 6 || password.length() > 30)) {
					if (firstError)
						errorMessage += "*Senha do Usuario deve conter de 6 a 30 caracteres";
					else
						errorMessage += "\n*Senha do Usuario deve conter de 6 a 30 caracteres";

					firstError = false;
					ocurredError = true;
				}

				AlertDialog dialog = new AlertDialog();
				dialog.setTitle("Erro");
				dialog.setContext( LoginActivity.this);
				dialog.setMessage(errorMessage);
				dialog.setOnClickListener(
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								ocurredError = false;
								unlockScreenOrientation();
							}
						});

				

				if (ocurredError){
					lockScreenOrientation();
					dialog.show(getFragmentManager(), "Erro");					
					return;
				}

				Editor editor = prefs.edit();
				editor.putString(USER_ID, userID);
				editor.putString(USER_PASSWORD, password);
				editor.commit();

				WSLoginTask task = new WSLoginTask(LoginActivity.this,
						LoginActivity.this, LOGIN_REQUEST_TASK);
				task.execute();

			}
		});

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
			ocurredError = true;
		}

		if (localWebserviceAddress == null || localWebserviceAddress.equals("")) {
			if (firstError)
				errorMessage += "Por favor, configure Endereço Webservice Rede Local!";
			else
				errorMessage += "\nPor favor, configure Endereço Webservice Rede Local!";

			firstError = false;
			ocurredError = true;
		}

		if (ocurredError) {
			lockScreenOrientation();
			AlertDialog dialog = new AlertDialog();
			dialog.setTitle("Erro");
			dialog.setContext( LoginActivity.this);
			dialog.setMessage(errorMessage);
			dialog.setOnClickListener( new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							Intent settingActivityCaller = new Intent(
									LoginActivity.this, SettingsActivity.class);
							startActivityForResult(settingActivityCaller,
									MainActivity.settingsRequestCode);
							ocurredError = false;
							unlockScreenOrientation();
						}
					});

			dialog.show(getFragmentManager(), "Erro");

			return;
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.login, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.login_exit_button:
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
			
			break;
		case R.id.action_settings:
			Intent settingActivityCaller = new Intent(
					LoginActivity.this, SettingsActivity.class);
			startActivityForResult(settingActivityCaller,
					MainActivity.settingsRequestCode);

		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.settingsRequestCode) {

			finish();
			startActivity(getIntent());
		}

	}
	
	@Override
	public void onTaskStarted(int requestCode) {
		lockScreenOrientation();
		pDialog = ProgressDialog.show(LoginActivity.this, "Login",
				"Realizando login. Por favor aguarde...", true);

	}

	private void lockScreenOrientation() {
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	private void unlockScreenOrientation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

	@Override
	public void onTaskFinished(int requestCode, List<String> errorMessages) {

		if (this.pDialog.isShowing())
			this.pDialog.dismiss();

		if (errorMessages == null || errorMessages.size() <= 0) {
			Editor editor = prefs.edit();

			editor.putBoolean(AUTHORIZED_USER, true);
			editor.putString(USER_ID, this.userID);
			editor.putString(USER_PASSWORD, password);
			editor.commit();

			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			unlockScreenOrientation();

			finish();

		} else {

			String errorMessage = "";
			for (String msg : errorMessages) {

				if (msg.equals(errorMessages.get(0)))
					errorMessage += msg;
				else
					errorMessage += "\n" + msg;

			}

			
			AlertDialog dialog = new AlertDialog();
			dialog.setTitle("Erro");
			dialog.setContext( LoginActivity.this);
			dialog.setMessage(errorMessage);
			dialog.setOnClickListener(
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {

							Editor editor = prefs.edit();
							
							editor.putBoolean(AUTHORIZED_USER, false);
							editor.putString(USER_ID, "");
							editor.putString(USER_PASSWORD, "");
							editor.commit();
							unlockScreenOrientation();
						}
					});

			dialog.show(getFragmentManager(), "Erro");
			
			
			
		}

	}
}
