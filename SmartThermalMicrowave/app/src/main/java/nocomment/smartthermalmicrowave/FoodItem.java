package nocomment.smartthermalmicrowave;

/**
 * Created by James Watts on 8/28/15.
 */
public class FoodItem{
    private String food_type;
    private String brand_name;
    private boolean frozen;
    private long upc;
    private String instructions;
    private byte[] image;

    /**
     * Constructor for a FoodItem object
     *
     * @author James Watts
     */
    public FoodItem(String food_type, String brand_name, boolean frozen, long upc, String instructions, byte[] image){

        this.food_type = food_type;
        this.brand_name = brand_name;
        this.frozen = frozen;
        this.upc = upc;
        this.instructions = instructions;
        this.image = image;
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

    public byte[] getImage() {
        return image;
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
}
