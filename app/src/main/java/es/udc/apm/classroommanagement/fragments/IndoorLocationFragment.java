package es.udc.apm.classroommanagement.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.udc.apm.classroommanagement.MainActivity;
import es.udc.apm.classroommanagement.R;

public class IndoorLocationFragment extends Fragment {

    public IndoorLocationFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).showLateralMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_indoor_location, container, false);
    }
}
