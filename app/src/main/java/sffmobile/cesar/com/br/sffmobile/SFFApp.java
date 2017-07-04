package sffmobile.cesar.com.br.sffmobile;

import android.app.Application;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SFFApp extends Application {
    private static int menuPosition;
    private static int yearMonth;
    private static int year;
    private static int month;

    static {
    	SFFApp.yearMonth = 0;
    	SFFApp.menuPosition = 0;
    }

    public static void setYearMonth(Calendar date) {
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM", new Locale("pt", "BR"));
        SFFApp.year = Integer.valueOf(new SimpleDateFormat("yyyy", new Locale("pt", "BR")).format(date.getTime())).intValue();
        SFFApp.month = Integer.valueOf(monthFormatter.format(date.getTime()));
        SFFApp.yearMonth = (SFFApp.year * 100) + SFFApp.month;
    }


	public static int getYearMonth() {
        return SFFApp.yearMonth;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onTerminate() {
        super.onTerminate();
    }

    public static int getMenuPosition() {
        return SFFApp.menuPosition;
    }

    public static void setMenuPosition(int menuPosition) {
    	SFFApp.menuPosition = menuPosition;
    }
    
    public static int getYear() {
		return SFFApp.year;
	}

	public static int getMonth() {
		return SFFApp.month;
	}

}

