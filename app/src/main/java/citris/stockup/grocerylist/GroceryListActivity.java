package citris.stockup.grocerylist;

import android.app.Activity;

/**
 * Created by Panic on 4/6/2015.
 */

public class GroceryListActivity extends Activity {

    protected GroceryListApplication getGroceryListApplication() {
        GroceryListApplication gla = (GroceryListApplication)getApplication();
        return gla;
    }
}
