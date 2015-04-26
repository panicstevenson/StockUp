package citris.stockup.groceries;

import java.util.ArrayList;

/**
 * Created by Panic on 4/26/2015.
 */
public class GroceryList {
    private String name;
    private ArrayList<Grocery> contents;

    public GroceryList(String name, ArrayList<Grocery> contents) {
        this.name = name;
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Grocery> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Grocery> contents) {
        this.contents = contents;
    }
}
