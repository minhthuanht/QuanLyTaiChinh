package com.minhthuanht.quanlytaichinh.overviewtransaction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.minhthuanht.quanlytaichinh.R;


public class FragmentTendency extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private Spinner mSpinnerItems;

    private Spinner mSpinnerSelect;

//    private Spinner mTimeStart;
//
//    private Spinner mTimeEnd;

    public FragmentTendency() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tendency, container, false);
        mSpinnerItems = view.findViewById(R.id.spinnerItems);
        mSpinnerSelect = view.findViewById(R.id.spinnerSelect);
//        mTimeStart = view.findViewById(R.id.spinnerTimeStart);
//        mTimeEnd = view.findViewById(R.id.spinnerTimeEnd);

        ArrayAdapter<CharSequence> adapterItems = ArrayAdapter.createFromResource(getContext(), R.array.spinnerItems, android.R.layout.simple_spinner_item);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerItems.setAdapter(adapterItems);

        ArrayAdapter<CharSequence> adapterSelect = ArrayAdapter.createFromResource(getContext(), R.array.spinnerSelect, android.R.layout.simple_spinner_item);
        adapterSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSelect.setAdapter(adapterSelect);


        return view;
    }










}
