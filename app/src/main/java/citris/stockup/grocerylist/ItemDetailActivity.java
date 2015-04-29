package citris.stockup.grocerylist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import citris.stockup.R;
import citris.stockup.groceries.Grocery;

public class ItemDetailActivity extends GroceryListActivity {

    private TextView groceryName;
    private TextView groceryQuantityInt;
    private TextView groceryQuantityType;
    private TextView groceryBrand;
    private TextView groceryTtlInt;
    private TextView groceryTtlType;
    private TextView groceryCategory;
    private Button editButton;
    private Button backButton;
    private TextView groceryId;
    private Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setViews();
    }

    private void setViews() {
        groceryName = (TextView) findViewById(R.id.insert_name);
        groceryQuantityInt = (TextView) findViewById(R.id.insert_quantity_int);
        groceryQuantityType = (TextView) findViewById(R.id.insert_quantity_type);
        groceryBrand = (TextView) findViewById(R.id.insert_brand);
        groceryTtlInt = (TextView) findViewById(R.id.insert_ttl_int);
        groceryTtlType = (TextView) findViewById(R.id.insert_ttl_type);
        groceryCategory = (TextView) findViewById(R.id.insert_category);
        editButton = (Button) findViewById(R.id.edit_button);
        backButton = (Button) findViewById(R.id.back_button);
        removeButton = (Button) findViewById(R.id.remove_button);
        groceryId = (TextView) findViewById(R.id.insert_id);

        final Grocery g = getIntent().getParcelableExtra("tmpGrocery");
        final int pos = getIntent().getIntExtra("position", 0);

        groceryName.setText(g.getName());
        groceryQuantityInt.setText("" + g.getQuantityInt());
        groceryQuantityType.setText(g.getQuantityType());
        groceryBrand.setText(g.getBrand());
        groceryTtlInt.setText("" + g.getTtlInt());
        groceryTtlType.setText(g.getTtlType());
        groceryCategory.setText(g.getCategory());
        groceryId.setText(g.getId());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, EditGroceryActivity.class);
                intent.putExtra("tmpGrocery", g);
                intent.putExtra("position", pos);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGroceryListApplication().removeGrocery(pos);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
