package com.example.navgraphtest.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navgraphtest.Database.DatabaseHelper;
import com.example.navgraphtest.Database.FoodItemModel;
import com.example.navgraphtest.Activities.MainActivity;
import com.example.navgraphtest.R;
import com.example.navgraphtest.RecyclerView.ItemAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AddItemFromCategoryFragment extends Fragment {

    Context context;
    ArrayList<FoodItemModel> arrayList;
    RecyclerView recyclerView;
    Button actionButton;
    DatabaseHelper database_helper;

    ImageButton btn_takePhto, btn_selectPhoto;
    ImageView iv_photo;

    private static final int REQUEST_PHOTO_GALLERY = 99;
    static final int REQUEST_IMAGE_CAPTURE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        context = getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item_from_category, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);

        // get components in layout
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
    public void onResume() {
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

    private byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void selectPhoto() {
        Intent selectPhotoIntent = new Intent(Intent.ACTION_PICK);
        selectPhotoIntent.setType("image/*");
        if(selectPhotoIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(selectPhotoIntent, REQUEST_PHOTO_GALLERY);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        else{
            Toast.makeText(context, "Camera app is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iv_photo.setImageBitmap(imageBitmap);
        }
        else if(requestCode == REQUEST_PHOTO_GALLERY && resultCode == Activity.RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                iv_photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showPhotoDialog() {
        Log.d(TAG, "showPhotoDialog: ");
        final TextView takePhoto, choosePhoto;
        Button cancel;

        // Create new dialog window.
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog_photo);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        choosePhoto = dialog.findViewById(R.id.tv_choosePhoto);
        takePhoto = dialog.findViewById(R.id.tv_takePhoto);
        cancel = dialog.findViewById(R.id.btn_cancel);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                dialog.cancel();
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
                dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

    }

    //display dialog_new_item
    public void showDialog() {
        Log.d(TAG, "showDialog: ");
        final EditText itemName, itemCalories;
        Button submit;

        // Create new dialog window.
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
        // Show the dialogWindow.
        dialog.show();

        // Get components in dialog_new_item
        itemName = dialog.findViewById(R.id.t_newitem_name);
        itemCalories = dialog.findViewById(R.id.t_newitem_calorievalue);
        submit = dialog.findViewById(R.id.btn_newitem_submit);
        iv_photo = dialog.findViewById(R.id.iv_photo_dialog);

        // Show new dialog window if user wants to change the photo.
        // I previously had buttons in this layout,
        // but it looks much tidier and is a bit easier to
        // understand with having a separate dialog window for these options.
        // The layout for this was inspired by how Instagram handles changing user profile images.
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoDialog();
            }
        });

        // Handling the submit button.
        submit.setOnClickListener(new View.OnClickListener() {
            ;

            @Override
            public void onClick(View v) {
                // if/else statement to check the textfields aren't empty.
                if (itemName.getText().toString().isEmpty()) {
                    // Display error messages if the fields are empty.
                    itemName.setError("Please Enter Item Name");
                } else if (itemCalories.getText().toString().isEmpty()) {
                    itemCalories.setError("Please Enter Item Calorie Value");
                } else {
                    String name = itemName.getText().toString();
                    int calories = Integer.parseInt(itemCalories.getText().toString());
                    // Create new item in the database with all the data that is in the dialogWindow.
                    database_helper.addFoodItem(new FoodItemModel(name, calories,
                            getArguments().getString("category_name"),
                            imageViewToByte(iv_photo)));
                    // Close the dialogWindow.
                    dialog.cancel();
                    // Update the list.
                    displayItems();
                }
            }
        });
    }
}



