package sffmobile.cesar.com.br.sffmobile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import java.util.Date;

public class DateChooserActivity extends AppCompatActivity {
    public static final String DATE_STRING_PARAM_VALUE = "DATE_STRING_PARAM_VALUE";
    public static final String TEXT_FIELD_PARAM_ID = "TEXT_FIELD_PARAM_ID";
    private long currentDate;
    private int dateFieldId;

    public DateChooserActivity() {
        this.dateFieldId = 0;
        this.currentDate = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_chooser);
        this.dateFieldId = getIntent().getExtras().getInt(TEXT_FIELD_PARAM_ID);
        CalendarView calendar = (CalendarView) findViewById(R.id.calendarView);
        this.currentDate = calendar.getDate();
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
	            CalendarView calendarView = view;
	            if (DateChooserActivity.this.currentDate != calendarView.getDate()) {
	                DateChooserActivity.this.returnDateStrValue(SFFUtil.getFormattedData(new Date(calendarView.getDate())));
	            }
			}
			
		});
        calendar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
	            DateChooserActivity.this.returnDateStrValue(SFFUtil.getFormattedData(new Date(((CalendarView) view).getDate())));
	        }
		});
    }

    private void returnDateStrValue(String dataStrValue) {
        Intent intent = new Intent();
        intent.putExtra(DATE_STRING_PARAM_VALUE, dataStrValue);
        intent.putExtra(TEXT_FIELD_PARAM_ID, this.dateFieldId);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(TEXT_FIELD_PARAM_ID, this.dateFieldId);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
