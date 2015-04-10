package citris.stockup.grocerylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import citris.stockup.R;
import citris.stockup.groceries.Grocery;

/**
 * Created by Panic on 4/6/2015.
 */

public class AddGroceryActivity extends GroceryListActivity {

    private static final int REQUEST_CHOOSE_ADDRESS = 0;
    private EditText groceryNameEditText;
    private EditText brandEditText;
    private EditText categoryEditText;
    private Button addButton;
    private Button cancelButton;
    private TextView groceryText;
    protected boolean changesPending;
    private AlertDialog unsavedChangesDialog;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_grocery);
        setViews();
        getActionBar().hide();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Vollkorn-Regular.ttf");
        addButton.setTypeface(tf);
    }

    private void addGrocery() {
        String groceryName = groceryNameEditText.getText().toString();
        if (!groceryName.isEmpty()) {
            Grocery g = new Grocery(groceryName);
            getGroceryListApplication().addGrocery(g);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: Invalid grocery name.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancel() {
        if (changesPending){
            unsavedChangesDialog = new AlertDialog.Builder(this)
            .setTitle(R.string.unsaved_changes_title)
            .setMessage(R.string.unsaved_changes_message)
//          Add
            .setNegativeButton(R.string.button_add, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    addGrocery();
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
        groceryNameEditText = (EditText)findViewById(R.id.grocery_edit_name);
        brandEditText = (EditText)findViewById(R.id.grocery_edit_brand);
        categoryEditText = (EditText)findViewById(R.id.edit_category);
        addButton = (Button)findViewById(R.id.add_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGrocery();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
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