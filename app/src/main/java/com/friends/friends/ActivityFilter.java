package com.friends.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;


/**
 * Created by dell on 3/13/2017.
 */

public class ActivityFilter extends Activity {

    RangeSeekBar seekBar;
    Button btnApplyFilter;
    EditText edtxtCountry,edtxtName;
    CheckBox chkMale,chkFemale, chkSingle,chkRelation;
    TextView txtMin, txtMax;
    String country;
    String gender,relation,agemin,agemax,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        seekBar = (RangeSeekBar) findViewById(R.id.rangeSeekbarAge);
        txtMax= (TextView) findViewById(R.id.txtMax);
        txtMin= (TextView) findViewById(R.id.txtMin);
        edtxtCountry = (EditText) findViewById(R.id.edtCountry);
        edtxtName = (EditText) findViewById(R.id.edtName);
        chkMale= (CheckBox) findViewById(R.id.checkBoxMale);
        chkFemale= (CheckBox) findViewById(R.id.checkBoxFemale);
        chkSingle= (CheckBox) findViewById(R.id.checkBoxSingle);
        chkRelation= (CheckBox) findViewById(R.id.checkBoxRelation);
        btnApplyFilter = (Button) findViewById(R.id.btnApplyFIlters);

        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                country="";
                country = edtxtCountry.getText().toString().trim();
            //    name = edtxtName.getText().toString().trim();
                agemin= seekBar.getSelectedMinValue().toString();
                agemax= seekBar.getSelectedMaxValue().toString();
                gender="";
                relation="";

                if(chkSingle.isChecked()){
                    relation="Single";
                }
                if(chkRelation.isChecked()){
                    relation="In a relationship";
                }
                if(chkMale.isChecked()){
                    gender="Male";
                }
                if(chkFemale.isChecked()){
                    gender="Female";
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("country",country);
                returnIntent.putExtra("relation",relation);
                returnIntent.putExtra("agemax",agemax);
                returnIntent.putExtra("agemin",agemin);
                returnIntent.putExtra("gender",gender);
             //   returnIntent.putExtra("name",name);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        chkMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chkFemale.setChecked(false);
                //    chkMale.setChecked(true);
                }
            }
        });

        chkFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    chkMale.setChecked(false);
                //    chkFemale.setChecked(true);
                }
            }
        });
        chkSingle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    chkRelation.setChecked(false);
                    //   chkSingle.setChecked(true);
                }
            }
        });
        chkRelation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    chkSingle.setChecked(false);
                }
            //    chkRelation.setChecked(true);
            }
        });
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {

                txtMax.setText(""+maxValue.toString());
                txtMin.setText("Age "+minValue.toString()+"-");

            }
        });
    }
}
