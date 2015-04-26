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
        GroceriesSQLiteOpenHelper helper = new GroceriesSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "0BqEQPyE7ycnXnarj1YgsGkvgzAlj8tJtkogFQL3", "388Timb3JFccZseI0M0pj92egGqd5DBaHpLr9qVV");
        ParseUser.enableAutomaticUser();

        updateData();
    }

    public void updateData(){
        ParseQuery<ParseObject> listQuery = ParseQuery.getQuery("List");
        listQuery.whereEqualTo("key", "pstevenson");    //TODO MAKE DYNAMIC
        listQuery.orderByAscending("name");
        listQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> lists, ParseException error) {
                try {
                    if(lists != null) {
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
                                        if(groceries != null) {
                                            currentGroceries.clear();
                                            for (int i = 0; i < groceries.size(); i++) {
                                                ParseObject p = groceries.get(i);
                                                Grocery g = new Grocery(p.getString(GROCERY_NAME), p.getInt(GROCERY_QUANTITY_INT), p.getInt(GROCERY_QUANTITY_TYPE), p.getString(GROCERY_BRAND), p.getInt(GROCERY_PAST_TTL), p.getInt(GROCERY_PAST_TTL_TYPE), p.getString(GROCERY_CATEGORY), p.getObjectId());
                                                currentGroceries.add(g);
                                            }
                                        }
                                    } catch (UnsupportedOperationException e) {
                                        //Toast
                                    }
                                }
                            });
                            GroceryList gl = new GroceryList(temp.getString("name"), currentGroceries);
                            currentGroceryLists.add(gl);
                        }
                    }
                } catch (UnsupportedOperationException e) {
                    //Toast
                }
            }
        });
        currentGroceries.clear();
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
        groceryItem.put(GROCERY_QUANTITY_TYPE, g.getQuantityType());
        groceryItem.put(GROCERY_BRAND, g.getBrand());
        groceryItem.put(GROCERY_PAST_TTL, g.getTtlInt());
        groceryItem.put(GROCERY_PAST_TTL_TYPE, g.getTtlType());
        groceryItem.put(GROCERY_CATEGORY, g.getCategory());
        groceryItem.put(GROCERY_TTL, g.getTTL());
        groceryItem.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));

        groceryItem.pinInBackground();
        groceryItem.saveEventually();

        currentGroceries.add(g);
    }

    public void editGrocery(int pos, String groceryName, int quantityInt, int quantityType, String brandName, int ttlInt, int ttlType, String categoryName) {
        final Grocery g = currentGroceries.get(pos);

        g.setName(groceryName);
        g.setQuantityInt(quantityInt);
        g.setQuantityType(quantityType);
        g.setBrand(brandName);
        g.setTtlInt(ttlInt);
        g.setTtlType(ttlType);
        g.setCategory(categoryName);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Grocery");
        query.getInBackground(g.getId(), new GetCallback<ParseObject>() {
            public void done(ParseObject groceryItem, ParseException e) {
                if (e == null) {
                    groceryItem.put(GROCERY_NAME, g.getName());
                    groceryItem.put(GROCERY_QUANTITY_INT, g.getQuantityInt());
                    groceryItem.put(GROCERY_QUANTITY_TYPE, g.getQuantityType());
                    groceryItem.put(GROCERY_BRAND, g.getBrand());
                    groceryItem.put(GROCERY_PAST_TTL, g.getTtlInt());
                    groceryItem.put(GROCERY_PAST_TTL_TYPE, g.getTtlType());
                    groceryItem.put(GROCERY_CATEGORY, g.getCategory());
                    groceryItem.put(GROCERY_TTL, g.getTTL());
                    groceryItem.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));
                    groceryItem.saveInBackground();
                } else {
                    //fill
                }
            }
        });
    }


    public void saveGrocery(Grocery g){
        assert(null != g);

        ContentValues values = new ContentValues();
        values.put(GROCERY_NAME, g.getName());
        values.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));
        //long id = g.getId();
        String where = String.format("%s = ?", GROCERY_ID);
        //database.update(GROCERY_TABLE, values, where, new String[]{id+""});
    }

    public void deleteGroceries(Long[] ids) {
        StringBuffer idList = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            idList.append(ids[i]);
            if (i < ids.length - 1) {
                idList.append(",");
            }
        }
        String where = String.format("%s in (%s)", GROCERY_ID, idList);
        database.delete(GROCERY_TABLE, where, null);
    }
}