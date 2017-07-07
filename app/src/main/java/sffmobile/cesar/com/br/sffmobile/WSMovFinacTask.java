package sffmobile.cesar.com.br.sffmobile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.util.Log;

public class WSMovFinacTask extends GenericWSTask {

	public static enum WebServiceMethod {
		CONSULTING, EDITING, INSERTING, DELETING, ACCOMPLISHING

	}

	private List<MovimentacaoFinanceira> movFinancList = null;

	public WSMovFinacTask(Context context, TaskListener listener,
			List<MovimentacaoFinanceira> listMovFinanc, int requestCode) {
		super(context, listener, requestCode);
		this.movFinancList = listMovFinanc;
	}

	@Override
	protected String doInBackground(Object... params) {

		switch ((WebServiceMethod) params[0]) {
		case CONSULTING:
			callWSGetMovimentacaoFinanceiraFromMonth();
			break;
		case EDITING:
			callWSEditMovimentacaoFinanceira((MovimentacaoFinanceira) params[1]);
			break;
		case INSERTING:
			callWSInsertMovimentacaoFinanceira((MovimentacaoFinanceira) params[1]);
			break;
		case DELETING:
			MovimentacaoFinanceira movFinanc = (MovimentacaoFinanceira) params[1];
			List<MovimentacaoFinanceira> listMovFinac = (List<MovimentacaoFinanceira>) params[2];

			List<MovimentacaoFinanceira> filtedListMovFinac = null;
			if (params[3] != null)
				filtedListMovFinac = (List<MovimentacaoFinanceira>) params[3];

			Boolean inSearch = (Boolean) params[4];

			callWSDeleteMovimentacaoFinanceira(movFinanc, listMovFinac,
					filtedListMovFinac, inSearch);
			break;
		case ACCOMPLISHING:
			callWSFulfillMovimentacaoFinanceira((MovimentacaoFinanceira) params[1]);
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

	protected void callWSGetMovimentacaoFinanceiraFromMonth() {
		SoapObject soap = new SoapObject("http://controller/",
				"getMovimentacaoFinanceiraFromMonth");

		if (!hasConnection()) {
			errorMessages
					.add("Não foi possivel estabelecer conexão com o servidor!");
			return;
		}

		Calendar now = Calendar.getInstance();
		SimpleDateFormat monthFormatter = new SimpleDateFormat("MM",
				new Locale("pt", "BR"));
		SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy",
				new Locale("pt", "BR"));


		soap.addProperty("yearMonth", Integer.toString(SFFApp.getYearMonth()));
		soap.addProperty("userId", this.userID);
		soap.addProperty("password", this.password);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(soap);

		Log.i("NGVL", "Chamando Webservice");

		String url = currentWebserviceAddress;

		HttpTransportSE httpTransport = new HttpTransportSE(url,
				connectionTimeout);
		httpTransport.debug = true;

		try {

			httpTransport.call("", envelope);

			String webMsg = envelope.getResponse().toString();

			if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|")))) {

				this.movFinancList.addAll(SFFUtil.parseWebServiceResponse(
						MovimentacaoFinanceira.class,
						webMsg.substring(webMsg.indexOf("|") + 1)));

			} else {
				errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessages
					.add("Desculpe, um erro ocorreu ao tentar consultar a lista de movimentações.");
		}
	}

	protected void callWSEditMovimentacaoFinanceira(
			MovimentacaoFinanceira movFinanc) {
		SoapObject soap = new SoapObject("http://controller/",
				"editMovimentacaoFinanceira");

		if (!hasConnection()) {
			errorMessages
					.add("Não foi possivel estabelecer conexão com o servidor!");
			return;
		}

		soap.addProperty("userId", this.userID);
		soap.addProperty("password", this.password);

		soap.addProperty("id", movFinanc.getId().toString());
		soap.addProperty("descricao", movFinanc.getDescricao());
		soap.addProperty("tipoMovimentacao", movFinanc.getTipoMovimentacao()
				.toString());
		soap.addProperty("valor",
				SFFUtil.getFormattedNumber(movFinanc.getValor()));
		soap.addProperty("juros",
				SFFUtil.getFormattedNumber(movFinanc.getJuros()));
		soap.addProperty("multa",
				SFFUtil.getFormattedNumber(movFinanc.getMulta()));
		soap.addProperty("desconto",
				SFFUtil.getFormattedNumber(movFinanc.getDesconto()));
		soap.addProperty("situacao", movFinanc.getSituacao().toString());
		soap.addProperty("dataPrevista",
				SFFUtil.getFormattedData(movFinanc.getDataPrevista()));
		soap.addProperty("dataRealizada",
				SFFUtil.getFormattedData(movFinanc.getDataRealizada()));
		soap.addProperty("tipoGastoId", movFinanc.getTipoGastoId().toString());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(soap);

		Log.i("NGVL", "Chamando Webservice");

		String url = currentWebserviceAddress;

		HttpTransportSE httpTransport = new HttpTransportSE(url,
				connectionTimeout);
		httpTransport.debug = true;

		try {

			httpTransport.call("", envelope);

			String webMsg = envelope.getResponse().toString();

			if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|")))) {

			} else {
				errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessages
					.add("Descupe, um erro ocorreu ao tentar salvar a movimentação.");
		}
	}

	protected void callWSInsertMovimentacaoFinanceira(
			MovimentacaoFinanceira movFinanc) {
		SoapObject soap = new SoapObject("http://controller/",
				"insertMovimentacaoFinanceira");

		if (!hasConnection()) {
			errorMessages
					.add("Não foi possivel estabelecer conexão com o servidor!");
			return;
		}

		soap.addProperty("userId", this.userID);
		soap.addProperty("password", this.password);

		soap.addProperty("id", movFinanc.getId().toString());
		soap.addProperty("descricao", movFinanc.getDescricao());
		soap.addProperty("tipoMovimentacao", movFinanc.getTipoMovimentacao()
				.toString());
		soap.addProperty("valor",
				SFFUtil.getFormattedNumber(movFinanc.getValor()));
		soap.addProperty("juros",
				SFFUtil.getFormattedNumber(movFinanc.getJuros()));
		soap.addProperty("multa",
				SFFUtil.getFormattedNumber(movFinanc.getMulta()));
		soap.addProperty("desconto",
				SFFUtil.getFormattedNumber(movFinanc.getDesconto()));
		soap.addProperty("situacao", movFinanc.getSituacao().toString());
		soap.addProperty("dataPrevista",
				SFFUtil.getFormattedData(movFinanc.getDataPrevista()));
		soap.addProperty("dataRealizada",
				SFFUtil.getFormattedData(movFinanc.getDataRealizada()));
		soap.addProperty("tipoGastoId", movFinanc.getTipoGastoId().toString());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(soap);

		Log.i("NGVL", "Chamando Webservice");

		String url = currentWebserviceAddress;

		HttpTransportSE httpTransport = new HttpTransportSE(url,
				connectionTimeout);
		httpTransport.debug = true;

		try {

			httpTransport.call("", envelope);

			String webMsg = envelope.getResponse().toString();

			if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|")))) {

				JSONObject json = new JSONObject(webMsg.substring(webMsg
						.indexOf("|") + 1));

				movFinanc.setId(json.getLong("id"));
				movFinanc.setNewRecord(false);

			} else {
				errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessages
					.add("Desculpe, um erro ocorreu ao tentar salvar a movimentação.");
		}
	}

	protected void callWSFulfillMovimentacaoFinanceira(MovimentacaoFinanceira movFinanc) {
        SoapObject soap = new SoapObject("http://controller/", "fulfillMovFinancExternal");
        
        if (hasConnection()) {
        	
            soap.addProperty("userId", this.userID);
            soap.addProperty("password", this.password);
            soap.addProperty("id", movFinanc.getId().toString());
            
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soap);
            
            Log.i("NGVL", "Chamando Webservice");
            
            HttpTransportSE httpTransport = new HttpTransportSE(currentWebserviceAddress, this.connectionTimeout);
            httpTransport.debug = true;
            try {
                httpTransport.call("", envelope);
                
                String webMsg = envelope.getResponse().toString();
                
                if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|")))) {
                    movFinanc.setSituacao(Character.valueOf('R'));
                    Calendar dataRealizada = Calendar.getInstance();
                    dataRealizada.set(dataRealizada.get(Calendar.YEAR), dataRealizada.get(Calendar.MONTH), dataRealizada.get(Calendar.DAY_OF_MONTH));
                    movFinanc.setDataRealizada(dataRealizada.getTime());
                    return;
                }
                this.errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                this.errorMessages.add("Desculpe, um erro ocorreu ao tentar realizar a movimentação.");
                return;
            }
        }else
        	this.errorMessages.add("N�o foi possivel estabelecer conex�o com o servidor!");
    }
	
	protected void callWSDeleteMovimentacaoFinanceira(
			MovimentacaoFinanceira movFinanc,
			List<MovimentacaoFinanceira> listMovFinac,
			List<MovimentacaoFinanceira> filtedListMovFinac, boolean inSearch) {
		SoapObject soap = new SoapObject("http://controller/",
				"removeMovFinancExternal");

		if (!hasConnection()) {
			errorMessages
					.add("Não foi possivel estabelecer conexão com o servidor!");
			return;
		}

		soap.addProperty("userId", this.userID);
		soap.addProperty("password", this.password);

		soap.addProperty("id", movFinanc.getId().toString());

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(soap);

		Log.i("NGVL", "Chamando Webservice");

		String url = currentWebserviceAddress;

		HttpTransportSE httpTransport = new HttpTransportSE(url,
				connectionTimeout);
		httpTransport.debug = true;

		try {

			httpTransport.call("", envelope);

			String webMsg = envelope.getResponse().toString();

			if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|")))) {

				listMovFinac.remove(movFinanc);

				if (inSearch)
					filtedListMovFinac.remove(movFinanc);

			} else {
				errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessages
					.add("Desculpe, um erro ocorreu ao tentar deletar a movimentação.");
		}
	}

}
