package citris.stockup.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import citris.stockup.R;
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
    private TextView textView;
    private TextView delete;

    public GroceryLists(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView = (TextView)findViewById(android.R.id.text1);
        subtext = (TextView)findViewById(android.R.id.text2);
        delete = (TextView)findViewById(R.id.delete_button);
    }

    public Grocery getGrocery() {
        return grocery;
    }

    public void setList(GroceryList list) {
        this.list = list;
        textView.setText(list.getName());
        subtext.setText("Created by " + list.getCreator());
        delete.setVisibility(View.INVISIBLE);
        if(list.getDelete()) {
            delete.setVisibility(View.VISIBLE);
        }
    }
}