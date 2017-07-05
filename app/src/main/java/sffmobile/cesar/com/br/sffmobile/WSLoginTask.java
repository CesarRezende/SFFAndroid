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
			errorMessages.add("Não foi possivel estabelecer conexão com o servidor!");
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
				.add("Usuario ou senha invalido!");
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages
					.add("Desculpe, um erro ocorreu ao efetuar login no sistema.");
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
