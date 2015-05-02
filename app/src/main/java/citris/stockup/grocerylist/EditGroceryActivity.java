package citris.stockup.grocerylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private Spinner quantityIntSpinner;
    private Spinner ttlIntSpinner;
    private Spinner quantityTypeSpinner;
    private Spinner ttlTypeSpinner;
    private String idHolder;
    private String listHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_grocery);
        setViews();
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
            getGroceryListApplication().editGrocery(pos, groceryName, quantityInt, quantityType, brandName, ttlInt, ttlType, categoryName, idHolder, listHolder);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: Invalid grocery name.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setViews() {
        final Grocery g = getIntent().getParcelableExtra("tmpGrocery");
        idHolder = g.getId();
        listHolder = g.getList();
        final int pos = getIntent().getIntExtra("position", 0);

        groceryNameEditText = (EditText)findViewById(R.id.grocery_edit_name);
        quantityIntSpinner = (Spinner)findViewById(R.id.quantity_int);
        quantityTypeSpinner = (Spinner)findViewById(R.id.quantity_type);
        brandEditText = (EditText)findViewById(R.id.grocery_edit_brand);
        ttlIntSpinner = (Spinner)findViewById(R.id.ttl_int);
        ttlTypeSpinner = (Spinner)findViewById(R.id.ttl_type);
        categoryEditText = (EditText)findViewById(R.id.edit_category);
        editButton = (Button)findViewById(R.id.edit_button);

        groceryNameEditText.setText(g.getName());
        quantityIntSpinner.setSelection(g.getQuantityInt() - 1);
        quantityTypeSpinner.setSelection(g.getQuantityTypePos());
        brandEditText.setText(g.getBrand());
        ttlIntSpinner.setSelection(g.getTtlInt() - 1);
        ttlTypeSpinner.setSelection(g.getTtlTypePos());
        categoryEditText.setText(g.getCategory());

        getActionBar().setTitle("Edit " + g.getName());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGrocery(pos);
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
        //Clicking home takes you back to your lists
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}