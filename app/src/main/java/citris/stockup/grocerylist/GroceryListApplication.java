package citris.stockup.grocerylist;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import citris.stockup.R;
import citris.stockup.adapters.GroceryListAdapter;
import citris.stockup.groceries.GroceriesSQLiteOpenHelper;
import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;

import static citris.stockup.groceries.GroceriesSQLiteOpenHelper.*;

/**
 * Created by Panic on 4/6/2015.
 */

public class GroceryListApplication extends Application {

    public ArrayList<GroceryList> currentGroceryLists = new ArrayList<GroceryList>();
    public ArrayList<Grocery> currentGroceries = new ArrayList<Grocery>();
    private SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        //Parse.initialize(this, "0BqEQPyE7ycnXnarj1YgsGkvgzAlj8tJtkogFQL3", "388Timb3JFccZseI0M0pj92egGqd5DBaHpLr9qVV");
        //updateData();
        //inflateLists();
    }

    public void logout() {
        ParseUser.logOut();
    }

    public void updateData(){
        ParseQuery<ParseObject> listQuery = ParseQuery.getQuery("List");
        listQuery.whereContains("key", ParseUser.getCurrentUser().getUsername());
        listQuery.orderByAscending("name");
        listQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> lists, ParseException error) {
                try {
                    if (lists != null) {
                        currentGroceryLists.clear();
                        for (int i = 0; i < lists.size(); i++) {
                            ParseObject temp = lists.get(i);

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Grocery");
                            query.whereEqualTo("tableid", temp.getString("name"));
                            query.orderByAscending(GROCERY_NAME);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> groceries, ParseException error) {
                                    try {
                                        if (groceries != null) {
                                            for (int i = 0; i < groceries.size(); i++) {
                                                ParseObject p = groceries.get(i);
                                                Grocery g = new Grocery(p.getString(GROCERY_NAME), p.getInt(GROCERY_QUANTITY_INT), p.getInt(GROCERY_QUANTITY_TYPE), p.getString(GROCERY_BRAND), p.getInt(GROCERY_PAST_TTL), p.getInt(GROCERY_PAST_TTL_TYPE), p.getString(GROCERY_CATEGORY));
                                                g.setId(p.getObjectId());
                                                g.setList(p.getString("tableid"));
                                                currentGroceries.add(g);
                                            }
                                        }
                                    } catch (UnsupportedOperationException e) {
                                        //Toast
                                    }
                                }
                            });
                            GroceryList gl = new GroceryList(temp.getString("name"));
                            gl.setCreator(temp.getString("created_by"));
                            gl.setKey(temp.getString("key"));
                            gl.setId(temp.getObjectId());
                            currentGroceryLists.add(gl);
                        }
                    }
                } catch (UnsupportedOperationException e) {
                    //Toast
                }
            }
        });
    }

    public void inflateLists() {
        for (int i = 0; i < currentGroceryLists.size(); i++) {
            for (int j = 0; j < currentGroceries.size(); j++) {
                if (currentGroceryLists.get(i).getName().equals(currentGroceries.get(j).getList())) {
                    currentGroceryLists.get(i).addGrocery(currentGroceries.get(j));
                }
            }
        }
    }

    public ArrayList<GroceryList> getCurrentGroceryLists() {
        return currentGroceryLists;
    }

    public ArrayList<Grocery> getCurrentGroceries() {
        return currentGroceries;
    }

    public void setCurrentGroceries(ArrayList<Grocery> currentGroceries) {
        this.currentGroceries = currentGroceries;
    }

    public void addGrocery(Grocery g){
        assert(null != g);

        ParseObject groceryItem = new ParseObject("Grocery");
        groceryItem.put(GROCERY_NAME, g.getName());
        groceryItem.put(GROCERY_QUANTITY_INT, g.getQuantityInt());
        groceryItem.put(GROCERY_QUANTITY_TYPE, g.getQuantityTypePos());
        groceryItem.put(GROCERY_BRAND, g.getBrand());
        groceryItem.put(GROCERY_PAST_TTL, g.getTtlInt());
        groceryItem.put(GROCERY_PAST_TTL_TYPE, g.getTtlTypePos());
        groceryItem.put(GROCERY_CATEGORY, g.getCategory());
        groceryItem.put(GROCERY_TTL, g.getTTL());
        groceryItem.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));
        groceryItem.put(GROCERY_LIST, g.getList());

        groceryItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException err) {
                if (err == null) {
                    //happy
                } else {
                    Log.d("cheese", "Add broke because: " + err.getMessage());
                }
            }
        });
        if (currentGroceries.size() > 0) {
            boolean found = false;
            for (int i = 0; i < currentGroceries.size(); i++) {
                if (currentGroceries.get(i).getName().compareToIgnoreCase(g.getName()) > 0) {
                    currentGroceries.add(i, g);
                    found = true;
                    break;
                }
            }
            if (found == false) {
                currentGroceries.add(g);
            }
        } else {
            currentGroceries.add(g);
        }
    }

    public void editGrocery(int pos, String groceryName, int quantityInt, int quantityType, String brandName, int ttlInt, int ttlType, String categoryName, String id, String list) {
        final Grocery g = currentGroceries.get(pos);

        g.setName(groceryName);
        g.setQuantityInt(quantityInt);
        g.setQuantityType(quantityType);
        g.setBrand(brandName);
        g.setTtlInt(ttlInt);
        g.setTtlType(ttlType);
        g.setCategory(categoryName);
        String temp = g.getId();

        ParseQuery<ParseObject> get = ParseQuery.getQuery("Grocery");
        get.getInBackground(temp, new GetCallback<ParseObject>() {
            public void done(ParseObject groceryItem, ParseException e) {
                if (e == null) {
                    groceryItem.put(GROCERY_NAME, g.getName());
                    groceryItem.put(GROCERY_QUANTITY_INT, g.getQuantityInt());
                    groceryItem.put(GROCERY_QUANTITY_TYPE, g.getQuantityTypePos());
                    groceryItem.put(GROCERY_BRAND, g.getBrand());
                    groceryItem.put(GROCERY_PAST_TTL, g.getTtlInt());
                    groceryItem.put(GROCERY_PAST_TTL_TYPE, g.getTtlTypePos());
                    groceryItem.put(GROCERY_CATEGORY, g.getCategory());
                    groceryItem.put(GROCERY_TTL, g.getTTL());
                    groceryItem.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));
                    groceryItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException err) {
                           if (err == null) {
                               //happy
                           } else {
                               Log.d("cheese", "Err broke because: " + err.getMessage());
                           }
                        }
                    });
                } else {
                    Log.d("cheese", "E broke because: " + e.getMessage());
                }
            }
        });
    }

    public void saveGrocery(int pos){
        final Grocery g = currentGroceries.get(pos);
        String temp = g.getId();
        ParseQuery<ParseObject> get = ParseQuery.getQuery("Grocery");
        get.getInBackground(temp, new GetCallback<ParseObject>() {
            public void done(ParseObject groceryItem, ParseException e) {
                if (e == null) {
                    groceryItem.put(GROCERY_NAME, g.getName());
                    groceryItem.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));
                    groceryItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException err) {
                            if (err == null) {
                                //happy
                            } else {
                                Log.d("cheese", "Err broke because: " + err.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("cheese", "E broke because: " + e.getMessage());
                }
            }
        });
    }

    public void removeGrocery (int position) {
        final Grocery g = currentGroceries.get(position);
        ParseQuery<ParseObject> get = ParseQuery.getQuery("Grocery");
        String temp = g.getId();

        get.getInBackground(temp, new GetCallback<ParseObject>() {
            public void done(ParseObject groceryItem, ParseException e) {
                groceryItem.deleteInBackground();
            }
        });
        currentGroceries.remove(position);
    }

    public void removeList (int position) {
        final GroceryList gl = currentGroceryLists.get(position);
        ParseQuery<ParseObject> get = ParseQuery.getQuery("List");
        String temp = gl.getId();

        get.getInBackground(temp, new GetCallback<ParseObject>() {
            public void done(ParseObject listItem, ParseException e) {
                listItem.deleteInBackground();
            }
        });
        setCurrentGroceries(currentGroceryLists.get(position).getContents());
        for(int i = 0; i < currentGroceries.size(); i++) {
            removeGrocery(i);
        }
        currentGroceryLists.remove(position);
    }

    public void checkout() {
        for(int i = 0; i < currentGroceries.size(); i++) {
            if(currentGroceries.get(i).isComplete()){
                final Grocery g = currentGroceries.get(i);
                String temp = g.getId();
                g.setTtlInt(g.getTtlInt());
                g.setTtlType(g.getQuantityTypePos());
                ParseQuery<ParseObject> get = ParseQuery.getQuery("Grocery");
                get.getInBackground(temp, new GetCallback<ParseObject>() {
                    public void done(ParseObject groceryItem, ParseException e) {
                        groceryItem.increment(GROCERY_TTL, g.getTTL());
                        groceryItem.put(GROCERY_PAST_TTL, g.getTtlInt());
                        groceryItem.put(GROCERY_PAST_TTL_TYPE, g.getTtlType());
                        groceryItem.saveInBackground();
                    }
                });
            }
        }
    }

    public void shareList(int pos, String sharedUser) {
        final GroceryList gl = currentGroceryLists.get(pos);
        final String key = gl.getKey() + ", " + sharedUser;
        gl.setKey(key);
        String temp = gl.getId();
        ParseQuery<ParseObject> get = ParseQuery.getQuery("List");
        get.getInBackground(temp, new GetCallback<ParseObject>() {
            public void done(ParseObject listItem, ParseException e) {
                if (e == null) {
                    listItem.put("key", key);
                    listItem.saveInBackground();
                } else {
                    Log.d("cheese", "E broke because: " + e.getMessage());
                }
            }
        });
    }

    public void addList(GroceryList gl) {
        assert (null != gl);

        ParseObject groceryItem = new ParseObject("List");
        groceryItem.put("name", gl.getName());
        groceryItem.put("created_by", gl.getCreator());
        groceryItem.put("key", gl.getKey());

        groceryItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException err) {
                if (err == null) {
                    //happy
                } else {
                    Log.d("cheese", "Add broke because: " + err.getMessage());
                }
            }
        });
        if (currentGroceryLists.size() > 0) {
            boolean found = false;
            for (int i = 0; i < currentGroceryLists.size(); i++) {
                if (currentGroceryLists.get(i).getName().compareToIgnoreCase(gl.getName()) > 0) {
                    currentGroceryLists.add(i, gl);
                    found = true;
                    break;
                }
            }
            if (found == false) {
                currentGroceryLists.add(gl);
            }
        } else {
            currentGroceryLists.add(gl);
        }
    }
}