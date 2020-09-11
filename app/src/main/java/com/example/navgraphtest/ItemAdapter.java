package com.example.navgraphtest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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

        // Set title and calorie within the list_item layout.
        holder.title.setText(itemName);
        holder.calories.setText(String.valueOf(calorieValue));

        databaseHelper = new DatabaseHelper(context);

        // Testing recyclerView.
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, itemName, Toast.LENGTH_SHORT).show();
            }
        });

        // Set up onClickListener with databaseHelper for delete icon.
        holder.delete.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                //deleting note
                databaseHelper.deleteFoodItem(arrayList.get(position));
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        // Open dialog with current value for edit icon.
        holder.edit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                //display edit dialog_new_category
                showDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // Dialog box for creating/editing items.
    // Essentially the same as the one in CategoryAdapter.
    public void showDialog(final int pos) {
        final EditText title, calories;
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
        title = dialog.findViewById(R.id.t_newitem_name);
        calories = dialog.findViewById(R.id.t_newitem_calorievalue);
        submit = dialog.findViewById(R.id.btn_newitem_submit);

        // Set to current values
        title.setText(arrayList.get(pos).getItemName());
        calories.setText(String.valueOf(arrayList.get(pos).getItemCalories()));

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
                    databaseHelper.updateFoodItem(arrayList.get(pos));
                    arrayList.get(pos).setItemName(title.getText().toString());
                    arrayList.get(pos).setItemCalories(Integer.parseInt(calories.getText().toString()));
                    dialog.cancel();

                    // Update
                    notifyDataSetChanged();
                }
            }
        });
    }

    // setting up list_item layout with viewHolder
    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, calories;
        ImageView delete, edit;

        public viewHolder(View itemView) {
            super(itemView);
            // Get components in list_item layout.
            title = itemView.findViewById(R.id.title);
            calories = itemView.findViewById(R.id.description);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}