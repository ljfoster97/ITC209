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

    NavController navController;
    Context context;
    AppCompatActivity activity;
    ArrayList<CategoryModel> arrayList;
    DatabaseHelper database_helper;

    public CategoryAdapter(Context context, AppCompatActivity activity, ArrayList<CategoryModel> arrayList, NavController navController) {
        this.context = context;
        this.activity  = activity ;
        this.arrayList = arrayList;
        this.navController = navController;
    }

    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.viewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final String title = (arrayList.get(position).getCategoryTitle());

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

        holder.delete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting note
                database_helper.deleteCategory(arrayList.get(position).getCategoryID());
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
        TextView title;
        ImageView delete, edit;
        public viewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            edit = (ImageView) itemView.findViewById(R.id.edit);
        }
    }

    public void showDialog(final int pos) {
        final EditText title;
        Button submit;
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

        title = (EditText) dialog.findViewById(R.id.t_category_title);
        submit = (Button) dialog.findViewById(R.id.btn_category_submit);

        title.setText(arrayList.get(pos).getCategoryTitle());

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Please Enter Category Name");
                }else {
                    //updating note
                    database_helper.updateCategory(title.getText().toString(), arrayList.get(pos).getCategoryID());
                    arrayList.get(pos).setCategoryTitle(title.getText().toString());

                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });
    }
}