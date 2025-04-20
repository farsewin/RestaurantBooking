package com.example.restaurantbookingapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.ChipGroup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {

    private TextView currentDateTextView, monthYearTextView, peopleCountTextView;
    private ImageButton prevMonthButton, nextMonthButton, decreaseBtn, increaseBtn;
    private GridLayout calendarGrid;
    private ChipGroup timeSlotsContainer;

    private Calendar calendar;
    private int peopleCount = 2;
    private String selectedTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        currentDateTextView = findViewById(R.id.currentDateTextView);
        monthYearTextView = findViewById(R.id.monthYearTextView);
        peopleCountTextView = findViewById(R.id.peopleCountTextView);
        prevMonthButton = findViewById(R.id.prevMonthButton);
        nextMonthButton = findViewById(R.id.nextMonthButton);
        decreaseBtn = findViewById(R.id.decreaseBtn);
        increaseBtn = findViewById(R.id.increaseBtn);
        calendarGrid = findViewById(R.id.calendarGrid);
        timeSlotsContainer = findViewById(R.id.timeSlotsContainer);
        Button reserveButton = findViewById(R.id.reserveButton);

        calendar = Calendar.getInstance();
        updateCalendar();

        prevMonthButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        nextMonthButton.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        decreaseBtn.setOnClickListener(v -> {
            if (peopleCount > 1) {
                peopleCount--;
                peopleCountTextView.setText(String.valueOf(peopleCount));
            }
        });

        increaseBtn.setOnClickListener(v -> {
            peopleCount++;
            peopleCountTextView.setText(String.valueOf(peopleCount));
        });

        populateTimeSlots();

        timeSlotsContainer.setOnCheckedChangeListener((group, checkedId) -> {
            Chip selectedChip = findViewById(checkedId);
            if (selectedChip != null) {
                selectedTime = selectedChip.getText().toString();
            }
        });

        reserveButton.setOnClickListener(v -> {
            if (selectedTime == null) {
                Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            } else {
                // Add Firebase or local reservation logic here
                Toast.makeText(this, "Reservation confirmed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCalendar() {
        calendarGrid.removeAllViews();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearTextView.setText(sdf.format(calendar.getTime()));

        sdf = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
        currentDateTextView.setText("Today, " + sdf.format(Calendar.getInstance().getTime()));

        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < startDay + maxDays; i++) {
            TextView dayView = new TextView(this);
            dayView.setGravity(Gravity.CENTER);
            dayView.setPadding(0, 20, 0, 20);

            if (i >= startDay) {
                int day = i - startDay + 1;
                dayView.setText(String.valueOf(day));
                dayView.setOnClickListener(v -> {
                    Toast.makeText(this, "Selected day: " + day, Toast.LENGTH_SHORT).show();
                });
            }

            calendarGrid.addView(dayView, new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ));
        }
    }

    private void populateTimeSlots() {
        String[] times = {"12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM"};
        for (String time : times) {
            Chip chip = new Chip(this);
            chip.setText(time);
            chip.setCheckable(true);
            timeSlotsContainer.addView(chip);
        }
    }
}
