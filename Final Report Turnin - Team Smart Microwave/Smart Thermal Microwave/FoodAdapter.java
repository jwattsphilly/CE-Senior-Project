package nocomment.smartthermalmicrowave;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by Darin on 11/16/15.
 */
public class FoodAdapter extends BaseAdapter {
    List<FoodItem> foodItemsReturned = null;
    Activity activity = null;
    private static LayoutInflater inflater = null;

    public FoodAdapter(Activity activity, List<FoodItem> resultsReturned) {
        foodItemsReturned = resultsReturned;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return foodItemsReturned.size();
    }

    @Override
    public Object getItem(int position) {
        return foodItemsReturned.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.custom_list, null);
        ImageView image = (ImageView) rowView.findViewById(R.id.lv_image_view);
        TextView foodType = (TextView) rowView.findViewById(R.id.lv_text_view_food_type);
        TextView brandName = (TextView) rowView.findViewById(R.id.lv_text_view_brand_name);

//        String filename = "test_filename";
//        File filepath = activity.getFileStreamPath(filename);
//        image.setImageDrawable(Drawable.createFromPath(filepath.toString()));

        FoodItem item = foodItemsReturned.get(position);
        if(item.getImage() == null)
            image.setImageResource(R.drawable.microwave);
        else
        {
            byte[] imageArray = item.getImage();
            Bitmap foodBitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            image.setImageBitmap(foodBitmap);
        }

        foodType.setText(item.getFoodType());
        brandName.setText(item.getBrandName());

        return rowView;
    }
}
