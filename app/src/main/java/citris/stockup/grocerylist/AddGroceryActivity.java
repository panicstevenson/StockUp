package citris.stockup.grocerylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import citris.stockup.R;
import citris.stockup.groceries.Grocery;
import citris.stockup.scanner.CameraActivity;

/**
 * Created by Panic on 4/6/2015.
 */

public class AddGroceryActivity extends GroceryListActivity {

    private EditText groceryNameEditText;
    private EditText brandEditText;
    private EditText categoryEditText;
    private Button addButton;
    private Button cancelButton;
    private ImageButton barcodeButton;
    private TextView groceryText;
    protected boolean changesPending;
    private AlertDialog unsavedChangesDialog;
    private Spinner quantityIntSpinner;
    private Spinner ttlIntSpinner;
    private Spinner quantityTypeSpinner;
    private Spinner ttlTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_grocery);
        setViews();

        getActionBar().setTitle("Add Grocery");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addGrocery() {
        final String listName = getIntent().getStringExtra("listName");

        String groceryName = groceryNameEditText.getText().toString();
        int ttlType = ttlTypeSpinner.getSelectedItemPosition();
        int ttlInt = ttlIntSpinner.getSelectedItemPosition() + 1;
        int quantityType = quantityTypeSpinner.getSelectedItemPosition();
        int quantityInt = quantityIntSpinner.getSelectedItemPosition() + 1;
        String brandName = brandEditText.getText().toString();
        String categoryName = categoryEditText.getText().toString();

        if (!groceryName.isEmpty()) {
            Grocery g = new Grocery(groceryName, quantityInt, quantityType, brandName, ttlInt, ttlType, categoryName);
            g.setList(listName);
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
        quantityIntSpinner = (Spinner)findViewById(R.id.quantity_int);
        quantityTypeSpinner = (Spinner)findViewById(R.id.quantity_type);
        brandEditText = (EditText)findViewById(R.id.grocery_edit_brand);
        ttlIntSpinner = (Spinner)findViewById(R.id.ttl_int);
        ttlTypeSpinner = (Spinner)findViewById(R.id.ttl_type);
        categoryEditText = (EditText)findViewById(R.id.edit_category);
        addButton = (Button)findViewById(R.id.add_button);
        barcodeButton = (ImageButton)findViewById(R.id.barcode);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGrocery();
            }
        });


        barcodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in = new Intent(AddGroceryActivity.this, CameraActivity.class);
                startActivity(in);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}