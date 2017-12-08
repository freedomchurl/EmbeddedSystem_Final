package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by caucse on 2017-11-29.
 */

public class ControlleeMainActivity extends Activity implements View.OnClickListener{

    private String deviceName = null;

    public ControlleeData myData = new ControlleeData();

    private int devicePort = 7777; // default value is 7777
    private int iconNum = 0;
    private String deviceIP = null;


    private boolean isReadMode = false;

    private TextView controllerIP = null;

    private EditText et_textlcd = null;
    private EditText et_7seg = null;
    private EditText et_dot = null;

    private Button[] ledButton = new Button[8];
    // for 문으로 button을 가져오고 하나의 setOnClick을 줘야한다.

    private Button bt_textlcd = null;
    private Button bt_7seg = null;
    private Button bt_dot = null;
    private LinearLayout bt_full= null;

    private Switch switch_mode = null;
    private TextView tv_deviceName = null;
    private TextView tv_deviceIP = null;

    private ImageButton bt_discon = null;

    private boolean[] isRed = new boolean[8];


    private TextView[] myFull = new TextView[4];


    private ControlleeThread ee_Thread = null;

    private int[][] fullData = new int[4][3];

    // Socket용 Thread가 하나 존재하고 null로 초기화 되어야한다. 항상 null을 유지

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controllee_main);

        Intent i = getIntent();
        Init();

        deviceName =  i.getStringExtra("DEVICE_NAME");
        deviceIP = i.getStringExtra("DEVICE_IP");
        devicePort = i.getIntExtra("DEVICE_PORT",7777);
        isReadMode = i.getBooleanExtra("IS_READMODE",false);
        iconNum = i.getIntExtra("ICON_NUM",0);

        Log.d("ICONNUM",iconNum + "");
        // 먼저 ReadMode를 셋팅
        switch_mode.setChecked(isReadMode);
        myData.isReadMode = isReadMode;

        // 그다음에 디바이스 이름을 셋팅
        tv_deviceName.setText(deviceName);
        tv_deviceIP.setText(deviceIP + "/" + devicePort);

        controllerIP.setText("No Connected!");
        et_7seg.setText("0");
        // 여기서 소켓생성을 통해서 ServerSocket을 열어야 한다.

        StartSock();
    }

    public boolean isReadMode()
    {
        return this.isReadMode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public int getIconNum() {
        return iconNum;
    }

    @Override
    public void onBackPressed() {
        // Sock 종료하고 뒤로 가야한다.
        this.DisconnectSock();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        // Activity 종료시, 뒤로 가면서 Disconnect 필요
        this.DisconnectSock();
        super.onDestroy();
    }

    public void notifyLED()
    {
        for(int i=0;i<8;i++)
        {
            if(isRed[i]==true)
                ledButton[i].setBackgroundResource(R.drawable.button_really_round_red);
            else
                ledButton[i].setBackgroundResource(R.drawable.button_really_round);
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.led1_ee)
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
        else if(view.getId() == R.id.led2_ee)
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
        else if(view.getId() == R.id.led3_ee)
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
        else if(view.getId() == R.id.led4_ee)
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
        else if(view.getId() == R.id.led5_ee)
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
        else if(view.getId() == R.id.led6_ee)
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
        else if(view.getId() == R.id.led7_ee)
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
        }else if(view.getId() == R.id.led8_ee)
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
       //
        SendAll();


    }

    public void Init()
    {

        myFull[0] = (TextView) findViewById(R.id.ct_led1);
        myFull[1] = (TextView) findViewById(R.id.ct_led2);
        myFull[2] = (TextView) findViewById(R.id.ct_led3);
        myFull[3] = (TextView) findViewById(R.id.ct_led4);

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<3;j++)
            {
                this.fullData[i][j] = 255;
            }
        }


        bt_discon = (ImageButton) findViewById(R.id.ee_disconnect);
        bt_discon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 가지고있는 소켓의 접속을 끊고, 새롭게 대기해야한다. ( 새롭게 대기해야하는 부분까지 명시 )
               ReStartSock();
            }
        });

        switch_mode = (Switch) findViewById(R.id.ee_mainMode);
        switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // 현재 모드로부터 변경되는 것을 알려야한다. ( 소켓을 통해서 Controller에게, 그리고 나도 가지고 있어야한다 )
                isReadMode = b;
                myData.isReadMode = b;
                Log.d("MODECHECK","" + isReadMode);
                SendAll();
            }
        });

        tv_deviceName = (TextView) findViewById(R.id.ee_device_Name);
        tv_deviceIP = (TextView) findViewById(R.id.ee_deviceIP);

        controllerIP = (TextView) findViewById(R.id.ee_controllerIP_view);
        // controllerIP를 띄워주는 TextView

        et_textlcd = (EditText) findViewById(R.id.inputText_ee);

        et_7seg = (EditText) findViewById(R.id.inputSegment_ee);

        et_dot = (EditText) findViewById(R.id.inputDot_ee);


        ledButton = new Button[8];
        // for 문으로 button을 가져오고 하나의 setOnClick을 줘야한다.

        ledButton[0] = (Button) findViewById(R.id.led1_ee);
        ledButton[1] = (Button) findViewById(R.id.led2_ee);
        ledButton[2] = (Button) findViewById(R.id.led3_ee);
        ledButton[3] = (Button) findViewById(R.id.led4_ee);
        ledButton[4] = (Button) findViewById(R.id.led5_ee);
        ledButton[5] = (Button) findViewById(R.id.led6_ee);
        ledButton[6] = (Button) findViewById(R.id.led7_ee);
        ledButton[7] = (Button) findViewById(R.id.led8_ee);
        ///

        for(int i=0;i<8;i++)
        {
            ledButton[i].setOnClickListener(this);
            // 이 클래스 자체를 넣어버려서 하나로 통합했다.
        }

        ///

        bt_textlcd = (Button) findViewById(R.id.adjustText_ee);
        bt_textlcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendAll();
            }
        });

        bt_7seg = (Button) findViewById(R.id.adjustSegment_ee);
        bt_7seg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 7segment 이벤트
                SendAll();

            }
        });

        bt_dot = (Button) findViewById(R.id.adjustDot_ee);
        bt_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dotmatrix 클릭되었음
                SendAll();

            }
        });

        bt_full = (LinearLayout) findViewById(R.id.adjustFull_ee);
        bt_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // full 적용버튼Full_Led_Clicked();
                FullColorLEDDialog dialog = new FullColorLEDDialog(ControlleeMainActivity.this, new FullColorLEDDialog.FullDialogEventListener() {
                    @Override
                    public void FullDialogSetEvent(int[][] input) {
                        fullData = input;

                        for(int i=0;i<4;i++)
                        {
                            myFull[i].setBackgroundColor(Color.rgb(fullData[i][0],fullData[i][1],fullData[i][2]));

                        }
                        SendAll();

                    }

                    @Override
                    public void SetDialogEvent(int[][] input) {

                    }
                });

                dialog.setValue(fullData);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });
    }


    // 전체를 한번에 보내기 위함
    public void SendAll()
    {
        if(ee_Thread!=null) {

            myData.setLed(isRed);
            myData.setReadMode(isReadMode);
            if(et_7seg.getText().toString().equals(""))
                myData.setSegData(0);
            else
                myData.setSegData(Integer.valueOf(et_7seg.getText().toString()));
            myData.setDotData(et_dot.getText().toString());
            myData.setTextData(et_textlcd.getText().toString());
            myData.setFull(this.fullData);

            ee_Thread.sendData();
        }
    }

    // 여기는, 유정이가 Receive한 데이터들을 나한테 보내주었을 때 내가 사용하기 위함이다
    //---------------------------------------------------------------------------------

    // 화면에서, Controller IP TextView를 설정하기 위함
    public void setControllerIP_Port(String IP, int Port)
    {
        this.controllerIP.setText("Controller IP : " + IP + "/" + Port);
    }

    public void UpdateUI()
    {
        String textData = myData.getTextData();
        String textDot = myData.getDotData();
        int SegData = myData.getSegData();
        int [][] fullData = myData.getFull();
        boolean [] ledData = myData.getLed();

        setTextLCDView(textData);
        setDot_EditText(textDot);
        set7SegView(SegData);
        setLedView(ledData);
        setFullLedView(fullData);
    }


    // TextLCD EditText에 값을 고정하기 위함이다.
    public void setTextLCDView(String input)
    {
        et_textlcd.setText(input); // TextLCD에 그 값을 넘겨받으면, 그대로 et_textlcd에 set한다
        SetTextDevice();
    }

    // DotMatrix에 관해서는, 둘 중 하나만 수정해도 서로 해결되도록 해야한다.
    // 즉, DotMatrix EditText에 값을 입력해도, Dot View에 반영이 되어야하고,
    // 반대로 Dot View를 건드리면, 깔끔하게 DotMatrix EditText를 날려버리자
    public void setDot_EditText(String input)
    {
        et_dot.setText(input);

        // 여기서는 View도 건드려야한다.
        SetDotDevice();
    }

    public void setDotView(boolean[][] input)
    {
        // 여기서는 Grid View를 건드린다.
        et_dot.setText("");

        // 설정

        // 그리고 디바이스
        SetDotDevice_Advanced();
    }

    public void set7SegView(int input)
    {
        et_7seg.setText(String.valueOf(input));
        Set7SegmentDevice();
    }

    public void setLedView(boolean [] input)
    {
        // 00000000 과 같은 형식

        //char [] inputArr = input.toCharArray();

        for(int i=0;i<8;i++)
        {
            isRed[i] = input[i];
        }

        notifyLED();
        SetLEDDevice();
    }

    public void setFullLedView(int [][] input)
    {
        // 들어온 인풋으로 값을 셋팅하고

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<3;j++)
            {
                fullData[i][j] = input[i][j];
            }

            myFull[i].setBackgroundColor(Color.rgb(fullData[i][0],fullData[i][1],fullData[i][2]));
        }


        SetFullLEDDevice();
    }



    // 여기부터 미구현 메소드들을 채워넣기 시작한다. - 유정 & 내가 채워넣어야 하는 부분이다
    //---------------------------------------------------------------------------------

    public TextView getControllerIP()
    {
        return controllerIP;
    }

    // Socket을 끊는다. Discon, onBackPressed, onDestroy와 같은 경우에서 사용해야한다.
    public void DisconnectSock()
    {
        // 1. Sock 종료
        // 2. 이 Activity내의 Thread 값 null로 변경
        // 3. 그리고 onBackPressed가 아닌 경우, 다시 생성해주어야한다. 메소드로 분리필요
        if(ee_Thread !=null)
        {
            ee_Thread.sendMessage("EXIT:");
            // 메시지를 보내면 -> Thread에서 다시 콜 한다.
            ee_Thread = null;
        }
        this.controllerIP.setText("No Connected!");
    }

    public void StopThread()
    {
        Log.d("StopThread 정상","정상인가");
        ee_Thread.stopThread();
    }

    public void ReStartSock()
    {
        if(ee_Thread !=null)
        {
            ee_Thread.sendMessage("REST:");
            // 메시지를 보내면 -> Thread에서 다시 콜 한다.
        }
        this.controllerIP.setText("No Connected!");
    }

    public void ReStartServerSock()
    {
        Log.d("재시작","재시작");
        ee_Thread.stopThread();
        ee_Thread.Restart();
    }


    public void DisconnectedLocal()
    {
        ee_Thread = null;
        this.controllerIP.setText("No Connected!");
    }

    // Socket을 생성하는 순간 ( onCreate에서와, 임의로 Discon을 할때 모두 이용 )
    public void StartSock()
    {
        // 1. SockThread를 생성함과 동시에 this Activity의 Thread 값에 대입해야한다.
        ee_Thread = new ControlleeThread(devicePort,this,myData);
        ee_Thread.start();
        // 자신의 정보를 Controller에게 보내야한다.
    }


    // 여기까지
    //-----------------------------------------------------------------------------------

    //---------------------------------------------------------------------------------
    // 여기부터는 순수 디바이스 부분, 내 스스로 짜야하는 부분이다.
    // 얘네들을 진짜 디바이스만 건드린다. 그 이외에 어떠한 작업도 안한다
    // 디바이스에 적용할 값은 UI 변수들로부터 가져오도록 한다.

    public void SetFullLEDDevice()
    {
        // 쓰레드가 필요하지 않음


    }
    public void SetTextDevice()
    {
        // 쓰레드가 필요하지 않음
    }

    public void Set7SegmentDevice()
    {
        // Thread 필요
    }

    public void SetLEDDevice()
    {
        // Thread 불필요
    }

    public void SetDotDevice()
    {
        // Thread 필요
    }

    public void SetDotDevice_Advanced()
    {
        // Thread 필요
        // SetDotDevice, SetDotDevice_Advanced는 같은 Thread에 접근해야하고 Thread 단에서, JNI 소스코드를 실행할 때, 어떤 옵션으로 실행할 것인지 옵션을 부여해야한다.
    }

    // 여기까지
    //-----------------------------------------------------------------------------------
}
