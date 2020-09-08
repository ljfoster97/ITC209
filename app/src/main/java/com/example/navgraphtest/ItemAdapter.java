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

    Context context;
    AppCompatActivity activity;
    ArrayList<FoodItemModel> arrayList;
    DatabaseHelper database_helper;

    public ItemAdapter(Context context, AppCompatActivity activity, ArrayList<FoodItemModel> arrayList) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
    }

    @Override
    public ItemAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.viewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final String itemName = (arrayList.get(position).getItemName());
        final int calorieValue = (arrayList.get(position).getItemCalories());

        holder.title.setText(itemName);
        holder.calories.setText(String.valueOf(calorieValue));

        database_helper = new DatabaseHelper(context);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, itemName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting note
                database_helper.deleteFoodItem(arrayList.get(position));
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {;
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

    public class viewHolder extends RecyclerView.ViewHolder {
        Button button;
        TextView title,calories;
        ImageView delete, edit;
        public viewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            calories = (TextView) itemView.findViewById(R.id.description);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            edit = (ImageView) itemView.findViewById(R.id.edit);
        }
    }

    public void showDialog(final int pos) {
        final EditText title, calories;
        Button submit;
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

        title = (EditText) dialog.findViewById(R.id.t_newitem_name);
        calories = (EditText) dialog.findViewById(R.id.t_newitem_calorievalue);
        submit = (Button) dialog.findViewById(R.id.btn_newitem_submit);

        title.setText(arrayList.get(pos).getItemName());
        calories.setText(String.valueOf(arrayList.get(pos).getItemCalories()));

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Please Enter Item Name");
                }else if(calories.getText().toString().isEmpty()) {
                    calories.setError("Please Enter Item Calorie Value");
                }else {
                    //updating note
                    database_helper.updateFoodItem(arrayList.get(pos));
                    arrayList.get(pos).setItemName(title.getText().toString());
                    arrayList.get(pos).setItemCalories(Integer.parseInt(calories.getText().toString()));
                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });
    }
}