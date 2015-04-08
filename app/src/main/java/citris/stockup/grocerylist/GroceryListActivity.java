package citris.stockup.grocerylist;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Panic on 4/6/2015.
 */

public class GroceryListActivity extends Activity {

    protected GroceryListApplication getGroceryListApplication() {
        GroceryListApplication gla = (GroceryListApplication)getApplication();
        return gla;
    }
}
