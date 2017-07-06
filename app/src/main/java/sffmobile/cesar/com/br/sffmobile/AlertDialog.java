package sffmobile.cesar.com.br.sffmobile;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AlertDialog extends DialogFragment{
	
	private String title;
	private String message;
	private Context context;
	private OnClickListener onClickListener;


	public AlertDialog() {
		super();
	}
	


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context).create();
		dialog.setTitle(this.title);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(this.message);
		
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,"OK", this.onClickListener);
		
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

	public OnClickListener getOnClickListener(){
		return this.onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener){
		this.onClickListener = onClickListener;
	}
}
