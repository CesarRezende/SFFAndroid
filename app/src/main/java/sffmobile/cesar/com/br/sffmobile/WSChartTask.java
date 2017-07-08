package sffmobile.cesar.com.br.sffmobile;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;

public class WSChartTask extends GenericWSTask {

    public WSChartTask(Context context, TaskListener listener, int requestCode) {
        super(context, listener, requestCode);
    }

    protected String doInBackground(Object... params) {
        callWSGetCurrentSpendingsChart((ArrayList) params[0]);

        if(errorMessages.size() <= 0)
        callWSGetSpendingsChart((ArrayList) params[0]);

        if(errorMessages.size() <= 0)
        callWSGetTipoGastoChart((ArrayList) params[0]);

        return XmlPullParser.NO_NAMESPACE;
    }

    protected void onPreExecute() {
        this.listener.onTaskStarted(this.requestCode);
    }

    protected void onPostExecute(String result) {
        this.listener.onTaskFinished(this.requestCode, this.errorMessages);
    }

    protected void callWSGetTipoGastoChart(ArrayList<ChartItem> chartList) {
        SoapObject soapObject = new SoapObject("http://controller/", "getTipoGastoChart");
        
        if (hasConnection()) {
        	
            soapObject.addProperty("yearMonth", Integer.toString(SFFApp.getYearMonth()));
            soapObject.addProperty("userId", this.userID);
            soapObject.addProperty("password", this.password);
            
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapObject);
            Log.i("NGVL", "Chamando Webservice");
            HttpTransportSE httpTransport = new HttpTransportSE(currentWebserviceAddress, this.connectionTimeout);
            httpTransport.debug = true;
            
            try {
            	
                httpTransport.call("", envelope);
                
                String webMsg = envelope.getResponse().toString();
                
                if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|"))).booleanValue()) {
                	
                    JSONObject json = new JSONObject(webMsg.substring(webMsg.indexOf("|") + 1));
                    
                    ArrayList<Entry> chartValues = new ArrayList();
                    List chartLegends = new ArrayList();
                    ArrayList<Double> pieChartValues = new ArrayList();
                    JSONArray charData = json.getJSONArray("data");
                    for (int i = 0; i < charData.length(); i++) {
                        JSONArray charItem = charData.getJSONArray(i);
                        chartLegends.add(charItem.getString(0));
                        chartValues.add(new Entry((float) charItem.getDouble(1), i));
                        pieChartValues.add(Double.valueOf(charItem.getDouble(1)));
                    }
                    PieDataSet dataSet = new PieDataSet(chartValues, json.getString("title"));
                    
                    ArrayList<Integer> colors = new ArrayList();
                    
                    colors.add(Color.rgb(220, 57, 18));
                    colors.add(Color.rgb(51, 102, 204));
                    
                    for (int color : ColorTemplate.PASTEL_COLORS) {
                        colors.add(color);
                    }
                    for (int color : ColorTemplate.COLORFUL_COLORS) {
                        colors.add(color);
                    }
                    for (int color : ColorTemplate.JOYFUL_COLORS) {
                        colors.add(color);
                    }
                    for (int color : ColorTemplate.VORDIPLOM_COLORS) {
                        colors.add(color);
                    }
                    for (int color : ColorTemplate.LIBERTY_COLORS) {
                        colors.add(color);
                    }
                    
                    dataSet.setColors((List) colors);
                    PieData data = new PieData(chartLegends, dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextColor(ViewCompat.MEASURED_STATE_MASK);
                    chartList.add(new PieChartItem(data, this.context, pieChartValues));
                    return;
                }
                this.errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.errorMessages.add(context.getResources().getString(R.string.msg_could_not_conect_to_server));
    }

    protected void callWSGetSpendingsChart(ArrayList<ChartItem> chartList) {
        SoapObject soapObject = new SoapObject("http://controller/", "getSpendingsChart");
        if (hasConnection()) {
        	
            soapObject.addProperty("yearMonth", Integer.toString(SFFApp.getYearMonth()));
            soapObject.addProperty("userId", this.userID);
            soapObject.addProperty("password", this.password);
            
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapObject);
            Log.i("NGVL", "Chamando Webservice");
            HttpTransportSE httpTransport = new HttpTransportSE(currentWebserviceAddress, this.connectionTimeout);
            httpTransport.debug = true;
            try {
                httpTransport.call(XmlPullParser.NO_NAMESPACE, envelope);
                String webMsg = envelope.getResponse().toString();
                if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|"))).booleanValue()) {
                    int i;
                    JSONObject json = new JSONObject(webMsg.substring(webMsg.indexOf("|") + 1));
                    ArrayList<Entry> chartValues = new ArrayList();
                    List chartLegends = new ArrayList();
                    ArrayList<Double> pieChartValues = new ArrayList();
                    JSONArray chartData = json.getJSONArray("data");
                    for (i = 0; i < chartData.length(); i++) {
                        JSONArray chartItem = chartData.getJSONArray(i);
                        chartLegends.add(chartItem.getString(0));
                        chartValues.add(new Entry((float) chartItem.getDouble(1), i));
                    }
                    JSONArray chartValuesArray = json.getJSONArray("values");
                    for (i = 0; i < chartValuesArray.length(); i++) {
                        pieChartValues.add(Double.valueOf(chartValuesArray.getJSONArray(i).getDouble(1)));
                    }
                    PieDataSet dataSet = new PieDataSet(chartValues, json.getString("title"));
                    ArrayList<Integer> colors = new ArrayList();
                    colors.add(Integer.valueOf(Color.rgb(220, 57, 18)));
                    colors.add(Integer.valueOf(Color.rgb(51, 102, 204)));
                    dataSet.setColors((List) colors);
                    PieData data = new PieData(chartLegends, dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextColor(ViewCompat.MEASURED_STATE_MASK);
                    PieChartItem pieChartItem = new PieChartItem(data, this.context, pieChartValues);
                    pieChartItem.setCalcSaldo(true);
                    chartList.add(pieChartItem);
                    return;
                }
                this.errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.errorMessages.add(context.getResources().getString(R.string.msg_could_not_conect_to_server));
    }

    protected void callWSGetCurrentSpendingsChart(ArrayList<ChartItem> chartList) {
        SoapObject soapObject = new SoapObject("http://controller/", "getCurrentSpendingsChart");
        if (hasConnection()) {
            soapObject.addProperty("yearMonth", Integer.toString(SFFApp.getYearMonth()));
            soapObject.addProperty("userId", this.userID);
            soapObject.addProperty("password", this.password);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapObject);
            Log.i("NGVL", "Chamando Webservice");
            HttpTransportSE httpTransport = new HttpTransportSE(currentWebserviceAddress, this.connectionTimeout);
            httpTransport.debug = true;
            try {
                httpTransport.call(XmlPullParser.NO_NAMESPACE, envelope);
                String webMsg = envelope.getResponse().toString();
                if (Boolean.valueOf(webMsg.substring(0, webMsg.indexOf("|"))).booleanValue()) {
                    int i;
                    JSONObject json = new JSONObject(webMsg.substring(webMsg.indexOf("|") + 1));
                    ArrayList<Entry> chartValues = new ArrayList();
                    List chartLegends = new ArrayList();
                    ArrayList<Double> pieChartValues = new ArrayList();
                    JSONArray chartData = json.getJSONArray("data");
                    for (i = 0; i < chartData.length(); i++) {
                        JSONArray chartItem = chartData.getJSONArray(i);
                        chartLegends.add(chartItem.getString(0));
                        chartValues.add(new Entry((float) chartItem.getDouble(1), i));
                    }
                    JSONArray chartValuesArray = json.getJSONArray("values");
                    for (i = 0; i < chartValuesArray.length(); i++) {
                        pieChartValues.add(Double.valueOf(chartValuesArray.getJSONArray(i).getDouble(1)));
                    }
                    PieDataSet dataSet = new PieDataSet(chartValues, json.getString("title"));
                    ArrayList<Integer> colors = new ArrayList();
                    colors.add(Integer.valueOf(Color.rgb(220, 57, 18)));
                    colors.add(Integer.valueOf(Color.rgb(51, 102, 204)));
                    dataSet.setColors((List) colors);
                    PieData data = new PieData(chartLegends, dataSet);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextColor(ViewCompat.MEASURED_STATE_MASK);
                    PieChartItem pieChartItem = new PieChartItem(data, this.context, pieChartValues);
                    pieChartItem.setCalcSaldo(true);
                    chartList.add(pieChartItem);
                    return;
                }
                this.errorMessages.add(webMsg.substring(webMsg.indexOf("|") + 1));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.errorMessages.add(context.getResources().getString(R.string.msg_could_not_conect_to_server));
    }
}
