package citris.stockup.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

import citris.stockup.groceries.Grocery;
import citris.stockup.R;
import citris.stockup.views.GroceryListItem;

/**
 * Created by Panic on 4/6/2015.
 */

public class GroceryListAdapter extends BaseAdapter{

    private ArrayList<Grocery> groceries;
    private ArrayList<Grocery> storedGroceries = new ArrayList<Grocery>();
    private Context context;

    public GroceryListAdapter(ArrayList<Grocery> groceries, Context context) {
        super();
        this.groceries = groceries;
        this.context = context;
        this.storedGroceries.addAll(groceries);
    }

    @Override
    public int getCount() {
        return groceries.size();
    }

    @Override
    public Grocery getItem(int position) {
        if (null == groceries) {
            return null;
        }
        return groceries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroceryListItem gli;
        if (null == convertView) {
            gli = (GroceryListItem)View.inflate(context, R.layout.grocery_list_item, null);
        } else {
            gli = (GroceryListItem)convertView;
        }
        gli.setGrocery(groceries.get(position));
        return gli;
    }

    public void forceReload() {
        storedGroceries.clear();
        this.storedGroceries.addAll(groceries);
        notifyDataSetChanged();
    }

    public void toggleTaskCompleteAtPosition(int position) {
        Grocery g = groceries.get(position);
        g.toggleComplete();
        notifyDataSetChanged();
    }

    public Long[] checkoutGroceries() {
        ArrayList<String> completedIds = new ArrayList<String>();
        ArrayList<Grocery> checkoutGroceries = new ArrayList<Grocery>();
        for (Grocery grocery : groceries){
            if (grocery.isComplete()) {
                completedIds.add(grocery.getId());
                checkoutGroceries.add(grocery);
            }
        }
        groceries.removeAll(checkoutGroceries);
        forceReload();
        return completedIds.toArray(new Long[]{});
    }

    public void searchGroceries(String query){
        query = query.toLowerCase();
        groceries.clear();
        if (query.isEmpty()) {
            groceries.addAll(storedGroceries);
        } else {
            for (Grocery grocery : storedGroceries) {
                if (grocery.getName().toLowerCase().contains(query)) {
                    groceries.add(grocery);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        groceries.remove(position);
        notifyDataSetChanged();
    }
}