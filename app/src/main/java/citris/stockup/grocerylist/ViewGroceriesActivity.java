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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import static citris.stockup.groceries.GroceriesSQLiteOpenHelper.*;


import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import citris.stockup.R;
import citris.stockup.adapters.GroceryListAdapter;
import citris.stockup.adapters.ListListAdapter;
import citris.stockup.groceries.Grocery;


public class ViewGroceriesActivity extends ListActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private Button addButton;
    private GroceryListApplication app;
    private GroceryListAdapter adapter;
    private Button checkoutButton;
    private SearchView searchView;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setViews();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = (SearchView)findViewById(R.id.grocery_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search grocery list...");

        app = (GroceryListApplication)getApplication();
        adapter = new GroceryListAdapter(app.getCurrentGroceries(), this);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(ViewGroceriesActivity.this, ItemDetailActivity.class);
        //adapter.toggleTaskCompleteAtPosition(position);
        Grocery g = adapter.getItem(position);

        intent.putExtra("tmpGrocery", g);
        intent.putExtra("position", position);
        startActivity(intent);
        //app.viewGrocery(g);
        //app.saveGrocery(g);
    }



    @Override
    protected void onResume() {
        super.onResume();
        adapter.forceReload();
    }

    private void setViews() {
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                adapter.toggleTaskCompleteAtPosition(position);
                return true;
            }
        });




        final String listName = getIntent().getStringExtra("listName");
        addButton = (Button)findViewById(R.id.add_grocery);
        checkoutButton = (Button)findViewById(R.id.checkout);
        //textview = (TextView)findViewById(R.id.grocery_list);
        //textview.setText(listName);

        getActionBar().setTitle(listName);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewGroceriesActivity.this, AddGroceryActivity.class);
                intent.putExtra("listName", listName);
                startActivity(intent);
            }
        });
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                app.checkout();
                finish();
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
