package com.tophawks.vm.visualmerchandising.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tophawks.vm.visualmerchandising.Modules.Attendance.EnrollActivity;
import com.tophawks.vm.visualmerchandising.Modules.Attendance.LocationDetectionActivity;
import com.tophawks.vm.visualmerchandising.Modules.Attendance.RecognizeActivity;
import com.tophawks.vm.visualmerchandising.R;
import com.tophawks.vm.visualmerchandising.model.AttendanceResponse;

import org.joda.time.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FieldEmpFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference dRef;
    private StorageReference sRef;

    private Boolean locationVerified = false;
    private Boolean faceVerified = false;
    private Boolean isDoneForToday = false;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Button location;
    private Button faceRecognition;
    private ImageView locDoneImageView;
    private ImageView faceDoneImageView;
    private ImageView doneImageView;

    public FieldEmpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.fragment_field_emp, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dRef = FirebaseDatabase.getInstance().getReference().child("google");
        sRef = FirebaseStorage.getInstance().getReference();

        location = (Button) vi.findViewById(R.id.button4);
        faceRecognition = (Button) vi.findViewById(R.id.button5);
        locDoneImageView = (ImageView) vi.findViewById(R.id.field_loc_done_img);
        faceDoneImageView = (ImageView) vi.findViewById(R.id.field_face_done_img);
        doneImageView = (ImageView) vi.findViewById(R.id.field_status);

        preferences = getActivity().getSharedPreferences("AttendancePref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        if(preferences.getBoolean("isLocDone", false) == false){
            locDoneImageView.setImageResource(R.drawable.ic_sub_not_done);
        }else{
            locDoneImageView.setImageResource(R.drawable.ic_sub_done);
        }

        if(preferences.getBoolean("isFaceDone", false) == false){
            faceDoneImageView.setImageResource(R.drawable.ic_sub_not_done);
        }else{
            faceDoneImageView.setImageResource(R.drawable.ic_sub_done);
        }

        if(preferences.getBoolean("isDoneForToday", false) == false){
            doneImageView.setImageResource(R.drawable.not_done);
        }else{
            doneImageView.setImageResource(R.drawable.attendance_done);
        }

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getBoolean("isDoneForToday", false) == false){
                    Intent intent = new Intent(getActivity(), LocationDetectionActivity.class);
                    startActivityForResult(intent, 160);
                } else{
                    Toast.makeText(getActivity(), "Done for Today", Toast.LENGTH_SHORT).show();
                }
            }
        });

        faceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getBoolean("isDoneForToday", false) == false && preferences.getBoolean("isLoggedIn", false) == true){
                    Intent intent = new Intent(getActivity(), RecognizeActivity.class);
                    startActivityForResult(intent, 260);
                } else if(preferences.getBoolean("isLoggedIn", false) == false){
                    Toast.makeText(getActivity(), "Please Enroll", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getActivity(), "Done for Today", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return vi;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 160){
            if(preferences.getBoolean("isLocDone", false) == true){
                locDoneImageView.setImageResource(R.drawable.ic_sub_done);
            }
        }
        if(requestCode == 260){
            if(preferences.getBoolean("isFaceDone", false) == true){
                faceDoneImageView.setImageResource(R.drawable.ic_sub_done);
            }
        }
        if(preferences.getBoolean("isLocDone", false) == true && preferences.getBoolean("isFaceDone", false) == true){
            editor.putBoolean("isDoneForToday", true);
            editor.commit();
            doneImageView.setImageResource(R.drawable.attendance_done);
            isDoneForToday = true;
            pushAttendanceInDatabase();
            Toast.makeText(getActivity(), "Attendance taken for today", Toast.LENGTH_SHORT).show();
        }
    }

    private void pushAttendanceInDatabase(){
        DatabaseReference attendanceRef = dRef.child("Attendance").child(user.getUid());    //Attendance of this particular user;
        String dateToday = LocalDate.now().getDayOfMonth() + "|" + LocalDate.now().getMonthOfYear() + "|" + LocalDate.now().getYear();
        AttendanceResponse response = new AttendanceResponse("FieldEmployee", "Present");
        attendanceRef.child(dateToday).setValue(response);
    }

}
