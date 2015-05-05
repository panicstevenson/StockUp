package citris.stockup.grocerylist;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Timer;
import java.util.TimerTask;

import citris.stockup.R;
import citris.stockup.adapters.GroceryListAdapter;
import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;


public class ViewGroceriesActivity extends ListActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private GroceryListApplication app;
    private GroceryListAdapter adapter;
    private Button addButton;
    private Button checkoutButton;
    private SearchView searchView;
    private Timer mTimer;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        setViews();

        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Search view initialization
        searchView = (SearchView)findViewById(R.id.grocery_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnCloseListener(this);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search grocery list...");

        //list adapter initialization
        app = (GroceryListApplication)getApplication();
        adapter = new GroceryListAdapter(app.getCurrentGroceries(), this);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        //TODO reorder the list of current groceries
        super.onResume();
        adapter.forceReload();
    }

    private void setViews() {
        //Set actionbar title to the name of the current list
        final String listName = getIntent().getStringExtra("listName");
        getActionBar().setTitle(listName);

        final SwipeDetector swipeDetector = new SwipeDetector();
        getListView().setOnTouchListener(swipeDetector);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                test = (TextView) view.findViewById(R.id.delete_button);
                final View finalView = view;

                test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.FadeOutLeft)
                                .duration(650)
                                .playOn(finalView);
                        mTimer = new Timer();
                        mTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                boolean tmp = false;
                                if (position > 0) {
                                    tmp = app.getCurrentGroceries().get(position - 1).getDelete();
                                }
                                final boolean finalTmp = tmp;
                                app.removeGrocery(position);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                        app.getCurrentGroceries().get(position - 1).setDelete(finalTmp);
                                        if (finalTmp) {
                                            finalView.findViewById(R.id.delete_button)
                                                    .setVisibility(View.VISIBLE);
                                        } else {
                                            finalView.findViewById(R.id.delete_button)
                                                    .setVisibility(View.INVISIBLE);
                                        }
                                        finalView.findViewById(R.id.delete_button).setClickable(finalTmp);
                                        finalView.setTranslationX(0);
                                    }
                                });
                                mTimer = null;
                            }
                        }, 700);
                        YoYo.with(Techniques.FadeIn)
                                .duration(1)
                                .delay(651)
                                .playOn(finalView);
                    }
                });

                test.setClickable(false);


                if (swipeDetector.swipeDetected()) {
                    switch (swipeDetector.getAction()) {
                        case RL:
                            if (!app.getCurrentGroceries().get(position).isComplete()) {
                                if (!app.getCurrentGroceries().get(position).getDelete()) {
                                    YoYo.with(Techniques.BounceInRight)
                                            .duration(700)
                                            .playOn(view.findViewById(R.id.delete_button));
                                    view.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
                                    view.findViewById(R.id.delete_button).setClickable(true);
                                    app.getCurrentGroceries().get(position).setDelete(true);
                                    break;
                                }
                            } else {
                                YoYo.with(Techniques.SlideOutRight)
                                        .duration(350)
                                        .playOn(view.findViewById(R.id.check));
                                app.getCurrentGroceries().get(position).toggleComplete();
                                app.saveGrocery(position);
                                break;
                            }
                        case LR:
                            if (app.getCurrentGroceries().get(position).getDelete()) {
                                YoYo.with(Techniques.SlideOutRight)
                                        .duration(350)
                                        .playOn(view.findViewById(R.id.delete_button));
                                view.findViewById(R.id.delete_button).setClickable(false);
                                app.getCurrentGroceries().get(position).setDelete(false);
                                break;
                            } else {
                                if (!app.getCurrentGroceries().get(position).isComplete()) {
                                    YoYo.with(Techniques.BounceInRight)
                                            .duration(700)
                                            .playOn(view.findViewById(R.id.check));
                                    view.findViewById(R.id.check).setVisibility(View.VISIBLE);
                                    app.getCurrentGroceries().get(position).toggleComplete();
                                    app.saveGrocery(position);
                                    break;
                                } else {
                                    YoYo.with(Techniques.SlideOutRight)
                                            .duration(350)
                                            .playOn(view.findViewById(R.id.check));
                                    app.getCurrentGroceries().get(position).toggleComplete();
                                    app.saveGrocery(position);
                                    break;
                                }
                            }
                    }
                } else {
                    if (app.getCurrentGroceries().get(position).getDelete()) {
                        YoYo.with(Techniques.SlideOutRight)
                                .duration(350)
                                .playOn(view.findViewById(R.id.delete_button));
                        view.findViewById(R.id.delete_button).setClickable(false);
                        app.getCurrentGroceries().get(position).setDelete(false);
                        app.saveGrocery(position);
                    } else {
                        if (!app.getCurrentGroceries().get(position).isComplete()) {
                            YoYo.with(Techniques.BounceInRight)
                                    .duration(700)
                                    .playOn(view.findViewById(R.id.check));
                            view.findViewById(R.id.check).setVisibility(View.VISIBLE);
                            app.getCurrentGroceries().get(position).toggleComplete();
                            app.saveGrocery(position);
                        } else {
                            YoYo.with(Techniques.SlideOutRight)
                                    .duration(350)
                                    .playOn(view.findViewById(R.id.check));
                            app.getCurrentGroceries().get(position).toggleComplete();
                            app.saveGrocery(position);
                        }
                    }
                }
            }
        });

        //Long Click sends you to item detail activity
        //Creates parcelable grocery and sends it to the next activity
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ViewGroceriesActivity.this, ItemDetailActivity.class);
                Grocery g = adapter.getItem(position);
                intent.putExtra("tmpGrocery", g);
                intent.putExtra("position", position);
                startActivity(intent);
                return true;
            }
        });

        //Add button creates a new grocery under the current list
        addButton = (Button)findViewById(R.id.add_grocery);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewGroceriesActivity.this, AddGroceryActivity.class);
                intent.putExtra("listName", listName);
                startActivity(intent);
            }
        });

        //Checkout button resets current TTL to set TTL then finishes
        checkoutButton = (Button)findViewById(R.id.checkout);
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
        //Clicking home takes you back to your lists
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Search functions
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