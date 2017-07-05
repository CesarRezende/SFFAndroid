package sffmobile.cesar.com.br.sffmobile;

import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

public class WSTipoGastoTask extends GenericWSTask {

	public static enum WebServiceMethod {
		CONSULTING,

	}

	private List<TipoGasto> listTipoGasto = null;
	
	
	public WSTipoGastoTask(Context context, TaskListener listener, List<TipoGasto> listTipoGasto, int requestCode) {
		super(context, listener, requestCode);
		this.listTipoGasto = listTipoGasto;
	}

	@Override
	protected String doInBackground(Object... params) {

		switch ((WebServiceMethod) params[0]) {
		case CONSULTING:
			callWSGetAllTipoGasto();
			break;

		default:
			break;
		}
		return "";
	}

	@Override
	protected void onPreExecute() {

		listener.onTaskStarted(this.requestCode);
		
	}

	@Override
	protected void onPostExecute(String result) {

		listener.onTaskFinished(this.requestCode, this.errorMessages);

	}

	protected void callWSGetAllTipoGasto() {
		SoapObject soap = new SoapObject("http://controller/",
				"getAllTipoGasto");

		if (!hasConnection()) {
			errorMessages.add("N�o foi possivel estabelecer conex�o com o servidor!");
			return;
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
			
			if(Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|")))){
				
			this.listTipoGasto.addAll(SFFUtil.parseWebServiceResponse(
					TipoGasto.class, webMsg.substring(webMsg.indexOf("|") + 1)));
			
			}else{
				errorMessages
				.add(webMsg.substring(webMsg.indexOf("|") + 1));
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			errorMessages
					.add("Descupe, um erro ocorreu ao tentar consultar a lista de tipo de gasto.");
		}
	}
}
