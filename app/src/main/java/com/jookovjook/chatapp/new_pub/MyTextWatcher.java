package com.jookovjook.chatapp.new_pub;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher {
    private EditText editText;

    public MyTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);
        int cursorPosition = editText.getSelectionStart();
        /////

        String multiLines = editText.getText().toString();
        Log.i("editText", multiLines);
        //final SpannableStringBuilder sb = new SpannableStringBuilder(multiLines);
        //final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        //sb.setSpan(bss, 0, multiLines.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //editText.setText(sb);

        /////
        editText.setSelection(cursorPosition);
        editText.addTextChangedListener(this);
    }
}