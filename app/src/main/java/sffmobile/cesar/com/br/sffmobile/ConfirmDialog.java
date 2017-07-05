package sffmobile.cesar.com.br.sffmobile;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class ConfirmDialog extends DialogFragment{
	
	private String title;
	private String message;
	private Context context;
	private OnClickListener onOkClickListener;
	private OnClickListener onCancelClickListener;
	
	
	public ConfirmDialog(String title, String message, Context context, OnClickListener onOkClickListener, OnClickListener onNoClickListener) {
		super();
		this.title = title;
		this.message = message;
		this.context = context;
		this.onOkClickListener = onOkClickListener;
		this.onCancelClickListener = onNoClickListener;
	}
	


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context).create();
		dialog.setTitle(this.title);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(this.message);
		
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"CANCELAR", this.onCancelClickListener);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", this.onOkClickListener);
		
		return dialog;
	}
	
	
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
