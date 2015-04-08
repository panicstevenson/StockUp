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
    private ArrayList<Grocery> storedGroceries;
    private ArrayList<Long> hiddenItems = new ArrayList<Long>();
    private Context context;

    public GroceryListAdapter(ArrayList<Grocery> groceries, Context context) {
        super();
        this.groceries = groceries;
        this.context = context;
    }

    @Override
    public int getCount() {
        return groceries.size() - hiddenItems.size();
    }

    @Override
    public Grocery getItem(int position) {
        if (null == groceries) {
            return null;
        } else {
            /*for (Long hiddenIndex : hiddenItems) {
                if (hiddenIndex == position) {
                    position++;
                }
            }
        }*/
        return groceries.get(position); }
    }

    @Override
    public long getItemId(int position) {
        boolean hidden = false;
        for (Long hiddenIndex : hiddenItems) {
            if (hiddenIndex == position){
                hidden = true;
            }
        }
        if (!hidden) {
            return position;
        }
        return 0;
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
        notifyDataSetChanged();
    }

    public void toggleTaskCompleteAtPosition(int position) {
        Grocery g = groceries.get(position);
        g.toggleComplete();
        notifyDataSetChanged();
    }

    public Long[] checkoutGroceries() {
        ArrayList<Long> completedIds = new ArrayList<Long>();
        ArrayList<Grocery> checkoutGroceries = new ArrayList<Grocery>();
        for (Grocery grocery : groceries){
            if (grocery.isComplete()) {
                completedIds.add(grocery.getId());
                checkoutGroceries.add(grocery);
            }
        }
        groceries.removeAll(checkoutGroceries);
        notifyDataSetChanged();
        return completedIds.toArray(new Long[]{});
    }

    public void searchGroceries(String query)
    {
        query = query.toLowerCase();
        /*Log.v("MyListAdapter", String.valueOf(groceries.size()));
        groceries.clear();*/

        if (!query.isEmpty()) {
            for (Grocery grocery : groceries) {
                if (grocery.getName().toLowerCase().contains(query)) {
                    hiddenItems.add(grocery.getId());
                }
            }
        } else {
            hiddenItems.clear();
        }
                /*if(newList.size() > 0) {
                    Grocery nContinent = new Continent(grocery.getName(), newList);
                    groceries.add(nContinent);
                }*/
//        Log.v("MyListAdapter", String.valueOf(groceries.size()));
        notifyDataSetChanged();
    }

    public void storeGroceries() {
        storedGroceries = groceries;
    }

    public void restoreGroceries() {
        groceries = storedGroceries;
    }
}