package citris.stockup.groceries;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Panic on 4/6/2015.
 */

public class GroceriesSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "grocery_list_db";
    public static final int VERSION = 1;
    public static final String GROCERY_TABLE = "groceries";
    public static final String GROCERY_ID = "id";
    public static final String GROCERY_NAME = "name";
    public static final String GROCERY_COMPLETE = "complete";
    public static final String GROCERY_QUANTITY_INT = "quantity";
    public static final String GROCERY_QUANTITY_TYPE = "quantityType";
    public static final String GROCERY_BRAND = "brand";
    public static final String GROCERY_CATEGORY = "category";
    public static final String GROCERY_TTL = "TTL";

    //Save the user's preference for ttl
    public static final String GROCERY_PAST_TTL = "pastTTL";
    public static final String GROCERY_PAST_TTL_TYPE = "pastTTLType";


    public GroceriesSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       //lel
    }

    protected void createTables(SQLiteDatabase db) {
        String CREATE_GROCERY_TABLE = "CREATE TABLE " + GROCERY_TABLE + "(" +
            GROCERY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                GROCERY_NAME + " TEXT, " + GROCERY_COMPLETE + " TEXT);";
        db.execSQL(CREATE_GROCERY_TABLE);
    }
}
