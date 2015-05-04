package citris.stockup.grocerylist;

import android.animation.ObjectAnimator;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

import citris.stockup.R;
import citris.stockup.adapters.GroceryListAdapter;
import citris.stockup.adapters.ListListAdapter;
import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;

import static citris.stockup.grocerylist.SwipeDetector.*;
import static citris.stockup.grocerylist.SwipeDetector.Action.*;


public class ViewListsActivity extends ListActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private Button addButton;
    private GroceryListApplication app;
    private ListListAdapter listAdapter;
    private Button checkoutButton;
    private SearchView searchView;
    private TextView test;
    private Timer mTimer;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdapter.forceReload();
    }

    private void setViews() {
        getActionBar().setTitle(ParseUser.getCurrentUser().getUsername() + "'s Lists");

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
                                    tmp = app.getCurrentGroceryLists().get(position - 1).getDelete();
                                }
                                final boolean finalTmp = tmp;
                                app.removeList(position);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listAdapter.notifyDataSetChanged();
                                        app.getCurrentGroceryLists().get(position - 1).setDelete(finalTmp);
                                        if(finalTmp) {
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
                    switch(swipeDetector.getAction()) {
                        case RL:
                            //if(!app.getCurrentGroceries().get(position).isComplete()) {
                            if(!app.getCurrentGroceryLists().get(position).getDelete()) {
                                YoYo.with(Techniques.BounceInRight)
                                        .duration(700)
                                        .playOn(view.findViewById(R.id.delete_button));
                                view.findViewById(R.id.delete_button).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.delete_button).setClickable(true);
                                app.getCurrentGroceryLists().get(position).setDelete(true);
                                break;
                            } else {
                                break;
                            }
                        case LR:
                            if(app.getCurrentGroceryLists().get(position).getDelete()) {
                                YoYo.with(Techniques.SlideOutRight)
                                        .duration(350)
                                        .playOn(view.findViewById(R.id.delete_button));
                                view.findViewById(R.id.delete_button).setClickable(false);
                                app.getCurrentGroceryLists().get(position).setDelete(false);
                                break;
                            } else {
                                break;
                            }
                            /*if(!app.getCurrentGroceries().get(position).getDelete()) {
                                if (!app.getCurrentGroceries().get(position).isComplete()) {
                                    YoYo.with(Techniques.BounceInRight)
                                        .duration(700)
                                        .playOn(view.findViewById(R.id.check));
                                    view.findViewById(R.id.check).setVisibility(View.VISIBLE);
                                    break;
                                }
                            } */
                    }
                } else {
                    //Open selected list
                    GroceryList gl = listAdapter.getItem(position);
                    app.setCurrentGroceries(gl.getContents());
                    Intent intent = new Intent(ViewListsActivity.this, ViewGroceriesActivity.class);
                    intent.putExtra("listName", listAdapter.getItem(position).getName());
                    startActivity(intent);
                }
            }
        });

        //On long click edit/remove grocery list
        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                if (swipeDetector.swipeDetected()) {
                    if(swipeDetector.getAction() == LR) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationX", 0, 200);
                        anim.setDuration(2);
                        anim.start();
                    }
                    return true;
                } else {
                    Intent intent = new Intent(ViewListsActivity.this, EditListActivity.class);
                    GroceryList gl = listAdapter.getItem(position);
                    intent.putExtra("tmpList", gl);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    return true;
                }
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bounceAnimate (View view) {
        ObjectAnimator step1 = ObjectAnimator.ofFloat(view, "translationX", 0, 500);
        step1.setDuration(200);
        step1.start();

        ObjectAnimator step2 = ObjectAnimator.ofFloat(view, "translationX", 0, 0);
        step2.setStartDelay(200);
        step2.setDuration(400);
        step2.start();

        ObjectAnimator step3 = ObjectAnimator.ofFloat(view, "translationX", 0, 230);
        step3.setStartDelay(600);
        step3.setDuration(150);
        step3.start();

        ObjectAnimator step4 = ObjectAnimator.ofFloat(view, "translationX", 0, 0);
        step4.setStartDelay(750);
        step4.setDuration(200);
        step4.start();
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
