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
import android.widget.TextView;

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


    public void calculateTdee(Boolean isMale, int age, int height, int weight) {
        double heightMetres = (height * 1.0) / (100);
        double bmi = (weight / (heightMetres * heightMetres));

        TextView bmiResult = findViewById(R.id.tv_BMI_result);
        TextView bmrResult = findViewById(R.id.tv_BMR_result);
        TextView tdeeResult = findViewById(R.id.tv_TDEE_result);

        int formulaNumber = -1;
        if (isMale) {
            formulaNumber = 5;
        } else {
            formulaNumber = -151;
        }
        double activityMultiplier = 1.2;
        double bmrDouble = (((10) * weight) + (6.25 * height) - (5 * age + formulaNumber));
        double tddeDouble = bmrDouble * activityMultiplier;
        int bmr = (int) bmrDouble;
        bmiResult.setText("" + bmi);
        tdeeResult.setText("" + tddeDouble);
        bmrResult.setText("" + bmr);
    }


    public void showTdeeDialog() {
        Log.d(TAG, "showTdeeDialog: ");
        final RadioButton rb_male, rb_female;
        final EditText tv_age, tv_height, tv_weight;
        final Button btn_tdee_submit;

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
        rb_male = dialog.findViewById(R.id.rb_male);
        rb_female = dialog.findViewById(R.id.rb_female);
        tv_age = dialog.findViewById(R.id.editText_age);
        tv_height = dialog.findViewById(R.id.editText_height);
        tv_weight = dialog.findViewById(R.id.editText_weight);
        btn_tdee_submit = dialog.findViewById(R.id.btn_this_is_a_unique_button_name);

        btn_tdee_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isMale = true;
                int age = 20;
                int height;
                int weight;
                if (tv_age.getText().toString().isEmpty()) {
                    // Display error messages if the fields are empty.
                    tv_age.setError("Please Enter Your Age");
                } else if (tv_height.getText().toString().isEmpty()) {
                    tv_height.setError("Please Enter Your Height");
                } else if (tv_weight.getText().toString().isEmpty()) {
                    tv_weight.setError("Please Enter Your Weight");
                } else if (rb_male.isChecked()) {
                    isMale = true;
                } else if (rb_female.isChecked()) {
                    isMale = false;
                } else
                    age = Integer.parseInt(String.valueOf(tv_age.getText()));
                height = Integer.parseInt(String.valueOf(tv_height.getText()));
                weight = Integer.parseInt(String.valueOf(tv_weight.getText()));
                calculateTdee(isMale, age, height, weight);
                dialog.cancel();

            }
        });
    }


}