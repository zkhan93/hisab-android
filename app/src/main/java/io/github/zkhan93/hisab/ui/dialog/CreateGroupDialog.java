package io.github.zkhan93.hisab.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.zkhan93.hisab.R;
import io.github.zkhan93.hisab.ui.GroupsActivity;

/**
 * Created by Zeeshan Khan on 6/26/2016.
 */
public class CreateGroupDialog extends DialogFragment {
    @BindView(R.id.group_name)
    EditText groupName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_create_grp);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_group,
                null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        builder.setPositiveButton(R.string.label_create, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((GroupsActivity) getActivity()).createGroup(groupName.getText().toString());
            }
        }).setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }
}