package citris.stockup.groceries;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

/**
 * Created by Panic on 4/6/2015.
 */
public class Grocery implements Parcelable {
    private String name;
    private String brand;
    private String category = "Unlisted";
    private int ttlInt;
    private int ttlType;
    private int quantityInt;
    private int quantityType;
    private int ttl;
    private String list;

    private boolean complete;
    private String id;
    private boolean delete;

    public Grocery(String groceryName) {
        name = groceryName;
        ttl = 1;
    }

    public Grocery(String groceryName, int groceryQuantityInt, int groceryQuantityType, String groceryBrand, int groceryTTL, int groceryTTLType, String groceryCategory) {
        name = groceryName;
        quantityInt = groceryQuantityInt;
        quantityType = groceryQuantityType;
        brand = groceryBrand;
        ttlInt = groceryTTL;
        ttlType = groceryTTLType;
        category = groceryCategory;
        ttl = convertTTL(groceryTTL, groceryTTLType);
    }

    public Grocery(String groceryName, int groceryQuantityInt, int groceryQuantityType, String groceryBrand, int groceryTTL, int groceryTTLType, String groceryCategory, String identity) {
        name = groceryName;
        quantityInt = groceryQuantityInt;
        quantityType = groceryQuantityType;
        brand = groceryBrand;
        ttlInt = groceryTTL;
        ttlType = groceryTTLType;
        category = groceryCategory;
        ttl = convertTTL(groceryTTL, groceryTTLType);
        id = identity;
    }

    public Grocery(String groceryName, int groceryQuantityInt, int groceryQuantityType, String groceryBrand, int groceryTTL, int groceryTTLType, String groceryCategory, String identity, String table) {
        name = groceryName;
        quantityInt = groceryQuantityInt;
        quantityType = groceryQuantityType;
        brand = groceryBrand;
        ttlInt = groceryTTL;
        ttlType = groceryTTLType;
        category = groceryCategory;
        ttl = convertTTL(groceryTTL, groceryTTLType);
        id = identity;
        list = table;
    }

    //Parcelable
    public Grocery(Parcel in) {
        String[] data = new String[9];
        in.readStringArray(data);
        this.name = data[0];
        this.quantityInt = Integer.parseInt(data[1]);
        this.quantityType = Integer.parseInt(data[2]);
        this.brand = data[3];
        this.ttlInt = Integer.parseInt(data[4]);
        this.ttlType = Integer.parseInt(data[5]);
        this.category = data[6];
        this.id = data[7];
        this.list = data[8];
        this.ttl = convertTTL(Integer.parseInt(data[4]), Integer.parseInt(data[5]));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.name, String.valueOf(this.quantityInt), String.valueOf(this.quantityType), this.brand, String.valueOf(this.ttlInt), String.valueOf(this.ttlType), this.category, this.id, this.list});
    }

    public static final Parcelable.Creator<Grocery> CREATOR = new Parcelable.Creator<Grocery>() {

        @Override
        public Grocery createFromParcel(Parcel source) {
            return new Grocery(source);
        }

        @Override
        public Grocery[] newArray(int size) {
            return new Grocery[size];
        }
    };

    public void setName(String groceryName) {
        this.name = groceryName;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTtlInt() {
        return ttlInt;
    }

    public void setTtlInt(int ttlInt) {
        this.ttlInt = ttlInt;
    }

    public String getTtlType() {
        switch (ttlType) {
            case 0 :
                return "Day(s)";
            case 1 :
                return "Week(s)";
            case 2 :
                return "Month(s)";
        }
        return null;
    }

    public void setTtlType(int ttlType) {
        this.ttlType = ttlType;
    }

    public int getQuantityInt() {
        return quantityInt;
    }

    public void setQuantityInt(int quantityInt) {
        this.quantityInt = quantityInt;
    }

    public String getQuantityType() {
        switch (quantityType) {
            case 0 :
                return "Package(s)";
            case 1 :
                return "Item(s)";
            case 2 :
                return "Other";
        }
        return null;
    }

    public int getQuantityTypePos() {
        return quantityType;
    }

    public void setQuantityType(int quantityType) {
        this.quantityType = quantityType;
    }

    public String toString(){
        return name;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void toggleComplete() {
        complete = !complete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public int getTTL() {
        return convertTTL(this.ttlInt, this.ttlType);
    }

    private int convertTTL (int ttlInt, int ttlType) {
        switch (ttlType) {
            case 0 :
                return ttlInt * 1;  //Days
            case 1 :
                return ttlInt * 7;  //Weeks
            case 2 :
                return ttlInt * 30; //Months
        }
        return 0;
    }

    public int getTtlTypePos() {
        return ttlType;
    }

    public boolean getDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
}
