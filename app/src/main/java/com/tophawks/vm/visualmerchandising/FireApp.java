package com.tophawks.vm.visualmerchandising;

import android.*;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.LocalDate;

 /**
      Reset the SharedPreference "isDoneForToday" and "DateToday" in this class.
      CODE WORKING PERFECTLY AND FLAWLESSLY
 **/
public class FireApp extends Application {

    private String currentDate;
    private String storedDate;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        preference = getSharedPreferences("AttendancePref", Context.MODE_PRIVATE);
        editor = preference.edit();
        storedDate = preference.getString("DateToday", "0/0/0");
        currentDate = LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthOfYear() + "/" + LocalDate.now().getYear();

        if(currentDate.equals(storedDate) == false){        //If its a new day || If user opens the app first time in that day
            editor.putBoolean("isLocDone", false);          //Reset the Location status
            editor.putBoolean("isFaceDone", false);         //Reset the FaceRecognition status
            editor.putBoolean("isDoneForToday", false);     //Reset the Attendance status
            editor.putString("DateToday", currentDate);     //Store today's date in SharedPreference
            editor.commit();
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
        }
    }
}
