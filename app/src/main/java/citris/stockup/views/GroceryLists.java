package citris.stockup.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;

/**
 * Created by Panic on 4/26/2015.
 */

public class GroceryLists extends LinearLayout {

    private GroceryList list;
    private Grocery grocery;
    private CheckedTextView checkbox;
    private TextView subtext;

    public GroceryLists(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkbox = (CheckedTextView)findViewById(android.R.id.text1);
        //subtext = (TextView)findViewById(android.R.id.text2);
    }

    public Grocery getGrocery() {
        return grocery;
    }

    public void setList(GroceryList list) {
        this.list = list;
        checkbox.setText(list.getName());
    }
}