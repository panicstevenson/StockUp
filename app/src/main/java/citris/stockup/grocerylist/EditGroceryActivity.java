package citris.stockup.grocerylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import citris.stockup.R;
import citris.stockup.groceries.Grocery;
import citris.stockup.scanner.CameraActivity;

/**
 * Created by Panic on 4/25/2015.
 */
public class EditGroceryActivity extends AddGroceryActivity {
    private EditText groceryNameEditText;
    private EditText brandEditText;
    private EditText categoryEditText;
    private Button editButton;
    private Button cancelButton;
    protected boolean changesPending;
    private AlertDialog unsavedChangesDialog;
    private Spinner quantityIntSpinner;
    private Spinner ttlIntSpinner;
    private Spinner quantityTypeSpinner;
    private Spinner ttlTypeSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_grocery);
        setViews();
        getActionBar().hide();
    }

    private void editGrocery(int pos) {
        String groceryName = groceryNameEditText.getText().toString();
        int ttlType = ttlTypeSpinner.getSelectedItemPosition();
        int ttlInt = ttlIntSpinner.getSelectedItemPosition() + 1;
        int quantityType = quantityTypeSpinner.getSelectedItemPosition();
        int quantityInt = quantityIntSpinner.getSelectedItemPosition() + 1;
        String brandName = brandEditText.getText().toString();
        String categoryName = categoryEditText.getText().toString();

        if (!groceryName.isEmpty()) {
            getGroceryListApplication().editGrocery(pos, groceryName, quantityInt, quantityType, brandName, ttlInt, ttlType, categoryName);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: Invalid grocery name.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancel(final int pos) {
        if (changesPending){
            unsavedChangesDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.unsaved_changes_title)
                    .setMessage(R.string.unsaved_changes_message)
//          Add
                    .setNegativeButton(R.string.button_add, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editGrocery(pos);
                        }
                    })
//          Cancel
                    .setPositiveButton(R.string.button_cancel, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            unsavedChangesDialog.cancel();
                        }
                    })
//          Discard
                    .setNeutralButton(R.string.button_discard, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            unsavedChangesDialog.show();
        } else {
            finish();
        }
    }

    private void setViews() {
        final Grocery g = getIntent().getParcelableExtra("tmpGrocery");
        final int pos = getIntent().getIntExtra("position", 0);

        groceryNameEditText = (EditText)findViewById(R.id.grocery_edit_name);
        quantityIntSpinner = (Spinner)findViewById(R.id.quantity_int);
        quantityTypeSpinner = (Spinner)findViewById(R.id.quantity_type);
        brandEditText = (EditText)findViewById(R.id.grocery_edit_brand);
        ttlIntSpinner = (Spinner)findViewById(R.id.ttl_int);
        ttlTypeSpinner = (Spinner)findViewById(R.id.ttl_type);
        categoryEditText = (EditText)findViewById(R.id.edit_category);
        editButton = (Button)findViewById(R.id.edit_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);

        groceryNameEditText.setText(g.getName());
        quantityIntSpinner.setSelection(g.getQuantityInt() - 1);
        quantityTypeSpinner.setSelection(g.getQuantityTypePos());
        brandEditText.setText(g.getBrand());
        ttlIntSpinner.setSelection(g.getTtlInt() - 1);
        ttlTypeSpinner.setSelection(g.getTtlTypePos());
        categoryEditText.setText(g.getCategory());


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGrocery(pos);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(pos);
            }
        });

        groceryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changesPending = true;
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}