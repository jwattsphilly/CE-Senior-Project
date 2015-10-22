package nocomment.smartthermalmicrowave;

import android.util.Log;

import java.util.ArrayList;
import java.sql.*;
import java.util.List;

/**
 * Created by jameswatts on 8/28/15.
 */
public class LocalDatabase {

    static public ArrayList<FoodItem> foodList = new ArrayList<FoodItem>();

    /**
     *
     * @throws SQLException
     */
    static public void populateFoodList() throws SQLException {

        foodList.clear();   // Empty the list before refilling it

        // Try to connect to the database
        Connection conn = null;
        try
        {
            Class.forName("org.h2.Driver");
            DriverManager.setLoginTimeout(10);
            conn = DriverManager.getConnection("jdbc:h2:tcp://nocomment.sipnswirlutah.com:9092/~/NoComment", "sa", "");

            // Obtain all of the contents from the Food table
            PreparedStatement query = conn.prepareStatement("SELECT * FROM FOOD;");
            ResultSet results = query.executeQuery();

            while(results.next())
            {
                // Create a new FoodItem with the following values:
                String food_type = results.getString("FOOD_TYPE");
                String brand_name = results.getString("BRAND_NAME");
                boolean frozen = results.getBoolean("FROZEN");
                long upc = results.getLong("UPC_BARCODE");
                String instructions = results.getString("INSTRUCTIONS");

                FoodItem newItem = new FoodItem(food_type, brand_name, frozen, upc, instructions);

                foodList.add(newItem);
            }
        }
        catch(Exception ex)
        {
            Log.e("LocalDatabase","Uh, oh!  Something bad happened:");
            Log.e("LocalDatabase", ex.getMessage());
        }
        finally
        {
            if(conn != null)
                conn.close();   // Close the connection to the database
        }
    }

    public static List<FoodItem> getMatches(String foodName){
        List<FoodItem> returnList = new ArrayList<FoodItem>();
        for (FoodItem item:foodList) {
            if(item.getFoodType().toLowerCase().contains(foodName.toLowerCase()) ||
                    item.getBrandName().toLowerCase().contains(foodName.toLowerCase())){
                returnList.add(item);
            }
        }
        return returnList;
    }

    public static List<FoodItem> getMatches(long UPC){
        List<FoodItem> returnList = new ArrayList<FoodItem>();
        for (FoodItem item:foodList) {
            if(item.getUPC() == UPC) {
                returnList.add(item);
            }
        }
        return returnList;
    }

    public static String parseInstructionString(FoodItem food)
    {
        return parseInstructionString(food.getInstructions());
    }

    public static String parseInstructionString(String instructions)
    {
        StringBuilder sb = new StringBuilder();

        for(int index=0; index<instructions.length(); index++)
        {
            switch(instructions.charAt(index))
            {
                case 't':
                    sb.append("Set timer to: ");

                    try{
                        String restOfString = instructions.substring(index+1);

                        String[] numbers = restOfString.split("[a-zA-Z]+");

                        if(numbers[0].equals(""))
                            sb.append(numbers[1] + " Seconds\n");
                        else
                            sb.append(numbers[0] + " Seconds\n");
                    }
                    catch(Exception ex){
                        sb.append("Couldn't find Number!\n");
                    }
                    break;

                case 'p':
                    sb.append("Power Level Set to: ");

                    try{
                        if(instructions.charAt(index+1) == 'i')
                        {
                            String restOfString = instructions.substring(index+1);
                            String[] numbers = restOfString.split("[a-zA-Z]+");

                            if(numbers[0].equals(""))
                                sb.append(numbers[1] + "\n");
                            else
                                sb.append(numbers[0] + "\n");
                        }
                        else if(instructions.charAt(index+1) == 'l')
                        {
                            switch(instructions.charAt(index+2))
                            {
                                case 'h': sb.append("High (10)\n"); break;
                                case 'm': sb.append("Medium (7)\n"); break;
                                case 'l': sb.append("Low (5)\n"); break;
                                case 'd': sb.append("Defrost (3)\n"); break;
                                case 'z': sb.append("Zero (0)\n"); break;
                                default: sb.append("Unknown power level\n"); break;
                            }
                        }
                    }
                    catch(Exception ex){
                        sb.append("No Power Level Given\n");
                    }
                    break;

                case 's':
                    sb.append("Start\n");
                    break;

                case 'S':
                    sb.append("Stop\n");
                    break;

                case 'P':
                    sb.append("Pause\n");
                    break;

                case 'r':
                    sb.append("Stir\n");
                    break;

                default:
                    break;
            }
        }
        return sb.toString();
    }

    public static ArrayList<String> splitInstructions(String instructions)
    {
        ArrayList<String> returnList = new ArrayList<String>();
        String restOfString = instructions;

        int stirIndex = restOfString.indexOf('r');  // Get first stir instruction (if it exists)

        while(stirIndex >= 0)
        {
            // Get the first part of the instruction
            returnList.add(restOfString.substring(0, stirIndex));
            // Get the rest of the instruction
            restOfString = restOfString.substring(stirIndex+1);
            // Check to see if there are anymore stir instructions
            stirIndex = restOfString.indexOf('r');
        }

        returnList.add(restOfString);  // Add the last instructions

        return returnList;
    }

    public static int getTotalSeconds(String fullCodedInstruction)
    {
        int seconds = 0;

        for(int i=0; i<fullCodedInstruction.length(); i++)
        {
            if(fullCodedInstruction.charAt(i) == 't')
            {
                try{
                    String restOfString = fullCodedInstruction.substring(i+1);
                    String[] numbers = restOfString.split("[a-zA-Z]+");
                    seconds += Integer.parseInt(numbers[0]);
                }
                catch(Exception ex){
                    // String not formatted correctly
                }
            }
        }

        return seconds;
    }

    public static int getTotalSeconds(ArrayList<String> instructionList)
    {
        int seconds = 0;

        for (String inst:instructionList) {
            seconds += getTotalSeconds(inst);
        }

        return seconds;
    }

    public static String secondsToString(int time)
    {
        int minutes = time / 60;
        int seconds = time % 60;

        String minutesString = (minutes > 9) ? ""+minutes : "0"+minutes;
        String secondsString = (seconds > 9) ? ""+seconds : "0"+seconds;

        return minutesString + ":" + secondsString;
    }

}
