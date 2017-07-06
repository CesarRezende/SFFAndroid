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


	public ConfirmDialog() {
		super();
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

	public String getTitle(){
		return this.title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public Context getContext(){
		return this.context;
	}

	public void setContext(Context context){
		this.context = context;
	}

	public OnClickListener getOnOkClickListener(){
		return this.onOkClickListener;
	}

	public void setOnOkClickListener(OnClickListener onOkClickListener){
		this.onOkClickListener = onOkClickListener;
	}

	public OnClickListener getOnCancelClickListener(){
		return this.onCancelClickListener;
	}

	public void setOnCancelClickListener(OnClickListener onCancelClickListener){
		this.onCancelClickListener = onCancelClickListener;
	}
}
