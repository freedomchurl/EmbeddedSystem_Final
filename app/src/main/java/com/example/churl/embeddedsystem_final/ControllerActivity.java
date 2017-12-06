package com.example.churl.embeddedsystem_final;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ResourceBundle;

/**
 * Created by churl on 2017-12-03.
 */

public class ControllerActivity extends Activity implements View.OnClickListener{

    private android.support.design.widget.FloatingActionButton addControllee = null;


    private TextView controlleeIP_view = null;

    private EditText et_textlcd = null;
    private EditText et_7seg = null;
    private EditText et_dot = null;

    private EditText et_full1 = null;
    private EditText et_full2 = null;
    private EditText et_full3 = null;
    private EditText et_full4 = null;

    private Button[] ledButton = new Button[8];
    // for 문으로 button을 가져오고 하나의 setOnClick을 줘야한다.

    private Button bt_textlcd = null;
    private Button bt_7seg = null;
    private Button bt_dot = null;
    private Button bt_full= null;

    private boolean[] isRed = new boolean[8];

    private String tmpAddIP = null;
    private int tmpAddPort = 7777;
    private String tmpAddPwd = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_activity);

        addControllee = (android.support.design.widget.FloatingActionButton) findViewById(R.id.add_controllee);

        addControllee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddControlleeDialog dialog = new AddControlleeDialog(ControllerActivity.this, new AddControlleeDialog.CustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(String ip, String pwd, int port) {
                        // 여기서 동작해야한다.
                        tmpAddIP = ip;
                        tmpAddPort = port;
                        tmpAddPwd = pwd;

                        // 그리고 여기서 RecyclerView에 추가하고, 서버와 통신을 열어야한다.
                        Log.d("Input Data","IP = " + ip + ", Port = " + port + ", Pwd = " + pwd);
                    }
                });

                dialog.show();

                Log.d("Controllee 추가","Add Controllee");
            }
        });

        Init();

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.led1)
        {
            if(isRed[0]==false)
            {
                isRed[0] = true;
                ledButton[0].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[0] == true)
            {
                ledButton[0].setBackgroundResource(R.drawable.button_really_round);
                isRed[0] = false;
            }
        }
        else if(view.getId() == R.id.led2)
        {
            if(isRed[1]==false)
            {
                isRed[1] = true;
                ledButton[1].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[1] == true)
            {
                ledButton[1].setBackgroundResource(R.drawable.button_really_round);
                isRed[1] = false;
            }
        }
        else if(view.getId() == R.id.led3)
        {
            if(isRed[2]==false)
            {
                isRed[2] = true;
                ledButton[2].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[2] == true)
            {
                ledButton[2].setBackgroundResource(R.drawable.button_really_round);
                isRed[2] = false;
            }
        }
        else if(view.getId() == R.id.led4)
        {
            if(isRed[3]==false)
            {
                isRed[3] = true;
                ledButton[3].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[3] == true)
            {
                ledButton[3].setBackgroundResource(R.drawable.button_really_round);
                isRed[3] = false;
            }
        }
        else if(view.getId() == R.id.led5)
        {
            if(isRed[4]==false)
            {
                isRed[4] = true;
                ledButton[4].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[4] == true)
            {
                ledButton[4].setBackgroundResource(R.drawable.button_really_round);
                isRed[4] = false;
            }
        }
        else if(view.getId() == R.id.led6)
        {
            if(isRed[5]==false)
            {
                isRed[5] = true;
                ledButton[5].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[5] == true)
            {
                ledButton[5].setBackgroundResource(R.drawable.button_really_round);
                isRed[5] = false;
            }
        }
        else if(view.getId() == R.id.led7)
        {
            if(isRed[6]==false)
            {
                isRed[6] = true;
                ledButton[6].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[6] == true)
            {
                ledButton[6].setBackgroundResource(R.drawable.button_really_round);
                isRed[6] = false;
            }
        }else if(view.getId() == R.id.led8)
        {
            if(isRed[7]==false)
            {
                isRed[7] = true;
                ledButton[7].setBackgroundResource(R.drawable.button_really_round_red);
            }
            else if(isRed[7] == true)
            {
                ledButton[7].setBackgroundResource(R.drawable.button_really_round);
                isRed[7] = false;
            }
        }

    }


    public void Init()
    {
        controlleeIP_view = null;
        controlleeIP_view = (TextView) findViewById(R.id.device_IP_controller);
        // controllerIP를 띄워주는 TextView

        et_textlcd = null;
        et_textlcd = (EditText) findViewById(R.id.inputText);

        et_7seg = null;
        et_7seg = (EditText) findViewById(R.id.inputSegment);

        et_dot = null;
        et_dot = (EditText) findViewById(R.id.inputDot);

        et_full1 = null;
        et_full1 = (EditText) findViewById(R.id.inputfull1);

        et_full2 = null;
        et_full2 = (EditText) findViewById(R.id.inputfull2);

        et_full3 = null;
        et_full3 = (EditText) findViewById(R.id.inputfull3);

        et_full4 = null;
        et_full4 = (EditText) findViewById(R.id.inputfull4);


        ledButton = new Button[8];
        // for 문으로 button을 가져오고 하나의 setOnClick을 줘야한다.

        ledButton[0] = (Button) findViewById(R.id.led1);
        ledButton[1] = (Button) findViewById(R.id.led2);
        ledButton[2] = (Button) findViewById(R.id.led3);
        ledButton[3] = (Button) findViewById(R.id.led4);
        ledButton[4] = (Button) findViewById(R.id.led5);
        ledButton[5] = (Button) findViewById(R.id.led6);
        ledButton[6] = (Button) findViewById(R.id.led7);
        ledButton[7] = (Button) findViewById(R.id.led8);
        ///

        for(int i=0;i<8;i++)
        {
            ledButton[i].setOnClickListener(this);
            // 이 클래스 자체를 넣어버려서 하나로 통합했다.
        }

        ///

        bt_textlcd = null;
        bt_textlcd = (Button) findViewById(R.id.adjustText);
        bt_textlcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bt_7seg = null;
        bt_7seg = (Button) findViewById(R.id.adjustSegment);
        bt_7seg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 7segment 이벤트
            }
        });

        bt_dot = null;
        bt_dot = (Button) findViewById(R.id.adjustDot);
        bt_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dotmatrix 클릭되었음
            }
        });

        bt_full= null;
        bt_full = (Button) findViewById(R.id.adjustFull);
        bt_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // full 적용버튼
            }
        });
    }

}
