package sffmobile.cesar.com.br.sffmobile;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.EditText;

public class SFFUtil {

	// Verifica se a String � null ou vazia ou s� tem espa�os em branco
    public static boolean isNullOrBlank(String s) {
        return (s == null || s.trim().equals(""));
    }

    // Verifica se a String � null ou vazia
    // Pode ser utilizado como suporte em APIs menores que 9 do android onde n�o est� disponivel o met�do de String isEmpty()
    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.equals(""));
    }
    
	public enum DateFormat {
		SHORT("dd/MM"), MEDIUM("dd/MM/yyyy"), LONG("dd/MM/yyyy HH:mm:ss");
		private String format;

		private DateFormat(String format) {
			this.setFormat(format);
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}
	}

	public static String getFormattedData(Date date) {

		return getFormattedData(date, DateFormat.MEDIUM);
	}

	public static String getFormattedData(Date date, DateFormat dateFormat) {

		String returnValue = "Sem Data";
		SimpleDateFormat dataFormatada = new SimpleDateFormat(
				dateFormat.getFormat(), new Locale("pt", "BR"));

		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);


			returnValue = dataFormatada.format(c.getTime());
		}
		return returnValue;
	}

	public static String getFormattedNumber(double number) {
		Locale locale = new Locale("pt", "BR");
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMinimumFractionDigits(2);
		return formatter.format(number);

	}

	public static <T> List<T> parseWebServiceResponse(Class<T> c, String webMsg) {

		List<T> list = new ArrayList<T>();
		String[] arrayString = webMsg.split(";");
		for (int i = 0; i < arrayString.length; i++) {

			JSONObject json;
			T t = null;
			try {
				t = c.newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

			for (Field field : c.getDeclaredFields()) {

				try {

					String methodName = "";

					if (field.getType().equals(List.class)
							|| GenericEntity.class.isAssignableFrom(field
									.getType())) {
						continue;
					}

					json = new JSONObject(arrayString[i]);
					Object fieldValue = json.get(field.getName());

					if (fieldValue.toString() != "null") {

						methodName = "set"
								+ field.getName().substring(0, 1).toUpperCase(new Locale("pt", "BR"))
								+ field.getName().substring(1);

						Method m = c.getDeclaredMethod(methodName,
								new Class[] { field.getType() });

						if (Date.class.isAssignableFrom(field.getType())) {
							String dateStr = (String) fieldValue;
							Calendar calendar = Calendar.getInstance();
							calendar.set(
									Integer.parseInt(dateStr.substring(6, 10)),
									Integer.parseInt(dateStr.substring(3, 5)) -1,
									Integer.parseInt(dateStr.substring(0, 2)));

							Date date = calendar.getTime();
							m.invoke(t, date);

						} else if (Character.class.isAssignableFrom(field
								.getType())) {

							Character character = fieldValue.toString().charAt(
									0);
							m.invoke(t, character);

						} else if (Long.class.isAssignableFrom(field.getType())) {

							Integer intValue = (Integer) fieldValue;

							m.invoke(t, new Long(intValue));

						} else if (double.class.isAssignableFrom(field
								.getType())) {

							Double doubleValue = (Double) fieldValue;

							m.invoke(t, doubleValue.doubleValue());

						} else if (boolean.class.isAssignableFrom(field
								.getType())) {

							Boolean booleanValue = (Boolean) fieldValue;

							m.invoke(t, booleanValue.booleanValue());

						} else {

							m.invoke(t, field.getType().cast(fieldValue));

						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {

					e.printStackTrace();
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (InvocationTargetException e) {

					e.printStackTrace();
				}

			}

			list.add(t);
		}

		return list;

	}

	public static Date getDateFromField(EditText field) {

		return getDateFromField(field, DateFormat.MEDIUM);
	}
	
	public static Date getDateFromField(EditText field, DateFormat dateFormat) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(
				DateFormat.MEDIUM.getFormat(), new Locale("pt", "BR"));

		try {
			date = format.parse(field.getText().toString());
		} catch (Exception e) {

		}

		return date;
	}
	
	public static double getDoubleFromField(EditText field) {
		double returnValue = 0;
		Locale locale = new Locale("pt", "BR");
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMinimumFractionDigits(2);

		try {
			returnValue = formatter.parse(field.getText().toString()).doubleValue();
		} catch (Exception e) {

		}

		return returnValue;
	}
	
	public static boolean IsDateThatYearMonth(Date date, int year, int month){
		boolean isDateThatYearMonth = false;
		
		Calendar dateC = Calendar.getInstance();
		dateC.setTime(date);
		
		isDateThatYearMonth = dateC.get(Calendar.YEAR) == year && (int)dateC.get(Calendar.MONTH) == (month -1);
		
		return isDateThatYearMonth;
		
	}
}

