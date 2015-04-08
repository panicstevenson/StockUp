package citris.stockup.grocerylist;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import citris.stockup.R;
import citris.stockup.adapters.GroceryListAdapter;
import citris.stockup.groceries.Grocery;


public class ViewGroceriesActivity extends ListActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private Button addButton;
    private GroceryListApplication app;
    private GroceryListAdapter adapter;
    private Button checkoutButton;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setViews();

        searchView = (SearchView)findViewById(R.id.grocery_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);
        app = (GroceryListApplication)getApplication();
        adapter = new GroceryListAdapter(app.getCurrentGroceries(), this);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        adapter.toggleTaskCompleteAtPosition(position);
        Grocery g = adapter.getItem(position);
        app.saveGrocery(g);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.forceReload();
    }

    /* TODO create separate instance for checkout and delete.
    * Currently checkout just removes from database, but it should update TTL
    * Delete grocery should remove from database*/
    protected void checkoutGroceries() {
        Long[] ids = adapter.checkoutGroceries();
        app.deleteGroceries(ids);
    }

    private void setViews() {
        addButton = (Button)findViewById(R.id.add_grocery);
        checkoutButton = (Button)findViewById(R.id.checkout);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.storeGroceries();
                Intent intent = new Intent(ViewGroceriesActivity.this, AddGroceryActivity.class);
                startActivity(intent);
            }
        });
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkoutGroceries();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
        return true;
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

    @Override
    public boolean onClose() {
        adapter.searchGroceries("");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.searchGroceries(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.searchGroceries(newText);
        return false;
    }
}
