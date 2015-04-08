package citris.stockup.groceries;

/**
 * Created by Panic on 4/6/2015.
 */
public class Grocery {
    private String name;
    private boolean complete;
    private long id;
    private boolean hidden = false;

    public Grocery(String groceryName) {
        name = groceryName;
    }

    public void setName(String groceryName) {
        this.name = groceryName;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return name;
    }

    public boolean isComplete() {
        return complete;
    }
    
    public boolean isHidden() {
        return hidden;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void toggleComplete() {
        complete = !complete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
