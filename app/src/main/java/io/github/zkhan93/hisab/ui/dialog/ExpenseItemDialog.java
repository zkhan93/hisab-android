package io.github.zkhan93.hisab.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.zkhan93.hisab.R;
import io.github.zkhan93.hisab.model.ExpenseItem;
import io.github.zkhan93.hisab.ui.MainActivity;

/**
 * Created by Zeeshan Khan on 6/26/2016.
 */
public class ExpenseItemDialog extends DialogFragment implements TextWatcher{

    public static final String TAG = ExpenseItemDialog.class.getSimpleName();

    @BindView(R.id.description)
    TextInputEditText description;
    @BindView(R.id.amount)
    TextInputEditText amount;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_create_expense);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_expense,
                null);
        ButterKnife.bind(this, view);
        description.addTextChangedListener(this);
        builder.setView(view);
        builder.setPositiveButton(R.string.label_create, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (validateValues(description.getText()
                        .toString(), amount.getText().toString())) {
                    ((MainActivity) getActivity()).createExpense(description.getText()
                            .toString(), Float.parseFloat(amount.getText().toString()),
                            ExpenseItem.ITEM_TYPE.SHARED,null,0);
                }else{

                }
            }
        }).setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    public boolean validateValues(String desc, String amt) {
        boolean result = true;
        try {
            if (desc == null || desc.isEmpty()) {
                description.setError(getString(R.string.err_empty_desc));
                description.requestFocus();
                result = false;
            }
            Float famt = Float.parseFloat(amt);
            if (famt <= 0) {
                amount.setError(getString(R.string.err_amount_non_zero_positive));
                amount.requestFocus();
                result = false;
            }
        } catch (NumberFormatException ex) {
            amount.setError(getString(R.string.err_invalid_amount));
            amount.requestFocus();
            result = false;
        }
        return result;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int len=editable.toString().length();
        if(len>100){
            editable.delete(100,len);
            description.setError(getString(R.string.err_long_desc));
        }
    }
}
