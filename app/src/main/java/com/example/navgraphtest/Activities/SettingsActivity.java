package com.example.navgraphtest.Activities;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navgraphtest.R;
import com.example.navgraphtest.Utilities.ApplicationSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.example.navgraphtest.Utilities.ApplicationSettings.settingsExist;

/**
 * This activity is opened the first time that the application has been launched.
 * The user is prompted for some info to calculate their daily calorie goal
 * and some other useful data.
 * This data is saved via sharedPreferences settings so that other components/activities
 * in the application can work with it.
 * For example the graph in HomeScreenFragment requires a calorie intake goal value.
 */
public class SettingsActivity extends AppCompatActivity {
    /**
     * Used to round data down for display.
     *
     * @param value  double
     * @param places how many digits to round to.
     * @return rounded double.
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getUserData();

        Button buttonCalculate, buttonSave, buttonReset;
        buttonCalculate = findViewById(R.id.btn_settings_calculate);
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTdeeDialog();
            }
        });

        buttonSave = findViewById(R.id.btn_settings_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserData();
            }
        });


        buttonReset = findViewById(R.id.btn_settings_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // this is the date picker for goal date
        final Calendar myCalendar = Calendar.getInstance();
        final EditText goalDate = findViewById(R.id.et_settings_goal_date);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                goalDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        goalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatePickerDialog datePicker = new DatePickerDialog(SettingsActivity.this,
                        date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));


                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);


                if (ApplicationSettings.settingsExist(SettingsActivity.this)) {
                    String currentDate = ApplicationSettings.getSetting(SettingsActivity.this,
                            "goalDate", "");


                    String[] str = currentDate.split("/");
                    int month = Integer.parseInt(str[0]);
                    int day = Integer.parseInt(str[1]);
                    int year = Integer.parseInt(str[2]);

                    datePicker.updateDate(year, month - 1, day);
                }
                datePicker.show();
            }
        });

    }

    /**
     * Creates a dialog from dialog_tdee.xml
     * Checks for user input, then parses data to calculation method.
     */
    public void showTdeeDialog() {
        Log.d(TAG, "showTdeeDialog: ");
        // Declarations
        final RadioButton rbMale, rbFemale;
        final EditText tvAge, tvHeight, tvWeight;
        final Spinner spinActivityLevel;
        final Button btnTdeeSubmit;

        // Create new dialogWindow.
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // Specify which .xml layout to use for the dialogWindow
        dialog.setContentView(R.layout.dialog_tdee);
        params.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Show the dialogWindow.
        dialog.show();

        // Get components in dialog_tdee
        rbMale = dialog.findViewById(R.id.rb_tdeedialog_male);
        rbFemale = dialog.findViewById(R.id.rb_tdeedialog_female);
        tvAge = dialog.findViewById(R.id.et_tdeedialog_age);
        tvHeight = dialog.findViewById(R.id.et_tdeedialog_height);
        tvWeight = dialog.findViewById(R.id.et_tdeedialog_weight);
        spinActivityLevel = dialog.findViewById(R.id.spin_tdeedialog_activitylevel);
        btnTdeeSubmit = dialog.findViewById(R.id.btn_tdeedialog_submit);

        // Set new onClickListener for the submit button in the window.
        btnTdeeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Declare some variables to pass data from the user input
                // in the window back to the root activity.
                boolean isMale = true;
                int age = 20;
                int height, weight, activityLevel;
                // Check that the fields aren't empty when the submit button is clicked.
                if (tvAge.getText().toString().isEmpty()) {
                    // Display error messages if the fields are empty.
                    tvAge.setError("Please Enter Your Age");
                } else if (tvHeight.getText().toString().isEmpty()) {
                    tvHeight.setError("Please Enter Your Height");
                } else if (tvWeight.getText().toString().isEmpty()) {
                    tvWeight.setError("Please Enter Your Weight");
                    // init as true so no need to check rbMale.isChecked()
                } else if (rbFemale.isChecked()) {
                    isMale = false;
                } else
                    // Get the data from the fields.
                    age = Integer.parseInt(String.valueOf(tvAge.getText()));
                height = Integer.parseInt(String.valueOf(tvHeight.getText()));
                weight = Integer.parseInt(String.valueOf(tvWeight.getText()));
                activityLevel = spinActivityLevel.getSelectedItemPosition();

                // Call method to perform all calculations, parse user input to this method.
                // It's probably best practice to do this with an intent or bundle.
                calculateTdee(isMale, age, height, weight, activityLevel);
                dialog.cancel();
            }
        });
    }

    /**
     * Calculates the user's BMI, BMR & TDEE from input in dialog_tdee.xml
     * Displays results in the SettingsActivity.
     * <p>
     * In future this could have an added feature to suggest
     * a goal weight for the user based on their current BMI.
     */
    public void calculateTdee(Boolean isMale, int age, int height, int weight, int activityLevel) {
        // User inputs height in cm, allowing it to be gotten as int rather than double.
        // It's easier to then calculate the height in metres from the number of cm.
        double heightMetres = (height * 1.0) / (100);
        // Rough BMI calculation.
        double bmi = (weight / (heightMetres * heightMetres));
        // init formulaNumber.
        int formulaNumber = -1;
        // TDEE is equal to the Basal Metabolic Rate multiplied by the activityMultiplier.
        // activityMultiplier relates to how physically active somebody is.
        double activityMultiplier = 1;

        // Get components in view so we can update them.
        TextView bmiResult = findViewById(R.id.tv_settings_bmi_result);
        TextView bmrResult = findViewById(R.id.tv_settings_bmr_result);
        TextView tdeeResult = findViewById(R.id.tv_settings_tdee_result);
        TextView calorieGoalResult = findViewById(R.id.tv_settings_caloriegoal_result);

        // Update the start weight EditText with whatever was input in the dialog.
        // The user may have already lost some weight prior to using the app, this way they can
        // still edit their starting weight from the beginning of their weight loss journey,
        // rather than their weight at the time of downloading this application.
        EditText startWeight = findViewById(R.id.et_settings_start_weight);
        startWeight.setText(String.valueOf(weight));

        // Determine gender.
        if (isMale) {
            formulaNumber = 5;
        } else {
            formulaNumber = -151;
        }

        // Determine activityLevel modifier from indexPosition of spinner array.
        switch (activityLevel) {
            case 0:
                // Sedentary (little to no exercise + work a desk job) = 1.2
                activityMultiplier = 1.2;
                break;
            case 1:
                // Lightly Active (light exercise 1-3 days / week) = 1.375
                activityMultiplier = 1.375;
                break;
            case 2:
                // Moderately Active (moderate exercise 3-5 days / week) = 1.55
                activityMultiplier = 1.550;
                break;
            case (3):
                // Very Active (heavy exercise 6-7 days / week) = 1.725
                activityMultiplier = 1.725;
                break;
            case (4):
                // Extremely Active (very heavy exercise, hard labor job, training 2x / day) = 1.9
                activityMultiplier = 1.9;
                break;
            default:
                activityMultiplier = 1.2;
                break;
        }

        // Calculate the Basal Metabolic Rate.
        // Women: BMR = 655 + (9.6 x weight in kg) + (1.8 x height in cm) - (4.7 x age in years)
        // Men: BMR = 66 + (13.7 x weight in kg) + (5 x height in cm) - (6.8 x age in years)
        double bmrDouble = (((10) * weight) + (6.25 * height) - (5 * age + formulaNumber));
        double tddeDouble = bmrDouble * activityMultiplier;

        // Get integer value of the BMR.
        int bmr = (int) bmrDouble;

        // Update the textViews with the results.
        bmiResult.setText("" + round(bmi, 2));
        tdeeResult.setText("" + tddeDouble);
        bmrResult.setText("" + round(bmr, 2));
        calorieGoalResult.setText("" + round((tddeDouble - 500), 2));

        updateBmiColor();
    }

    /**
     * Checks the value of BMI, concatenates a description to the BMI textView
     * and sets a relevant foreground and background color for the BMI textView.
     */
    public void updateBmiColor() {
        TextView bmiResult;
        double bmi;
        bmiResult = findViewById(R.id.tv_settings_bmi_result);
        bmi = Double.parseDouble(bmiResult.getText().toString().replaceAll("[^.0123456789]", ""));

        // Determine color for BMI
        // BMI below 18.5 is considered underweight.
        if (bmi <= 18.5) {
            bmiResult.setText("" + Math.round(bmi) + " - Underweight");
            bmiResult.setTextColor(getResources().getColor(R.color.bmi_underweight_foreground));
            bmiResult.setBackgroundColor(getResources().getColor(R.color.bmi_underweight_background));
        }
        // Between 18.5-24.9 is normal/healthy
        else if (bmi >= 18.5 & bmi <= 24.9) {
            bmiResult.setText("" + Math.round(bmi) + " - Average");
            bmiResult.setTextColor(getResources().getColor(R.color.bmi_healthy_foreground));
            bmiResult.setBackgroundColor(getResources().getColor(R.color.bmi_healthy_background));
        }
        // 25 - 29.9 is overweight.
        else if (bmi >= 25 & bmi <= 29.9) {
            bmiResult.setText("" + Math.round(bmi) + " - Overweight");
            bmiResult.setTextColor(getResources().getColor(R.color.bmi_overweight_foreground));
            bmiResult.setBackgroundColor(getResources().getColor(R.color.bmi_overweight_background));
        }
        // Above 30 is obese
        else if (bmi >= 29.9 & bmi <= 34.9) {
            bmiResult.setText("" + Math.round(bmi) + " - Obese");
            bmiResult.setTextColor(getResources().getColor(R.color.bmi_obese_foreground));
            bmiResult.setBackgroundColor(getResources().getColor(R.color.bmi_obese_background));
        }
        // Above 35 is morbidly obese.
        else if (bmi >= 34.9) {
            bmiResult.setText("" + Math.round(bmi) + " Morbidly Obese");
            bmiResult.setTextColor(getResources().getColor(R.color.bmi_extreme_foreground));
            bmiResult.setBackgroundColor(getResources().getColor(R.color.bmi_extreme_background));
        }
    }

    public void saveUserData() {
        final String DATE_FORMAT = "dd/MM/yyyy";
        EditText startWeight, goalWeight, goalDate;
        TextView bmi, bmr, tdee, calorieGoal;
        Boolean validData = true;
        Boolean validWeight = true;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        startWeight = findViewById(R.id.et_settings_start_weight);
        goalWeight = findViewById(R.id.et_settings_goal_weight);
        goalDate = findViewById(R.id.et_settings_goal_date);
        bmi = findViewById(R.id.tv_settings_bmi_result);
        bmr = findViewById(R.id.tv_settings_bmr_result);
        tdee = findViewById(R.id.tv_settings_tdee_result);
        calorieGoal = findViewById(R.id.tv_settings_caloriegoal_result);

        if (startWeight.length() == 0) {
            startWeight.setError("Please enter a valid starting weight!");
            validData = false;
        }
        if (goalWeight.length() == 0) {
            goalWeight.setError("Please enter a valid goal weight!");
            validData = false;
        }
        if (goalDate.length() == 0) {
            goalDate.setError("Please enter a valid goal date!");
            validData = false;

        }
//        if (Integer.parseInt(goalWeight.getText().toString())
//                >= Integer.parseInt(startWeight.getText().toString()))
//        {
//            goalWeight.setError("Goal weight cannot be greater than or equal to start weight!");
//            validData = false;
//            }


        if (validData) {
            ApplicationSettings.setSetting(SettingsActivity.this,
                    "startWeight", startWeight.getText().toString());

            ApplicationSettings.setSetting(SettingsActivity.this,
                    "goalWeight", goalWeight.getText().toString());

            ApplicationSettings.setSetting(SettingsActivity.this,
                    "goalDate", goalDate.getText().toString());

            ApplicationSettings.setSetting(SettingsActivity.this,
                    // Remove string from bmi, would have been better to use
                    // separate textViews with unique ids.
                    "bmi", bmi.getText().toString().replaceAll("[^.0123456789]", ""));

            ApplicationSettings.setSetting(SettingsActivity.this,
                    "bmr", bmr.getText().toString());

            ApplicationSettings.setSetting(SettingsActivity.this,
                    "tdee", tdee.getText().toString());

            ApplicationSettings.setSetting(SettingsActivity.this,
                    "calorieGoal", calorieGoal.getText().toString());

            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please complete all fields!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * If the sharedPreferences settings exist, update all TextViews with the relevant data.
     */
    public void getUserData() {
        TextView bmi, bmr, tdee, calorieGoal;
        EditText startWeight, goalWeight, goalDate;

        bmi = findViewById(R.id.tv_settings_bmi_result);
        bmr = findViewById(R.id.tv_settings_bmr_result);
        tdee = findViewById(R.id.tv_settings_tdee_result);
        calorieGoal = findViewById(R.id.tv_settings_caloriegoal_result);

        startWeight = findViewById(R.id.et_settings_start_weight);
        goalWeight = findViewById(R.id.et_settings_goal_weight);
        goalDate = findViewById(R.id.et_settings_goal_date);

        if (settingsExist(this)) {
            bmi.setText(ApplicationSettings.getSetting(SettingsActivity.this, "bmi", ""));
            bmr.setText(ApplicationSettings.getSetting(SettingsActivity.this, "bmr", ""));
            tdee.setText(ApplicationSettings.getSetting(SettingsActivity.this, "tdee", ""));
            calorieGoal.setText(ApplicationSettings.getSetting(SettingsActivity.this, "calorieGoal", ""));
            startWeight.setText(ApplicationSettings.getSetting(SettingsActivity.this, "startWeight", ""));
            goalWeight.setText(ApplicationSettings.getSetting(SettingsActivity.this, "goalWeight", ""));
            goalDate.setText(ApplicationSettings.getSetting(SettingsActivity.this, "goalDate", ""));

            updateBmiColor();
        }
    }
}