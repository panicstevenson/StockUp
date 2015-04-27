package citris.stockup.grocerylist;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import citris.stockup.R;
import citris.stockup.adapters.GroceryListAdapter;
import citris.stockup.adapters.ListListAdapter;
import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;


public class ViewListsActivity extends ListActivity {

    private Button addButton;
    private GroceryListApplication app;
    private ListListAdapter listAdapter;
    private Button checkoutButton;
    private SearchView searchView;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setViews();

        getActionBar().hide();

        app = (GroceryListApplication)getApplication();
        listAdapter = new ListListAdapter(app.getCurrentGroceryLists(), this);
        setListAdapter(listAdapter);
        app.inflateLists();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        GroceryList gl = listAdapter.getItem(position);
        app.setCurrentGroceries(gl.getContents());
        Intent intent = new Intent(ViewListsActivity.this, ViewGroceriesActivity.class);
        //adapter.toggleTaskCompleteAtPosition(position);


        //intent.putExtra("tmpGrocery", g);
        //intent.putExtra("position", position);
        startActivity(intent);
        //app.viewGrocery(g);
        //app.saveGrocery(g);

    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter.forceReload();
    }

    private void setViews() {
        /*addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add grocerylist add button
                //Intent intent = new Intent(ViewListsActivity.this, AddGroceryActivity.class);
                //startActivity(intent);
            }
        }); */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
