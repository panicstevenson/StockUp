package citris.stockup.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import citris.stockup.R;
import citris.stockup.groceries.Grocery;
import citris.stockup.groceries.GroceryList;
import citris.stockup.views.GroceryListItem;
import citris.stockup.views.GroceryLists;

/**
 * Created by Panic on 4/26/2015.
 */

public class ListListAdapter extends BaseAdapter{

    private ArrayList<GroceryList> list;
    private Context context;

    public ListListAdapter(ArrayList<GroceryList> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GroceryList getItem(int position) {
        if (null == list) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroceryLists gl;
        if (null == convertView) {
            gl = (GroceryLists)View.inflate(context, R.layout.lists_item, null);
        } else {
            gl = (GroceryLists)convertView;
        }
        gl.setList(list.get(position));
        return gl;
    }

    public void openList(int position) {
        //
    }

    public void forceReload() {
        /*storedGroceries.clear();
        this.storedGroceries.addAll(groceries);
        notifyDataSetChanged();*/
    }
}