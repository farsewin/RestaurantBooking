package com.example.restaurantbookingapp;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {

    private TextView currentDateTextView, monthYearTextView, peopleCountTextView;
    private MaterialButton prevMonthButton, nextMonthButton, decreaseBtn, increaseBtn;
    private GridLayout calendarGrid;
    private ChipGroup timeSlotsContainer;
    private Button reserveButton;

    private Calendar calendar;
    private Calendar selectedDate = null;

    private TextView selectedDayView = null;
    private int peopleCount = 2;
    private String selectedTime = null;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        reserveButton = findViewById(R.id.reserveButton);

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
            if (selectedDate == null) {
                Toast.makeText(this, "Please select a day", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedTime == null) {
                Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String reservationDateStr = sdf.format(selectedDate.getTime());

            Map<String, Object> reservation = new HashMap<>();
            reservation.put("peopleCount", peopleCount);
            reservation.put("selectedTime", selectedTime);
            reservation.put("reservationDate", reservationDateStr);

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                reservation.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

            db.collection("reservations")
                    .add(reservation)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Reservation saved to Firebase!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    private void updateCalendar() {
        calendarGrid.removeAllViews();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearTextView.setText(monthFormat.format(calendar.getTime()));

        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < startDay + maxDays; i++) {
            TextView dayView = new TextView(this);
            dayView.setGravity(Gravity.CENTER);
            dayView.setPadding(0, 30, 0, 30);

            if (i >= startDay) {
                int day = i - startDay + 1;
                dayView.setText(String.valueOf(day));

                Calendar now = Calendar.getInstance();
                Calendar candidate = (Calendar) calendar.clone();
                candidate.set(Calendar.DAY_OF_MONTH, day);

                if (candidate.before(now)) {
                    dayView.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                } else {
                    dayView.setOnClickListener(v -> {
                        if (selectedDayView != null) {
                            selectedDayView.setBackgroundColor(0x00000000); // transparent
                            selectedDayView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
                        }
                        selectedDayView = (TextView) v;
                        selectedDayView.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
                        selectedDayView.setTextColor(ContextCompat.getColor(this, android.R.color.white));

                        selectedDate = (Calendar) calendar.clone();
                        selectedDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(selectedDayView.getText().toString()));

                        SimpleDateFormat selectedFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault());
                        currentDateTextView.setText(selectedFormat.format(selectedDate.getTime()));
                    });
                }
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)
            );
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;

            calendarGrid.addView(dayView, params);
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
