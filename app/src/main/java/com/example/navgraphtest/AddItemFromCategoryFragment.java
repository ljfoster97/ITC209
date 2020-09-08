package com.example.navgraphtest;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AddItemFromCategoryFragment extends Fragment {

    ArrayList<FoodItemModel> arrayList;
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    DatabaseHelper database_helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item_from_category, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.recycler_view_items);
        actionButton = getView().findViewById(R.id.btn_additem_newitem);
        database_helper = new DatabaseHelper(getActivity());
        displayItems();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume: ");
        super.onResume();
        // Update ActionBar titles in MainActivity to reflect current fragment
        ((MainActivity) getActivity()).setActionBarTitle("Viewing Items by Category");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Select by category or quick add recent items");
    }


    //display notes list
    public void displayItems() {
        Log.d(TAG, "displayNotes: ");
        arrayList = new ArrayList<>(database_helper.getFoodItemsByCategory(getArguments().getString("category_name")));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemAdapter adapter = new ItemAdapter(getActivity().getApplicationContext(), (AppCompatActivity) getActivity(), arrayList);
        recyclerView.setAdapter(adapter);
    }

    //display dialog_new_item
    public void showDialog() {
        Log.d(TAG, "showDialog: ");
        final EditText itemName, itemCalories;
        Button submit;
        final Dialog dialog = new Dialog(getActivity());
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

        itemName = (EditText) dialog.findViewById(R.id.t_newitem_name);
        itemCalories = (EditText) dialog.findViewById(R.id.t_newitem_calorievalue);
        submit = (Button) dialog.findViewById(R.id.btn_newitem_submit);

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (itemName.getText().toString().isEmpty()) {
                    itemName.setError("Please Enter Item Name");
                }else if(itemCalories.getText().toString().isEmpty()) {
                itemCalories.setError("Please Enter Item Calorie Value");
                }else {
                    String name = itemName.getText().toString();
                    int calories = Integer.parseInt(itemCalories.getText().toString());
                    database_helper.addFoodItem(new FoodItemModel(name, calories, getArguments().getString("category_name")));
                    dialog.cancel();
                    displayItems();
                }
            }
        });
    }
}



