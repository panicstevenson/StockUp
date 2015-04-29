package citris.stockup.groceries;

import java.util.ArrayList;

/**
 * Created by Panic on 4/26/2015.
 */
public class GroceryList {
    private String name;
    private ArrayList<Grocery> contents;
    private String creator;

    public GroceryList(String name){
        this.name = name;
        this.contents = new ArrayList<Grocery>();
    }

    public GroceryList(String name, ArrayList<Grocery> contents) {
        this.name = name;
        this.contents = contents;
    }

    public void addGrocery (Grocery g) {
        this.contents.add(g);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<Grocery> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Grocery> contents) {
        this.contents = contents;
    }
}
