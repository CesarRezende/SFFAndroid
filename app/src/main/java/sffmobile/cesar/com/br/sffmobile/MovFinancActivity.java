package sffmobile.cesar.com.br.sffmobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class MovFinancActivity extends AppCompatActivity implements TaskListener {

	private MovimentacaoFinanceira movFinanc;
	private EditText idField;
	private EditText releaseDateField;
	private RadioButton statusExpectedOpt;
	private RadioButton statusAccomplishOpt;
	private EditText expectedDateField;
	private EditText fulfillmentDateField;
	private CheckBox manualChkField;
	private RadioButton movtypeCreditOpt;
	private RadioButton movtypeDebitOpt;
	private EditText descrField;
	private EditText valueField;
	private EditText discountField;
	private EditText fineField;
	private EditText interestRateField;
	private Spinner spendTypeField;
	private boolean ocurredError = false;
	private ArrayList<TipoGasto> tipoGastoList = new ArrayList<TipoGasto>();
	private ProgressDialog pDialog;
	private int spendTypeFieldPos = -1;
	private static final int TIPOGASTO_REQUEST_TASK = 1;
	private static final int MOVFINANC_EDIT_REQUEST_TASK = 2;
	private static final int MOVFINANC_INSERT_REQUEST_TASK = 3;
	private static final int CALENDAR_REQUEST_TASK = 4;

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putInt("spendTypeFieldPosition",
				spendTypeField.getSelectedItemPosition());
		savedInstanceState.putParcelableArrayList("tipoGastoList",
				tipoGastoList);
		savedInstanceState.putSerializable("movFinanc", this.movFinanc);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CALENDAR_REQUEST_TASK && data != null) {
            EditText dateField = (EditText) findViewById(data.getIntExtra(DateChooserActivity.TEXT_FIELD_PARAM_ID, 0));
            if (resultCode == RESULT_OK) {
                String dateStr = data.getStringExtra(DateChooserActivity.DATE_STRING_PARAM_VALUE);
                if (dateStr != null) {
                    dateField.setText(dateStr);
                }
            }
        }
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_movfinac);

		Bundle b = this.getIntent().getExtras();

		idField = (EditText) findViewById(R.id.movfinac_id_field);
		releaseDateField = (EditText) findViewById(R.id.movfinanc_releasedate_field);
		statusExpectedOpt = (RadioButton) findViewById(R.id.movfinanc_status_expected_opt);
		statusAccomplishOpt = (RadioButton) findViewById(R.id.movfinanc_status_accomplish_opt);
		expectedDateField = (EditText) findViewById(R.id.movfinanc_expecteddate_field);
		fulfillmentDateField = (EditText) findViewById(R.id.movfinanc_fulfillmentdate_field);
		manualChkField = (CheckBox) findViewById(R.id.movfinanc_manual_field);
		movtypeCreditOpt = (RadioButton) findViewById(R.id.movfinanc_movtype_credit_opt);
		movtypeDebitOpt = (RadioButton) findViewById(R.id.movfinanc_movtype_debit_opt);
		descrField = (EditText) findViewById(R.id.movfinanc_descr_field);
		valueField = (EditText) findViewById(R.id.movfinanc_value_field);
		discountField = (EditText) findViewById(R.id.movfinanc_discount_field);
		fineField = (EditText) findViewById(R.id.movfinanc_fine_field);
		interestRateField = (EditText) findViewById(R.id.movfinanc_interest_rate_field);
		spendTypeField = (Spinner) findViewById(R.id.movfinanc_spend_type_field);

		if (savedInstanceState == null) {

			if (b != null)
				this.movFinanc = (MovimentacaoFinanceira) b
						.getSerializable("movFinanc");

			if (tipoGastoList.size() <= 0
					&& !this.movFinanc.getTipoMovimentacao().equals('C')) {
				loadTipoGasto();
			}

		} else {

			this.spendTypeFieldPos = savedInstanceState
					.getInt("spendTypeFieldPosition");
			this.tipoGastoList = savedInstanceState
					.getParcelableArrayList("tipoGastoList");

			this.movFinanc = (MovimentacaoFinanceira) savedInstanceState
					.getSerializable("movFinanc");

			renderSpendTypeField();

		}

		if (this.movFinanc != null) {

			idField.setText(this.movFinanc.getId().toString());
			releaseDateField.setText(SFFUtil.getFormattedData(
					this.movFinanc.getDataLancamento(),
					SFFUtil.DateFormat.MEDIUM));
			expectedDateField
					.setText(SFFUtil.getFormattedData(
							this.movFinanc.getDataPrevista(),
							SFFUtil.DateFormat.MEDIUM));
			fulfillmentDateField.setText(SFFUtil.getFormattedData(
					this.movFinanc.getDataRealizada(),
					SFFUtil.DateFormat.MEDIUM));
			descrField.setText(this.movFinanc.getDescricao());
			valueField.setText(SFFUtil.getFormattedNumber(this.movFinanc
					.getValor()));
			discountField.setText(SFFUtil.getFormattedNumber(this.movFinanc
					.getDesconto()));
			fineField.setText(SFFUtil.getFormattedNumber(this.movFinanc
					.getMulta()));
			interestRateField.setText(SFFUtil.getFormattedNumber(this.movFinanc
					.getJuros()));

			manualChkField.setChecked(this.movFinanc.isManual());

			statusExpectedOpt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					movFinanc.setSituacao('P');
					movFinanc.setDataRealizada(null);
					fulfillmentDateField.setText("");
					fulfillmentDateField.setEnabled(false);
				}
			});
            this.statusAccomplishOpt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						movFinanc.setSituacao('R');
						movFinanc.setDataRealizada(null);
						fulfillmentDateField.setText("");
						fulfillmentDateField.setEnabled(true);
					
				}
			});
            this.movtypeCreditOpt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MovFinancActivity.this.movFinanc.setTipoMovimentacao(Character.valueOf('C'));
	                MovFinancActivity.this.spendTypeFieldPos = -1;
	                MovFinancActivity.this.tipoGastoList = new ArrayList();
	                MovFinancActivity.this.renderSpendTypeField();
	                MovFinancActivity.this.spendTypeField.setEnabled(false);
					
				}
			});
            this.movtypeDebitOpt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					movFinanc.setTipoMovimentacao(Character.valueOf('D'));
					loadTipoGasto();
					spendTypeField.setEnabled(true);

				}
			});
           
            this.expectedDateField.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
		                view.clearFocus();
		                Intent dateChooserActiviryCaller = new Intent(MovFinancActivity.this, DateChooserActivity.class);
		                dateChooserActiviryCaller.putExtra(DateChooserActivity.TEXT_FIELD_PARAM_ID, view.getId());
		                startActivityForResult(dateChooserActiviryCaller, MovFinancActivity.CALENDAR_REQUEST_TASK);
		            }
		            return true;
				}
			});
            this.fulfillmentDateField.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
		                view.clearFocus();
		                Intent dateChooserActiviryCaller = new Intent(MovFinancActivity.this, DateChooserActivity.class);
		                dateChooserActiviryCaller.putExtra(DateChooserActivity.TEXT_FIELD_PARAM_ID, view.getId());
		                startActivityForResult(dateChooserActiviryCaller, MovFinancActivity.CALENDAR_REQUEST_TASK);
		            }
		            return true;
				}
			});
			this.valueField.addTextChangedListener(new CurrencyWatcher(this.valueField));
            this.fineField.addTextChangedListener(new CurrencyWatcher(this.fineField));
            this.discountField.addTextChangedListener(new CurrencyWatcher(this.discountField));
            this.interestRateField.addTextChangedListener(new CurrencyWatcher(this.interestRateField));
            
			if (this.movFinanc.getSituacao().equals('R')) {
				statusAccomplishOpt.setChecked(true);
			} else {
				statusExpectedOpt.setChecked(true);
				fulfillmentDateField.setEnabled(false);
			}

			if (this.movFinanc.getTipoMovimentacao().equals('C')) {
				movtypeCreditOpt.setChecked(true);
				spendTypeField.setEnabled(false);

			} else
				movtypeDebitOpt.setChecked(true);

			if (this.movFinanc.getSaidaFixaId() != null
					|| this.movFinanc.getSaidaVariavelId() != null
					|| this.movFinanc.getEntradaFixaId() != null
					|| this.movFinanc.getSaidaVariavelId() != null) {

				descrField.setEnabled(false);
				expectedDateField.setEnabled(false);
				movtypeCreditOpt.setEnabled(false);
				movtypeDebitOpt.setEnabled(false);
				spendTypeField.setEnabled(false);
			}

		}

	}

	private void loadTipoGasto() {
		WSTipoGastoTask task = new WSTipoGastoTask(MovFinancActivity.this,
				this, tipoGastoList, TIPOGASTO_REQUEST_TASK);
		task.execute(WSTipoGastoTask.WebServiceMethod.CONSULTING);
	}

	private void lockScreenOrientation() {
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	private void unlockScreenOrientation() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mov_financ, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean pressedCancel = item.getItemId() == R.id.movfinanc_cancel_button;
		boolean pressedOK = item.getItemId() == R.id.movfinanc_ok_button;

		if (pressedCancel) {

			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		} else if (pressedOK) {

			String errorMessage = "";
			boolean firstError = true;

			if (descrField.getText().toString().trim().equals("")) {
				if (firstError)
					errorMessage += "*Campo Descri��o � obrigat�rio";
				else
					errorMessage += "\n*Campo Descri��o � obrigat�rio";

				firstError = false;
				ocurredError = true;
			}

			if (!descrField.getText().toString().trim().equals("")
					&& descrField.getText().toString().trim().length() < 3) {
				if (firstError)
					errorMessage += "*O campo 'Descri��o' deve conter de 3 a 30 caracteres";
				else
					errorMessage += "\n*O campo 'Descri��o' deve conter de 3 a 30 caracteres";

				firstError = false;
				ocurredError = true;
			}

			if (valueField.getText().toString().trim().equals("")) {
				if (firstError)
					errorMessage += "*Campo Valor � obrigat�rio";
				else
					errorMessage += "\n*Campo Valor � obrigat�rio";

				firstError = false;
				ocurredError = true;
			}

			if (!valueField.getText().toString().trim().equals("")
					&& SFFUtil.getDoubleFromField(valueField) <= 0.0) {
				if (firstError)
					errorMessage += "*Campo Valor deve ser maior que zero";
				else
					errorMessage += "\n*Campo Valor deve ser maior que zero";

				firstError = false;
				ocurredError = true;
			}

			if (expectedDateField.getText().toString().trim().equals("")) {
				if (firstError)
					errorMessage += "*Campo Data Prevista � obrigat�rio";
				else
					errorMessage += "\n*Campo Data Prevista � obrigat�rio";

				firstError = false;
				ocurredError = true;
			}

			if (!expectedDateField.getText().toString().trim()
					.matches("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]")) {
				if (firstError)
					errorMessage += "*Formato da Data Prevista � invalido";
				else
					errorMessage += "\n*Formato da Data Prevista � invalido";

				firstError = false;
				ocurredError = true;
			}

			if (statusAccomplishOpt.isChecked())
				if (fulfillmentDateField.getText().toString().trim().equals("")) {
					if (firstError)
						errorMessage += "*Campo Data Realizada � obrigat�rio";
					else
						errorMessage += "\n*Campo Data Realizada � obrigat�rio";

					firstError = false;
					ocurredError = true;
				}

			if (statusAccomplishOpt.isChecked())
				if (!fulfillmentDateField.getText().toString().trim()
						.matches("[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]")) {
					if (firstError)
						errorMessage += "*Formato da Data Realizada � invalido";
					else
						errorMessage += "\n*Formato da Data Realizada � invalido";

					firstError = false;
					ocurredError = true;
				}

			if (statusAccomplishOpt.isChecked()) {
				Calendar c = Calendar.getInstance();
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
				Date limitFulfillmentDate = c.getTime();
				if (SFFUtil.getDateFromField(fulfillmentDateField).after(
						limitFulfillmentDate)) {
					if (firstError)
						errorMessage += "*A Data Realizada n�o deve ser superior a data atual";
					else
						errorMessage += "\n*A Data Realizada n�o deve ser superior a data atual";

					firstError = false;
					ocurredError = true;
				}

			}

			AlertDialog dialog = new AlertDialog();
			dialog.setTitle("Erro");
			dialog.setContext( MovFinancActivity.this);
			dialog.setMessage(errorMessage);
			dialog.setOnClickListener(
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							ocurredError = false;
							unlockScreenOrientation();
						}
					});

			if (ocurredError) {
				lockScreenOrientation();
				dialog.show(getFragmentManager(), "Erro");
				return true;
			}

			populateMovFinanc();

			WSMovFinacTask task = new WSMovFinacTask(MovFinancActivity.this,
					this, null,
					movFinanc.isNewRecord() ? MOVFINANC_INSERT_REQUEST_TASK
							: MOVFINANC_EDIT_REQUEST_TASK);
			task.execute(
					movFinanc.isNewRecord() ? WSMovFinacTask.WebServiceMethod.INSERTING
							: WSMovFinacTask.WebServiceMethod.EDITING,
					movFinanc);

		}

		return super.onOptionsItemSelected(item);
	}

	private void populateMovFinanc() {

		// this.movFinanc.setDataLancamento(SFFUtil.getDateFromField(releaseDateField));
		this.movFinanc.setDataPrevista(SFFUtil
				.getDateFromField(expectedDateField));
		this.movFinanc.setDataRealizada(SFFUtil
				.getDateFromField(fulfillmentDateField));

		if (statusAccomplishOpt.isChecked())
			this.movFinanc.setSituacao('R');
		else
			this.movFinanc.setSituacao('P');

		if (movtypeCreditOpt.isChecked())
			this.movFinanc.setTipoMovimentacao('C');
		else
			this.movFinanc.setTipoMovimentacao('D');

		this.movFinanc.setDescricao(descrField.getText().toString().trim());

		this.movFinanc.setManual(manualChkField.isChecked());

		this.movFinanc.setValor(SFFUtil.getDoubleFromField(valueField));

		this.movFinanc.setDesconto(SFFUtil.getDoubleFromField(discountField));

		this.movFinanc.setMulta(SFFUtil.getDoubleFromField(fineField));

		this.movFinanc.setJuros(SFFUtil.getDoubleFromField(interestRateField));

		if (this.movFinanc.getTipoMovimentacao().equals('D')) {

			TipoGasto tipoGasto = tipoGastoList.get(spendTypeField
					.getSelectedItemPosition());
			this.movFinanc.setTipoGasto(tipoGasto);

			if (tipoGasto != null) {

				this.movFinanc.setTipoGastoId(tipoGasto.getId());
			} else {
				this.movFinanc.setTipoGastoId(Long.valueOf(0L));

			}
		} else {
			this.movFinanc.setTipoGasto(null);
			this.movFinanc.setTipoGastoId(Long.valueOf(0L));
		}

	}

	@Override
	public void onTaskStarted(int requestCode) {
		lockScreenOrientation();
		if (requestCode == MOVFINANC_EDIT_REQUEST_TASK
				|| requestCode == MOVFINANC_INSERT_REQUEST_TASK)
			pDialog = ProgressDialog.show(MovFinancActivity.this, "Salvando",
					"Salvando. Por favor aguarde...", true);
		else
			pDialog = ProgressDialog.show(MovFinancActivity.this, "Carregando",
					"Carregando. Por favor aguarde...", true);

	}

	private void renderSpendTypeField() {

		TipoGastoArrayAdapter adapter = new TipoGastoArrayAdapter(
				MovFinancActivity.this, R.layout.simple_spinner_item,
				R.id.textview1, tipoGastoList);

		spendTypeField.setAdapter(adapter);

		if (spendTypeFieldPos == -1) {

			TipoGasto tipoGasto = TipoGasto.findTipoGasto(tipoGastoList,
					this.movFinanc.getTipoGastoId());
			if (tipoGasto != null) {
				spendTypeFieldPos = adapter.getPosition(tipoGasto);
				spendTypeField.setSelection(spendTypeFieldPos);
			}
		} else {
			spendTypeField.setSelection(spendTypeFieldPos);
		}

	}

	@Override
	public void onTaskFinished(int requestCode, List<String> errorMessages) {

		if (this.pDialog.isShowing())
			this.pDialog.dismiss();

		if (errorMessages == null || errorMessages.size() <= 0) {

			if (requestCode == TIPOGASTO_REQUEST_TASK) {

				Collections.sort(tipoGastoList, TipoGasto.getComparator());
				renderSpendTypeField();

			} else if (requestCode == MOVFINANC_EDIT_REQUEST_TASK) {

				Intent intent = new Intent();
				intent.putExtra("movFinanc", movFinanc);
				setResult(RESULT_OK, intent);
				finish();

			} else if (requestCode == MOVFINANC_INSERT_REQUEST_TASK) {

				Intent intent = new Intent();
				intent.putExtra("movFinanc", movFinanc);
				setResult(RESULT_OK, intent);
				finish();
			}

			unlockScreenOrientation();

		} else {

			String errorMessage = "";
			for (String msg : errorMessages) {

				if (msg.equals(errorMessages.get(0)))
					errorMessage += msg;
				else
					errorMessage += "\n" + msg;

			}

			AlertDialog dialog = new AlertDialog();
			dialog.setTitle("Erro");
			dialog.setContext( MovFinancActivity.this);
			dialog.setMessage(errorMessage);
			dialog.setOnClickListener(
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							unlockScreenOrientation();
						}
					});

			dialog.show(getFragmentManager(), "Erro");

		}

	}

	public static class TipoGastoArrayAdapter extends ArrayAdapter<TipoGasto> {

		private final Context context;
		private final List<TipoGasto> tipoGastoList;

		public TipoGastoArrayAdapter(Context context, int resource,
				int textViewResourceId, List<TipoGasto> tipoGastoList) {
			super(context, resource, textViewResourceId, tipoGastoList);
			this.context = context;
			this.tipoGastoList = tipoGastoList;
		}

		@Override
		public boolean isEnabled(int position) {
			return !tipoGastoList.get(position).isDesativado();
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@SuppressLint({ "ViewHolder", "NewApi" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.simple_spinner_item,
					parent, false);

			TipoGasto tipoGasto = tipoGastoList.get(position);

			TextView descField = (TextView) rowView
					.findViewById(R.id.spinner_item_descr);

			descField.setText(tipoGasto.getDescricao());

			return rowView;
		}

	}

}
