package com.example.navgraphtest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    // Declarations.
    Context context;
    AppCompatActivity activity;
    ArrayList<CategoryModel> arrayList;
    DatabaseHelper database_helper;

    // Added NavController here to create onClickListeners when tapping a category item.
    NavController navController;

    // Constructor
    public CategoryAdapter(Context context, AppCompatActivity activity, ArrayList<CategoryModel> arrayList, NavController navController) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
        this.navController = navController;
    }

    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // get list_item layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.viewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Databinding
        // get the categoryTitle for the current position in arrayList.
        final String title = (arrayList.get(position).getCategoryTitle());

        // Set title in list_item to categoryTitle
        holder.title.setText(title);

        database_helper = new DatabaseHelper(context);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("category_name", (String) holder.title.getText());
                navController.navigate(R.id.action_categoryQuickAddFragment_to_addItemFromCategoryFragment, bundle);
            }
        });

        // Set up delete icon with datbasehelper.deleteCategory()
        holder.delete.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                //deleting note
                database_helper.deleteCategory(arrayList.get(position).getCategoryID());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        // Set up edit icon.
        holder.edit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                // Open dialog for current item
                showDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void showDialog(final int pos) {
        final EditText title;
        Button submit;

        // Create new dialog window.
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog_new_category);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = dialog.findViewById(R.id.t_category_title);
        submit = dialog.findViewById(R.id.btn_category_submit);

        // Get existing title.
        title.setText(arrayList.get(pos).getCategoryTitle());

        submit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    // Error if EditText is empty when submitted.
                    title.setError("Please Enter Category Name");
                } else {
                    // Updating db.
                    database_helper.updateCategory(title.getText().toString(), arrayList.get(pos).getCategoryID());
                    arrayList.get(pos).setCategoryTitle(title.getText().toString());

                    // Close dialog.
                    dialog.cancel();

                    //
                    notifyDataSetChanged();
                }
            }
        });
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        Button button;
        TextView title;
        ImageView delete, edit;

        public viewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}