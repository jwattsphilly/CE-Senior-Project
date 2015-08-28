package nocomment.smartthermalmicrowave;

import java.util.ArrayList;
import java.sql.*;

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
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/NoComment", "sa", "");

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
            System.err.println("Uh, oh!  Something bad happened:");
            System.out.println(ex.getMessage());
        }
        finally
        {
            if(conn != null)
                conn.close();
        }
    }


}
