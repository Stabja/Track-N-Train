package com.tophawks.vm.visualmerchandising.SalesMgmt.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.tophawks.vm.visualmerchandising.R;

import org.joda.time.DateTime;


public class HomeFragment extends Fragment {

    private HorizontalPicker picker;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.sales_fragment_home, container, false);

        picker = (HorizontalPicker) vi.findViewById(R.id.datePicker);
        picker.setListener(new DatePickerListener() {
            @Override
            public void onDateSelected(DateTime dateSelected) {

            }
        });
        picker.init();

        return vi;
    }

}
