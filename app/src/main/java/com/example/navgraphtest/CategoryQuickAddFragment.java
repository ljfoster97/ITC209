package com.example.navgraphtest;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class CategoryQuickAddFragment extends Fragment {
    NavController navController;
    ArrayList<CategoryModel> arrayList;
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    DatabaseHelper database_helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_quick_add, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getView());

        recyclerView = getView().findViewById(R.id.recycler_view_categories);
        actionButton = getView().findViewById(R.id.btn_category_add);
        database_helper = new DatabaseHelper(getActivity());


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        displayCategories();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        // Update ActionBar titles in MainActivity to reflect current fragment
        ((MainActivity) getActivity()).setActionBarTitle("Add Item to Journal");
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Select by category or quick add recent items");
    }


    //display category list
    public void displayCategories() {
        Log.d(TAG, "displayNotes: ");
        // get ArrayList of Categories
        arrayList = new ArrayList<>(database_helper.getCategories());

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Create new CategoryAdapter and populate recyclerView with the ArrayList of Categories.
        CategoryAdapter adapter = new CategoryAdapter(getActivity().getApplicationContext(), (AppCompatActivity) getActivity(), arrayList, navController);
        recyclerView.setAdapter(adapter);
    }

    //display dialog_new_category
    public void showDialog() {
        Log.d(TAG, "showDialog: ");
        final EditText title;
        Button submit;

        // Creating dialog window.
        final Dialog dialog = new Dialog(getActivity());
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

        submit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    // Error if EditText is empty when submitted.
                    title.setError("Please Enter Category Name");
                } else {
                    // Create new category in DB from EditText string.
                    database_helper.addCategory(title.getText().toString());

                    // Close dialog window.
                    dialog.cancel();

                    // Update
                    displayCategories();
                }
            }
        });
    }
}



