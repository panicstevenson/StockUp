package citris.stockup.grocerylist;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import citris.stockup.R;
import citris.stockup.adapters.GroceryListAdapter;
import citris.stockup.adapters.ListListAdapter;
import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;


public class ViewListsActivity extends ListActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private Button addButton;
    private GroceryListApplication app;
    private ListListAdapter listAdapter;
    private Button checkoutButton;
    private SearchView searchView;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_list_view);
        setViews();

        //Search view initialization
        searchView = (SearchView)findViewById(R.id.grocery_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search for a list...");

        app = (GroceryListApplication)getApplication();
        listAdapter = new ListListAdapter(app.getCurrentGroceryLists(), this);
        setListAdapter(listAdapter);
        app.inflateLists();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //Open selected list
        GroceryList gl = listAdapter.getItem(position);
        app.setCurrentGroceries(gl.getContents());
        Intent intent = new Intent(ViewListsActivity.this, ViewGroceriesActivity.class);
        intent.putExtra("listName", listAdapter.getItem(position).getName());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter.forceReload();
    }

    private void setViews() {
        getActionBar().setTitle(ParseUser.getCurrentUser().getUsername() + "'s Lists");

        //On long click edit/remove grocery list
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ViewListsActivity.this, EditListActivity.class);
                GroceryList gl = listAdapter.getItem(position);
                intent.putExtra("tmpList", gl);
                intent.putExtra("position", position);
                startActivity(intent);
                return true;
            }
        });

        //add list
        addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewListsActivity.this, AddListActivity.class);
                startActivity(intent);
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
        //Logout
        switch (item.getItemId()) {
            case R.id.logout:
                app.logout();
                app.getCurrentGroceries().clear();
                app.getCurrentGroceryLists().clear();
                //listAdapter.notifyDataSetChanged();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Search functions
    @Override
    public boolean onClose() {
        listAdapter.searchLists("");
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.searchLists(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.searchLists(newText);
        return false;
    }
}
