package io.github.zkhan93.hisab.ui.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.zkhan93.hisab.R;
import io.github.zkhan93.hisab.model.ExpenseItem;
import io.github.zkhan93.hisab.ui.MainActivity;
import io.github.zkhan93.hisab.util.ImagePicker;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Zeeshan Khan on 6/26/2016.
 */
public class ExpenseItemDialog extends DialogFragment implements TextWatcher, View
        .OnClickListener, View.OnFocusChangeListener {

    public static final String TAG = ExpenseItemDialog.class.getSimpleName();

    @BindView(R.id.description)
    TextInputEditText description;

    @BindView(R.id.amount)
    TextInputEditText amount;

//    @BindView(R.id.addImage)
//    View btnAddImage;
//
//    @BindView(R.id.clickImage)
//    ImageButton btnClickImage;

    @BindView(R.id.image)
    ImageView image;

    private boolean imageAdded;
    private int currentEditTextId;
    private Bitmap selectedImage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_create_expense);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_expense,
                null);
        ButterKnife.bind(this, view);
        description.addTextChangedListener(this);
        amount.addTextChangedListener(this);
        builder.setView(view);
        description.setOnFocusChangeListener(this);
        amount.setOnFocusChangeListener(this);
        builder.setPositiveButton(R.string.label_create, new DialogInterface
                .OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((MainActivity) getActivity()).createExpense(description.getText()
                                .toString(), Float.parseFloat(amount.getText().toString()),
                        imageAdded,
                        ExpenseItem.ITEM_TYPE.SHARED, null, 0);

            }
        }).setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        image.setOnClickListener(this);
        imageAdded = false;
        if (savedInstanceState != null) {
            imageAdded = savedInstanceState.getBoolean("imageAdded", false);
            if (imageAdded) {
                selectedImage = ImagePicker.getSelectedBitmapImage();
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageBitmap(selectedImage);
            } else {
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable
                        .ic_add_a_photo_grey_500_24dp));
            }
        }
//        btnClickImage.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableIfValidInput();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("imageAdded", imageAdded);
    }

    /**
     * Enable created button without setting error messages
     */
    private void enableIfValidInput() {
        if (description.getText().toString().trim().isEmpty() || amount.getText().toString().trim
                ().isEmpty())
            ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        else
            ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        CharSequence charSequence = editable.toString();
        int len = editable.toString().length();
        /*Show error for only the filed being edited and not the other one whcih is not yet
        received focus*/
        switch (currentEditTextId) {
            case R.id.description:

                if (charSequence.toString().isEmpty()) {
                    description.setError(getString(R.string.err_required, "Description"));
                } else if (charSequence.toString().length() > 100) {
                    editable.delete(100, len);
                    description.setError("Keep the description short and crisp.");
                } else
                    description.setError(null);

                break;
            case R.id.amount:
                if (charSequence.toString().isEmpty()) {
                    amount.setError(getString(R.string.err_required, "Amount"));
                }
                try {
                    float amt = Float.parseFloat(charSequence.toString());
                    if (amt <= 0) {
                        amount.setError(getString(R.string.err_zero_amount));
                    } else
                        amount.setError(null);
                } catch (NumberFormatException ex) {
                    amount.setError(getString(R.string.err_invalid_amount));
                }
                break;
        }
        enableIfValidInput();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                // Show only images, no videos or anything else
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission
                        .CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    showSelectImage();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {
                        //TODO: explain  why you need permission
                        Toast.makeText(getActivity(), "allow camera permission", Toast
                                .LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest
                                .permission.CAMERA}, 0);
                    }
                }
                break;
            default:
                Log.d(TAG, "click not implemented");
        }
    }

    private void showSelectImage() {
        startActivityForResult(ImagePicker.getPickImageIntent(getActivity(), imageAdded), 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode != RESULT_OK) return;
            if (data != null && data.getBooleanExtra("remove_image", false)) {
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable
                        .ic_add_a_photo_grey_500_24dp));
                imageAdded = false;
                return;
            }
            selectedImage = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageBitmap(selectedImage);
            imageAdded = true;
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSelectImage();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            currentEditTextId = v.getId();
    }
}
