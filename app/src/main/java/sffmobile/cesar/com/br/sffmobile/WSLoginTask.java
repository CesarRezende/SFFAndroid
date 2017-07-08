package sffmobile.cesar.com.br.sffmobile;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

public class WSLoginTask extends GenericWSTask {

	
	
	public WSLoginTask(Context context, TaskListener listener, int requestCode) {
		super(context, listener, requestCode);
	}

	@Override
	protected String doInBackground(Object... params) {

		SoapObject soap = new SoapObject("http://controller/",
				"validateUser");

		if (!hasConnection()) {
			errorMessages.add(context.getResources().getString(R.string.msg_could_not_conect_to_server));
			return "";
		}


		soap.addProperty("userId", this.userID);
		soap.addProperty("password", this.password);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(soap);

		Log.i("NGVL", "Chamando Webservice");

		String url = currentWebserviceAddress;

		HttpTransportSE httpTransport = new HttpTransportSE(url, connectionTimeout);
		httpTransport.debug = true; 

		try {

			httpTransport.call("", envelope);

			String webMsg = envelope.getResponse().toString();
			
			if(!Boolean.valueOf(webMsg)){
				errorMessages
				.add(context.getResources().getString(R.string.msg_invalid_user_or_password));
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages
					.add(context.getResources().getString(R.string.msg_login_error));
		}
		return "";
	}

	@Override
	protected void onPreExecute() {

		listener.onTaskStarted(this.requestCode);
		
	}

	@Override
	protected void onPostExecute(String result) {

		listener.onTaskFinished(requestCode, errorMessages);

	}


}
