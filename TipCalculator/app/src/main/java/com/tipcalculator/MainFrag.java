package com.tipcalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MainFrag extends Fragment{
    Button button;
    TextView txt;
    TextInputEditText input, tip;
    Spinner dropdown;
    int dropDownPosition = 0;
    public MainFrag(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        button = rootView.findViewById(R.id.button);
        txt = rootView.findViewById(R.id.txt);
        input = rootView.findViewById(R.id.input);
        dropdown = rootView.findViewById(R.id.spinner);
        String[] items = new String[] {"No Rounding", "Round Total Bill", "Round Tip"};
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        Selection.setSelection(input.getText(), input.getText().length());
        tip = rootView.findViewById(R.id.tip);
        input.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s){
                if(!s.toString().startsWith("$")){
                    input.setText("$");
                    Selection.setSelection(input.getText(), input.getText().length());

                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dropDownPosition = position;
                Log.v(Integer.toString(position), "drop down pos");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String temp = input.getText().toString().substring(1);
                String tempTip = tip.getText().toString();
                try{
                    double result = Double.parseDouble(temp);
                    double total = 0;
                    try {
                        double tipres = Double.parseDouble(tempTip);
                        Double convertedTip = (tipres / 100) * result;
                        if(dropDownPosition == 1 && tipres != 0){
                            total = result + convertedTip;
                            BigDecimal bDtotal = new BigDecimal(Double.toString(total));
                            bDtotal = bDtotal.setScale(0, RoundingMode.HALF_UP);
                            total = bDtotal.doubleValue();
                            convertedTip = (int)total - result;
                            BigDecimal convTipBD = new BigDecimal(Double.toString(convertedTip));
                            convTipBD = convTipBD.setScale(2, RoundingMode.HALF_UP);
                            convertedTip = convTipBD.doubleValue();
                        }
                        else if (dropDownPosition == 2){
                            BigDecimal bDtip = new BigDecimal(Double.toString(convertedTip));
                            bDtip = bDtip.setScale(0, RoundingMode.HALF_UP);
                            convertedTip = bDtip.doubleValue();
                            total = bDtip.doubleValue() + result;

                        }
                        else if (dropDownPosition == 0){
                            BigDecimal bDtip = new BigDecimal(Double.toString(convertedTip));
                            bDtip = bDtip.setScale(2, RoundingMode.HALF_UP);
                            convertedTip = bDtip.doubleValue();
                            total = result + convertedTip;
                            BigDecimal bDtotal = new BigDecimal(Double.toString(total));
                            bDtotal = bDtotal.setScale(2, RoundingMode.HALF_UP);
                            total = bDtotal.doubleValue();

                        }
                        else{
                            total = result;
                            convertedTip = (double)0;
                        }
                        txt.setText("The total bill is $" + total + " with a tip of $" + convertedTip);
                    }
                        catch(NumberFormatException | NullPointerException nfe) {
                            txt.setText("Enter a number!");
                        }
                } catch(NumberFormatException | NullPointerException nfe){
                    txt.setText("Enter a number!");

                }


            }
        });
        return rootView;
    }
}

