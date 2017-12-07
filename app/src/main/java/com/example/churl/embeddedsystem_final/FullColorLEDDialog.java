package com.example.churl.embeddedsystem_final;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by churl on 2017-12-07.
 */

public class FullColorLEDDialog extends Dialog implements View.OnClickListener{

    private Context context;

    private Button CancelButton = null;
    private Button OkButton = null;
    private Button PreviewButton = null;

    private TextView[] ledView = new TextView[4];

    private EditText inputRed, inputGreen, inputBlue;

    private int[][] fullData;

    private int ledNum = 0;

    private FullDialogEventListener onCustomDialogEventListener;

    public FullColorLEDDialog(@NonNull Context context,FullDialogEventListener onCustomDialogEventListener)
    {
        super(context);
        this.context = context;
        this.onCustomDialogEventListener = onCustomDialogEventListener;
    }


    public interface FullDialogEventListener{
        public void FullDialogSetEvent(int[][] input);
        public void SetDialogEvent(int[][] input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fullcolor);


        PreviewButton = (Button)findViewById(R.id.modify_preview_button_controllee);
        CancelButton = (Button) findViewById(R.id.modify_cancel_button_controllee);
        OkButton = (Button) findViewById(R.id.modify_ok_button_controllee);

        ledView[0] = (TextView) findViewById(R.id.controllee_led1);
        ledView[1] = (TextView) findViewById(R.id.controllee_led2);
        ledView[2] = (TextView) findViewById(R.id.controllee_led3);
        ledView[3] = (TextView) findViewById(R.id.controllee_led4);

        inputRed = (EditText) findViewById(R.id.controllee_dialog_red);
        inputGreen = (EditText) findViewById(R.id.controllee_dialog_green);
        inputBlue = (EditText) findViewById(R.id.controllee_dialog_blue);

        for(int i=0;i<4;i++)
        {
            ledView[i].setOnClickListener(this);
            ledView[i].setBackgroundColor(Color.rgb(fullData[i][0],fullData[i][1],fullData[i][2]));
        }

        inputRed.setText(String.valueOf(fullData[ledNum][0]));
        inputGreen.setText(String.valueOf(fullData[ledNum][1]));
        inputBlue.setText(String.valueOf(fullData[ledNum][2]));



        PreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckUnderRange();
                fullData[ledNum][0] = Integer.valueOf(inputRed.getText().toString());
                fullData[ledNum][1] = Integer.valueOf(inputGreen.getText().toString());
                fullData[ledNum][2] = Integer.valueOf(inputBlue.getText().toString());

                // 아이콘 바꾸어 줘야한다.

                TextView thisFull = ledView[ledNum];

                CheckRange();

                thisFull.setBackgroundColor(Color.rgb(fullData[ledNum][0],fullData[ledNum][1],fullData[ledNum][2]));
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckUnderRange();
                fullData[ledNum][0] = Integer.valueOf(inputRed.getText().toString());
                fullData[ledNum][1] = Integer.valueOf(inputGreen.getText().toString());
                fullData[ledNum][2] = Integer.valueOf(inputBlue.getText().toString());

                // 아이콘 바꾸어 줘야한다.

                TextView thisFull = ledView[ledNum];

                CheckRange();
                thisFull.setBackgroundColor(Color.rgb(fullData[ledNum][0],fullData[ledNum][1],fullData[ledNum][2]));

                onCustomDialogEventListener.FullDialogSetEvent(fullData);
                cancel();
            }
        });

    }

    public void CheckUnderRange()
    {
        if(inputRed.getText().toString().equals(""))
            inputRed.setText("0");
        if(inputGreen.getText().toString().equals(""))
            inputGreen.setText("0");
        if(inputBlue.getText().toString().equals(""))
            inputBlue.setText("0");
    }


    public void CheckRange()
    {
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<3;j++)
            {
                if(fullData[i][j] >= 255)
                    fullData[i][j] = 255;
                if(fullData[i][j] <=0)
                    fullData[i][j] = 0;
            }
        }

        inputRed.setText(String.valueOf(fullData[ledNum][0]));
        inputGreen.setText(String.valueOf(fullData[ledNum][1]));
        inputBlue.setText(String.valueOf(fullData[ledNum][2]));
    }

    @Override
    public void onClick(View view) {


        CheckUnderRange();
        fullData[ledNum][0] = Integer.valueOf(inputRed.getText().toString());
        fullData[ledNum][1] = Integer.valueOf(inputGreen.getText().toString());
        fullData[ledNum][2] = Integer.valueOf(inputBlue.getText().toString());

        // 아이콘 바꾸어 줘야한다.

        TextView thisFull = ledView[ledNum];

        CheckRange();

        thisFull.setBackgroundColor(Color.rgb(fullData[ledNum][0],fullData[ledNum][1],fullData[ledNum][2]));


        if(view.getId()==R.id.controllee_led1)
        {
            this.ledNum = 0;
        }
        else if(view.getId()==R.id.controllee_led2)
        {
            this.ledNum = 1;
        }
        else if(view.getId() == R.id.controllee_led3)
        {
            this.ledNum = 2;
        }
        else if(view.getId() == R.id.controllee_led4)
        {
            this.ledNum = 3;
        }
        int thisnum = this.ledNum;
        // 대입하고나서
        inputRed.setText(String.valueOf(fullData[thisnum][0]));
        inputGreen.setText(String.valueOf(fullData[thisnum][1]));
        inputBlue.setText(String.valueOf(fullData[thisnum][2]));

    }

    public void setValue(int[][] input)
    {
        this.fullData = input;
    }

}
