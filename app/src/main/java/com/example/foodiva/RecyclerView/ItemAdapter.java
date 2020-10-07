package com.example.foodiva.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodiva.Database.DatabaseHelper;
import com.example.foodiva.Database.FoodItemModel;
import com.example.foodiva.R;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {
    // Declarations.
    Context context;
    AppCompatActivity activity;
    ArrayList<FoodItemModel> arrayList;
    DatabaseHelper databaseHelper;

    // Constructor for ItemAdapter.
    public ItemAdapter(Context context, AppCompatActivity activity, ArrayList<FoodItemModel> arrayList) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
    }

    // get list_item layout for the viewHolder
    @Override
    public ItemAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new viewHolder(view);
    }

    // Databinding
    @Override
    public void onBindViewHolder(final ItemAdapter.viewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Get the itemName and calorieValue for item at current position in the ArrayList of foodItems.
        final String itemName = (arrayList.get(position).getItemName());
        final int calorieValue = (arrayList.get(position).getItemCalories());
        final byte[] photo = (arrayList.get(position).getPhoto());

        // Convert BLOB to bitmap and display in the items ImageView
        ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        holder.photo.setImageBitmap(bitmap);

        // Set title and calorie within the list_item layout.
        holder.title.setText(itemName);
        String calorieValueString = calorieValue + " Calories";
        holder.calories.setText(calorieValueString);

        databaseHelper = new DatabaseHelper(context);

//        // Testing recyclerView.
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, itemName, Toast.LENGTH_SHORT).show();
//            }
//        });

        // Set up onClickListener with databaseHelper for delete icon.
        holder.delete.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                //deleting note
                databaseHelper.deleteFoodItem(arrayList.get(position));
                arrayList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });

//        // Open dialog with current value for edit icon.
//        holder.edit.setOnClickListener(new View.OnClickListener() {;
//            @Override
//            public void onClick(View v) {
//                //display edit dialog_new_category
//                showDialog(position);
//            }
//        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "item has been clicked", Toast.LENGTH_LONG).show();
                //display edit dialog_new_category
                showDialog(position);
                return false;
            }
        });
    }

    // Dialog box for creating/editing items.
    // Essentially the same as the one in CategoryAdapter.
    public void showDialog(final int position) {
        final EditText title, calories;
        ImageView imageView;
        Button submit;

        // Creating new dialog window.
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog_new_item);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // Get components in dialog_new_item layout.
        title = dialog.findViewById(R.id.et_itemdialog_itemname);
        calories = dialog.findViewById(R.id.et_itemdialog_calorievalue);
        submit = dialog.findViewById(R.id.btn_itemdialog_submit);
        imageView = dialog.findViewById(R.id.iv_itemdialog_photo);

        // Set to current values
        title.setText(arrayList.get(position).getItemName());
        calories.setText(String.valueOf(arrayList.get(position).getItemCalories()));

        // Convert BLOB to bitmap and display in the items ImageView
        final byte[] photo = (arrayList.get(position).getPhoto());
        ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(bitmap);

        submit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    // Error if editText empty when submitted.
                    title.setError("Please Enter Item Name");
                } else if (calories.getText().toString().isEmpty()) {
                    // Error if editText empty when submitted.
                    calories.setError("Please Enter Item Calorie Value");
                } else {
                    // Updating the item within db
                    databaseHelper.updateFoodItem(arrayList.get(position));
                    arrayList.get(position).setItemName(title.getText().toString());
                    arrayList.get(position).setItemCalories(Integer.parseInt(calories.getText().toString()));
                    dialog.cancel();

                    // Update
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // setting up list_item layout with viewHolder
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, calories;
        ImageView delete, edit, photo;

        public viewHolder(View itemView) {
            super(itemView);
            // Get components in list_item layout.
            title = itemView.findViewById(R.id.tv_listitem_title);
            calories = itemView.findViewById(R.id.tv_listitem_description);
            delete = itemView.findViewById(R.id.iv_listitem_delete);
            photo = itemView.findViewById(R.id.civ_listitem_photo);
        }
    }

}