package com.kaseka.boxmaptest1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;


public class CustomAutocompleteFragment extends PlaceAutocompleteFragment {

    private EditText etInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View var4 = super.onCreateView(inflater, container, savedInstanceState);
        var4.setBackground(getResources().getDrawable(R.drawable.search_box_shape));

        etInput = (EditText)var4.findViewById(com.google.android.gms.R.id.place_autocomplete_search_input);
        etInput.setHintTextColor(getResources().getColor(R.color.colorMyGrey));
        etInput.setTextColor(getResources().getColor(R.color.colorMyYellow));

        return var4;
    }

    public void setHintText(String hintText) {

        etInput.setHint(hintText);
    }
}