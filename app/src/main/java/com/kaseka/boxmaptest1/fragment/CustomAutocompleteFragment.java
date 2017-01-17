package com.kaseka.boxmaptest1.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.kaseka.boxmaptest1.R;


public class CustomAutocompleteFragment extends PlaceAutocompleteFragment {

    private EditText etInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View var4 = super.onCreateView(inflater, container, savedInstanceState);
        var4.setBackground(getResources().getDrawable(R.drawable.google_search_box_shape));


        etInput = (EditText)var4.findViewById(com.google.android.gms.R.id.place_autocomplete_search_input);
        etInput.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.colorMyLightGrey));
        etInput.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorMyLightGreen));
        return var4;
    }

    public void setHintText(String hintText) {

        etInput.setHint(hintText);
    }
}
