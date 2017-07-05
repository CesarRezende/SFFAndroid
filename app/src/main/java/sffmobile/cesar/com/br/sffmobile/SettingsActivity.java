package sffmobile.cesar.com.br.sffmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

	final static String APP_PREFS = "APP_PREFS";
	final static String WEBSERV_ADRRES = "WEBSERV_ADRRES";
	final static String LOCAL_WEBSERV_ADRRES = "LOCAL_WEBSERV_ADRRES";
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		prefs = getSharedPreferences(SettingsActivity.APP_PREFS, MODE_PRIVATE);
		String webserviceValue = prefs.getString(SettingsActivity.WEBSERV_ADRRES, null);
		String localWebserviceValue = prefs.getString(SettingsActivity.LOCAL_WEBSERV_ADRRES, null);
		
		EditText webServAdressField = (EditText) findViewById(R.id.webservice_address_field);
		EditText localWebServAdressField = (EditText) findViewById(R.id.local_webservice_address_field);
		
		webServAdressField.setText(webserviceValue);
		localWebServAdressField.setText(localWebserviceValue);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		
		boolean pressedCancel = item.getItemId() == R.id.settings_cancel_button;
		boolean pressedOK = item.getItemId() == R.id.settings_ok_button;

		if (pressedCancel){
			
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
		
			finish();
		}
		else if(pressedOK){
			
			EditText webServAdressField = (EditText) findViewById(R.id.webservice_address_field);
			EditText localWebServAdressField = (EditText) findViewById(R.id.local_webservice_address_field);
			
			Editor editor = prefs.edit();
			editor.putString(SettingsActivity.WEBSERV_ADRRES, webServAdressField.getEditableText().toString().trim());
			editor.putString(SettingsActivity.LOCAL_WEBSERV_ADRRES, localWebServAdressField.getEditableText().toString().trim());
			editor.commit();
			
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		}



		return super.onOptionsItemSelected(item);
	}
}
