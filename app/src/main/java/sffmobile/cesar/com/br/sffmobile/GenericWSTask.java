package sffmobile.cesar.com.br.sffmobile;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public abstract class GenericWSTask extends AsyncTask<Object, Void, String> {

	protected SharedPreferences prefs;
	protected String webserviceAddress;
	protected String localWebserviceAddress;
	protected static String currentWebserviceAddress;
	protected List<String> errorMessages = new ArrayList<String>();
	protected Context context;
	protected TaskListener listener;
	protected int connectionTimeout = 3 * 1000; 
	protected String userID = "";
	protected String password = "";
	protected int requestCode;

	public GenericWSTask(Context context,TaskListener listener, int requestCode) {
		this.context = context;
		this.listener = listener;
		this.requestCode = requestCode;

		this.prefs = context.getSharedPreferences(SettingsActivity.APP_PREFS,
				Context.MODE_PRIVATE);
		this.webserviceAddress = prefs.getString(
				SettingsActivity.WEBSERV_ADRRES, null);
		this.localWebserviceAddress = prefs.getString(
				SettingsActivity.LOCAL_WEBSERV_ADRRES, null);
		this.userID = prefs.getString(
				LoginActivity.USER_ID, "");
		this.password = prefs.getString(
				LoginActivity.USER_PASSWORD, "");
	}
	

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
	

	protected boolean hasConnection() {
		int attemptsNumber = 0;
		boolean hasConnection = false;
		
		
		if(localWebserviceAddress == null || localWebserviceAddress.equals("")){
			errorMessages.add("Por favor, configure Endereço Webservice Rede Local!");
			return false;
		}
		
		if(webserviceAddress == null || webserviceAddress.equals("")){
			errorMessages.add("Por favor, configure Endereço Webservice!");
			return false;
		}
		

		SoapObject soap = new SoapObject("http://controller/", "hasConnection");

		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(soap);


		if( currentWebserviceAddress == null)
			currentWebserviceAddress = localWebserviceAddress;
			

		HttpTransportSE httpTransport = new HttpTransportSE(currentWebserviceAddress,connectionTimeout);

		while (attemptsNumber < 3) {
			
			try {

				httpTransport.call("", envelope);

				hasConnection = Boolean.parseBoolean(envelope.getResponse().toString());

			    if(hasConnection)
			    	break;			    
			} catch (Exception e) {
				e.printStackTrace();
			    
				toogleWebserviceAddress(currentWebserviceAddress);
				httpTransport = new HttpTransportSE(currentWebserviceAddress, connectionTimeout);

				
			}
			
			attemptsNumber++;

		}
		
		return hasConnection;

	}
	
	private void  toogleWebserviceAddress(String currentWebserviceAddress){
		
		if(currentWebserviceAddress.equals(this.localWebserviceAddress))
			GenericWSTask.currentWebserviceAddress = this.webserviceAddress;
		else
			GenericWSTask.currentWebserviceAddress = this.localWebserviceAddress;
		
	}
}
