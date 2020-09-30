package com.example.navgraphtest.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navgraphtest.R;

import static android.content.ContentValues.TAG;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button getTdee;
        getTdee = findViewById(R.id.btn_calculate_tdee);
        getTdee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTdeeDialog();
            }
        });

    }

    public void showTdeeDialog() {
        Log.d(TAG, "showTdeeDialog: ");
        RadioButton male, female;
        EditText age, height, weight;

        // Create new dialog window.
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog_tdee);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Show the dialogWindow.
        dialog.show();

        // Get components in dialog_tdee
        male = findViewById(R.id.rb_male);
        female = findViewById(R.id.rb_female);
        age = findViewById(R.id.editText_age);
        height = findViewById(R.id.editText_height);
        weight = findViewById(R.id.editText_weight);

    }


}