package citris.stockup.groceries;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Panic on 4/26/2015.
 */
public class GroceryList implements Parcelable {
    private String name;
    private ArrayList<Grocery> contents;
    private String creator;
    private String key;
    private String id;

    public GroceryList(String name){
        this.name = name;
        this.contents = new ArrayList<Grocery>();
    }

    public GroceryList(String name, ArrayList<Grocery> contents) {
        this.name = name;
        this.contents = contents;
    }

    public GroceryList(String name, String creator, String key) {
        this.name = name;
        this.contents = new ArrayList<Grocery>();
        this.creator = creator;
        this.key = key;
    }

    //Parcelable
    public GroceryList(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        this.name = data[0];
        this.creator = data[1];
        this.key = data[2];
        this.id = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.name, this.creator, this.key, this.id});
    }

    public static final Parcelable.Creator<GroceryList> CREATOR = new Parcelable.Creator<GroceryList>() {

        @Override
        public GroceryList createFromParcel(Parcel source) {
            return new GroceryList(source);
        }

        @Override
        public GroceryList[] newArray(int size) {
            return new GroceryList[size];
        }
    };

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
