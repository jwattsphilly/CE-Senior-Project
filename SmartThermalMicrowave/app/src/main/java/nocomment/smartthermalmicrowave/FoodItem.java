package nocomment.smartthermalmicrowave;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by James Watts on 8/28/15.
 */
public class FoodItem implements Parcelable{
    private String food_type;
    private String brand_name;
    private boolean frozen;
    private long upc;
    private String instructions;

    /**
     * Constructor for a FoodItem object
     *
     * @author James Watts
     */
    public FoodItem(String food_type, String brand_name, boolean frozen, long upc, String instructions){

        this.food_type = food_type;
        this.brand_name = brand_name;
        this.frozen = frozen;
        this.upc = upc;
        this.instructions = instructions;
    }

    /**
     * Getter method for the food type field
     */
    public String getFoodType(){
        return food_type;
    }

    /**
     * Getter method for the brand name field
     */
    public String getBrandName(){
        return brand_name;
    }

    /**
     * Getter method for the frozen field
     */
    public boolean getFrozen(){
        return frozen;
    }

    /**
     * Getter method for the UPC barcode field
     */
    public long getUPC(){
        return upc;
    }

    /**
     * Getter method for the instructions field
     */
    public String getInstructions(){
        return instructions;
    }


    @Override
    public String toString()
    {
        String frozenString;
        if(frozen)
            frozenString = "Frozen";
        else
            frozenString = "Not Frozen";

        return "Food Type: " + food_type + " (" + frozenString + ")" + "\tBrand Name: " + brand_name ;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
