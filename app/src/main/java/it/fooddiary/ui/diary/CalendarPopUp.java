package it.fooddiary.ui.diary;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import it.fooddiary.R;

public class CalendarPopUp {

    private static final String TAG = "CalendarPopUp";

    public CalendarPopUp(View view) {

    }

    /*
     * PopupWindow display method
     */
    public void showPopupWindow(final View view) {
        //Create a View object yourself through inflater
        view.getContext();
        LayoutInflater inflater = (LayoutInflater) view.getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_calendar, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Create a window with our parameters
       PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button closeButton = popupView.findViewById(R.id.close_calendar_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close the window when clicked
                DatePicker calendar = popupView.findViewById(R.id.date_picker);
                Log.d(TAG, String.valueOf(calendar.getDayOfMonth()));
                popupWindow.dismiss();
            }
        });

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
