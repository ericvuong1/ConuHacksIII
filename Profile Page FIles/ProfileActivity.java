package com.ew.jc.canieatthis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    //1) Calcium
    String calcium = "Calcium: ";
    SeekBar scr_calcium;
    TextView qty_calcium;
    TextView txt_calcium;
    int ipt_calcium = 0;
    int max_calcium = 1000;

    //2) Fat
    String fat = "Fat: ";
    SeekBar scr_fat;
    TextView qty_fat;
    TextView txt_fat;
    int ipt_fat = 0;
    int max_fat = 100;

    //3) Protein
    String protein = "Protein: ";
    SeekBar scr_protein;
    TextView qty_protein;
    TextView txt_protein;
    int ipt_protein = 0;
    int max_protein = 500;

    //4) Sodium
    String sodium = "Sodium: ";
    SeekBar scr_sodium;
    TextView qty_sodium;
    TextView txt_sodium;
    int ipt_sodium = 0;
    int max_sodium = 2500;

    //5) Heart Disease
    TextView qty_hd;
    CheckBox cbx_hd;

    //6) Kidney Disease
    TextView qty_kd;
    CheckBox cbx_kd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //1) Calcium
        scr_calcium = (SeekBar) findViewById(R.id.scr_calcium);
        txt_calcium = (TextView) findViewById(R.id.txt_calcium);
        qty_calcium = (TextView) findViewById(R.id.qty_calcium);

        scr_calcium.setMax(max_calcium);
        txt_calcium.setText(calcium);
        qty_calcium.setText("" + ipt_calcium + " mg");

        scr_calcium.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ipt_calcium = progress;
                qty_calcium.setText("" + ipt_calcium + " mg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //2) Protein
        scr_fat = (SeekBar) findViewById(R.id.scr_fat);
        txt_fat = (TextView) findViewById(R.id.txt_fat);
        qty_fat = (TextView) findViewById(R.id.qty_fat);

        scr_fat.setMax(max_fat);
        txt_fat.setText(fat);
        qty_fat.setText("" + ipt_fat + " g");

        scr_fat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ipt_fat = progress;
                qty_fat.setText("" + ipt_fat + " g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //3) Protein
        scr_protein = (SeekBar) findViewById(R.id.scr_protein);
        txt_protein = (TextView) findViewById(R.id.txt_protein);
        qty_protein = (TextView) findViewById(R.id.qty_protein);

        scr_protein.setMax(max_protein);
        txt_protein.setText(protein);
        qty_protein.setText("" + ipt_protein + " g");

        scr_protein.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ipt_protein = progress;
                qty_protein.setText("" + ipt_protein + " g");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //4) Sodium
        scr_sodium = (SeekBar) findViewById(R.id.scr_sodium);
        txt_sodium = (TextView) findViewById(R.id.txt_sodium);
        qty_sodium = (TextView) findViewById(R.id.qty_sodium);

        scr_sodium.setMax(max_sodium);
        txt_sodium.setText(sodium);
        qty_sodium.setText("" + ipt_sodium + " mg");

        scr_sodium.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ipt_sodium = progress;
                qty_sodium.setText("" + ipt_sodium + " mg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //5) Heart Disease
        qty_hd = (TextView) findViewById(R.id.qty_hd);
        cbx_hd = (CheckBox) findViewById(R.id.cbx_hd);

        qty_hd.setText("No");

        cbx_hd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    qty_hd.setText("Yes");
                    scr_fat.setProgress(13);
                    scr_sodium.setProgress(2200);

                }
                else{
                    qty_hd.setText("No");
                    scr_fat.setProgress(0);
                    scr_sodium.setProgress(0);
                }
            }
        });

        //6) Kidney Disease
        qty_kd = (TextView) findViewById(R.id.qty_kd);
        cbx_kd = (CheckBox) findViewById(R.id.cbx_kd);

        qty_kd.setText("No");

        cbx_kd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    qty_kd.setText("Yes");
                    scr_protein.setProgress(280);
                }
                else{
                    qty_kd.setText("No");
                    scr_protein.setProgress(0);
                }
            }
        });
    }

}
