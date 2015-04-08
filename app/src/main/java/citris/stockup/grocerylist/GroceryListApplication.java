package citris.stockup.grocerylist;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import citris.stockup.groceries.GroceriesSQLiteOpenHelper;
import citris.stockup.groceries.Grocery;

import static citris.stockup.groceries.GroceriesSQLiteOpenHelper.*;

/**
 * Created by Panic on 4/6/2015.
 */

public class GroceryListApplication extends Application {

    private ArrayList<Grocery> currentGroceries;
    private SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        GroceriesSQLiteOpenHelper helper = new GroceriesSQLiteOpenHelper(this);
        database = helper.getWritableDatabase();
        if (null == currentGroceries) {
            loadGroceries();
        }
    }

    private void loadGroceries() {
        currentGroceries = new ArrayList<Grocery>();
        Cursor groceryCursor = database.query(
                GROCERY_TABLE, new String[] {GROCERY_ID, GROCERY_NAME, GROCERY_COMPLETE},
                null, null, null, null,
                String.format("%s, %s", GROCERY_COMPLETE, GROCERY_NAME));
        groceryCursor.moveToFirst();
        Grocery g;
        if (! groceryCursor.isAfterLast()) {
            do {
                long id = groceryCursor.getLong(0);
                String name = groceryCursor.getString(1);
                String boolValue = groceryCursor.getString(2);
                boolean complete = Boolean.parseBoolean(boolValue);
                g = new Grocery(name);
                g.setId(id);
                g.setComplete(complete);
                currentGroceries.add(g);
            } while (groceryCursor.moveToNext());
        }
        groceryCursor.close();
    }

    public ArrayList<Grocery> getCurrentGroceries() {
        return currentGroceries;
    }

    public void setCurrentGroceries(ArrayList<Grocery> currentGroceries) {
        this.currentGroceries = currentGroceries;
    }

    public void addGrocery(Grocery g){
        assert(null != g);

        ContentValues values = new ContentValues();
        values.put(GROCERY_NAME, g.getName());
        values.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));
        long id = database.insert(GROCERY_TABLE, null, values);
        g.setId(id);
        currentGroceries.add(g);
    }

    public void saveGrocery(Grocery g){
        assert(null != g);

        ContentValues values = new ContentValues();
        values.put(GROCERY_NAME, g.getName());
        values.put(GROCERY_COMPLETE, Boolean.toString(g.isComplete()));
        long id = g.getId();
        String where = String.format("%s = ?", GROCERY_ID);
        database.update(GROCERY_TABLE, values, where, new String[]{id+""});
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