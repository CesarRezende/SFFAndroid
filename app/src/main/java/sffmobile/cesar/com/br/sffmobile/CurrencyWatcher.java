package sffmobile.cesar.com.br.sffmobile;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
public class CurrencyWatcher
implements TextWatcher {
    private boolean backspacePressed = false;
    private boolean inputFromKeyboard = false;
    private String inputText = "";
    private boolean internalTextChange = false;
    private int oldStartPosition = 0;
    private String oldText = "";
    private EditText ownerField = null;
    private int textChangeLength = 0;

    public CurrencyWatcher(EditText editText) {
        this.ownerField = editText;
    }

    private void clean() {
        this.oldText = "";
        this.oldStartPosition = 0;
        this.textChangeLength = 0;
        this.backspacePressed = false;
        this.inputText = "";
    }

    public void afterTextChanged(Editable editable) {
    	String text = editable.toString();
        if (this.internalTextChange) {
            this.internalTextChange = false;
            this.ownerField.setSelection(editable.length());
            return;
        }
        this.internalTextChange = true;
        if (this.backspacePressed) {
            text = this.oldText.substring(0, this.oldText.length() - 1).replaceAll(",", "").replaceAll("\\.", "");
            text = String.valueOf(text.substring(0, text.length() - 2)) + "." + text.substring(text.length() - 2);
            this.clean();
            this.ownerField.setText((CharSequence)SFFUtil.getFormattedNumber(Double.parseDouble(text)));
            return;
        }
        if (!this.inputFromKeyboard) {
            this.ownerField.setText((CharSequence)this.oldText);
            return;
        }
        text = String.valueOf(this.oldText.toString().replaceAll(",", "").replaceAll("\\.", "")) + this.inputText.toString().replaceAll(",", "").replaceAll("\\.", "");
        text = String.valueOf(text.substring(0, text.length() - 2)) + "." + text.substring(text.length() - 2);
        this.clean();
        this.ownerField.setText((CharSequence)SFFUtil.getFormattedNumber(Double.parseDouble(text)));
    }

    
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        boolean bl = true;
        if (this.internalTextChange) return;
        this.oldText = new String(charSequence.toString());
        this.oldStartPosition = start;
        this.textChangeLength = after;
        boolean bl2 = after <= 0;
        this.backspacePressed = bl2;
        bl2 = count <= 1 ? bl : false;
        this.inputFromKeyboard = bl2;
    }


    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (this.internalTextChange) return;
        if (this.backspacePressed) {
            this.inputText = "";
            return;
        }
        this.inputText = new String(charSequence.subSequence(this.oldStartPosition, this.oldStartPosition + this.textChangeLength).toString());
    }
}

