package com.example.eventify;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.eventify.models.Event;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eventify.models.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BlankScreenActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ListView eventsListView;
    private Button createEventButton;

    // Map of dates to events
    private Map<Long, List<Event>> events = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_blank_screen);

        // Get references to the CalendarView, ListView, and Create Event button
        calendarView = findViewById(R.id.calendarView);
        eventsListView = findViewById(R.id.eventsListView);
        createEventButton = findViewById(R.id.createEventButton);

        // Set up the CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                long dateMillis = getDateMillis(year, month, dayOfMonth);
                List<Event> eventsForDate = events.get(dateMillis);
                if (eventsForDate != null && !eventsForDate.isEmpty()) {
                    showEventsList(eventsForDate);
                } else {
                    showCreateEventDialog(dateMillis);
                }
            }
        });

        // Set up the ListView
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = (Event) adapterView.getItemAtPosition(i);
                showEventDetails(event);
            }
        });

        // Set up the Create Event button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateEventDialog(calendarView.getDate());
            }
        });

        // Add some sample events for testing
        long todayMillis = getDateMillis(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        events.put(todayMillis, Arrays.asList(new Event(todayMillis, "Sample event", "This is a sample event for testing purposes", null)));
    }

    // Helper method to convert a date to milliseconds since epoch
    private long getDateMillis(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    // Helper method to display a list of events in an AlertDialog
    private void showEventsList(List<Event> events) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Events");
        builder.setAdapter(new EventListAdapter(this, events), null);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    // Helper method to display the details of an event in an AlertDialog
    private void showEventDetails(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(event.getTitle());
        builder.setMessage(event.getDescription());
        builder.setPositiveButton("OK", null);
        builder.show();
    }

//    // Helper method to display a dialog for creating a new event
//    private void showCreateEventDialog(final long dateMillis) {
//        // Create a new AlertDialog.Builder object
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Create Event");
//
//        // Inflate the dialog layout from XML
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View dialogLayout = inflater.inflate(R.layout.dialog_add_event, null);
//        // Get references to the dialog's views
//        final EditText titleEditText = dialogLayout.findViewById(R.id.titleEditText);
//        final EditText descriptionEditText = dialogLayout.findViewById(R.id.descriptionEditText);
//        final Button selectTimeButton = dialogLayout.findViewById(R.id.selectTimeButton);
//
//        // Set up the Select Time button
//        selectTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Create a new TimePickerDialog object
//                TimePickerDialog timePickerDialog = new TimePickerDialog(BlankScreenActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
//                        // Set the selected time on the Select Time button
//                        selectTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
//                    }
//                }, 0, 0, false);
//
//                // Show the TimePickerDialog
//                timePickerDialog.show();
//            }
//        });
//
//        // Add the dialog layout to the AlertDialog
//        builder.setView(dialogLayout);
//
//        // Add a "Create" button to the AlertDialog
//        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // Get the values entered by the user
//                String title = titleEditText.getText().toString().trim();
//                String description = descriptionEditText.getText().toString().trim();
//                String time = selectTimeButton.getText().toString();
//
//                // Validate the input
//                if (title.isEmpty() || description.isEmpty() || time.isEmpty()) {
//                    Toast.makeText(BlankScreenActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Parse the selected time into hours and minutes
//                int hours = Integer.parseInt(time.substring(0, 2));
//                int minutes = Integer.parseInt(time.substring(3, 5));
//
//                // Create a new Event object and add it to the list of events
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(dateMillis);
//                calendar.set(Calendar.HOUR_OF_DAY, hours);
//                calendar.set(Calendar.MINUTE, minutes);
//                Event event = new Event(dateMillis, title, description, calendar.getTime());
//                List<Event> eventsForDate = events.get(dateMillis);
//                if (eventsForDate == null) {
//                    eventsForDate = new ArrayList<>();
//                    events.put(dateMillis, eventsForDate);
//                }
//                eventsForDate.add(event);
//
//                // Show a confirmation message
//                Toast.makeText(BlankScreenActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Add a "Cancel" button to the AlertDialog
//        builder.setNegativeButton("Cancel", null);
//
//        // Show the AlertDialog
//        builder.show();
//    }

    // Adapter class for displaying a list of events in a ListView
    private static class EventListAdapter extends ArrayAdapter<Event> {
        public EventListAdapter(Context context, List<Event> events) {
            super(context, 0, events);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            // Get references to the views in the list item layout
            TextView titleTextView = convertView.findViewById(android.R.id.text1);
            TextView descriptionTextView = convertView.findViewById(android.R.id.text2);

            // Get the Event object for the current position
            Event event = getItem(position);

            // Set the title and description in the views
            titleTextView.setText(event.getTitle());
            descriptionTextView.setText(String.format("%02d:%02d -%s", event.getStartDate().getHours(), event.getStartDate().getMinutes(), event.getDescription()));
            return convertView;
        }

    }

    // Method for updating the list of events for a given date
    private void updateEventsList(long dateMillis) {
// Get the list of events for the selected date
        List<Event> eventsForDate = events.get(dateMillis);
        // Get a reference to the ListView in the layout
        ListView eventsListView = findViewById(R.id.eventsListView);

        // Set the adapter for the ListView
        if (eventsForDate != null) {
            EventListAdapter adapter = new EventListAdapter(this, eventsForDate);
            eventsListView.setAdapter(adapter);
        } else {
            eventsListView.setAdapter(null);
        }
    }

    // Method for displaying the Create Event dialog
    private void showCreateEventDialog(long dateMillis) {
        // Create a new AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Event");
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.dialog_create_event, null);

        // Get references to the dialog's views
        final EditText titleEditText = dialogLayout.findViewById(R.id.titleEditText);
        final EditText descriptionEditText = dialogLayout.findViewById(R.id.descriptionEditText);
        final Button selectTimeButton = dialogLayout.findViewById(R.id.selectTimeButton);

        // Set up the Select Time button
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new TimePickerDialog object
                TimePickerDialog timePickerDialog = new TimePickerDialog(BlankScreenActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        // Set the selected time on the Select Time button
                        selectTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, 0, 0, false);

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        // Add the dialog layout to the AlertDialog
        builder.setView(dialogLayout);

        // Add a "Create" button to the AlertDialog
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get the values entered by the user
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String time = selectTimeButton.getText().toString();

                // Validate the input
                if (title.isEmpty() || description.isEmpty() || time.isEmpty()) {
                    Toast.makeText(BlankScreenActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Parse the selected time into hours and minutes
                int hours = Integer.parseInt(time.substring(0, 2));
                int minutes = Integer.parseInt(time.substring(3, 5));

                // Create a new Event object and add it to the list of events
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateMillis);
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                Event event = new Event(dateMillis, title, description, calendar.getTime());
                List<Event> eventsForDate = events.get(dateMillis);
                if (eventsForDate == null) {
                    eventsForDate = new ArrayList<>();
                    events.put(dateMillis, eventsForDate);
                }
                eventsForDate.add(event);

                // Show a confirmation message
                Toast.makeText(BlankScreenActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();

                // Update the events list for the selected date
                updateEventsList(dateMillis);
            }
        });

        // Add a "Cancel" button to the AlertDialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });

        // Show the AlertDialog
        builder.show();
    }
}


