package citris.stockup.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import citris.stockup.groceries.Grocery;

/**
 * Created by Panic on 4/6/2015.
 */

public class GroceryListItem extends LinearLayout {

    private Grocery grocery;
    private CheckedTextView checkbox;
    private TextView subtext;

    public GroceryListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkbox = (CheckedTextView)findViewById(android.R.id.text1);
        subtext = (TextView)findViewById(android.R.id.text2);
    }

    public Grocery getGrocery() {
        return grocery;
    }

    public void setGrocery(Grocery grocery) {
        this.grocery = grocery;
        checkbox.setText(grocery.getName());
        checkbox.setChecked(grocery.isComplete());
        subtext.setText("ItemID: " + grocery.getId());
    }
}