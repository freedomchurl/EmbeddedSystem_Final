package com.example.churl.embeddedsystem_final;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.RecursiveAction;

/**
 * Created by churl on 2017-12-03.
 */

public class ControllerActivity extends Activity implements View.OnClickListener{

    private android.support.design.widget.FloatingActionButton addControllee = null;


    private ArrayList<ControlleeData> myData = new ArrayList<ControlleeData>();

    private RecyclerView controlleeView = null;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView controlleeIP_view = null;

    private EditText et_textlcd = null;
    private EditText et_7seg = null;
    private EditText et_dot = null;

    private Button[] ledButton = new Button[8];
    // for 문으로 button을 가져오고 하나의 setOnClick을 줘야한다.

    private Button bt_textlcd = null;
    private Button bt_7seg = null;
    private Button bt_dot = null;
    private LinearLayout bt_full= null;

    private boolean[] isRed = new boolean[8];

    private String tmpAddIP = null;
    private int tmpAddPort = 7777;

    private int[][] fullData = new int[4][3];
    private TextView[] myFull = new TextView[4];

    ItemTouchHelper itemTouchHelper = null;

    ArrayList<ControllerThread> mcontrollerThread = new ArrayList<ControllerThread>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_activity);

        controlleeView = (RecyclerView) findViewById(R.id.controlleeList);
        layoutManager = new LinearLayoutManager(this);

        controlleeView.setLayoutManager(layoutManager);
        adapter = new MyAdpater(myData,getApplicationContext());

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(controlleeView);

        controlleeView.setAdapter(adapter);

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

                        // 그리고 여기서 RecyclerView에 추가하고, 서버와 통신을 열어야한다.
                        //Log.d("Input Data","IP = " + ip + ", Port = " + port + ", Pwd = " + pwd);


                        mcontrollerThread.add((new ControllerThread(tmpAddIP,tmpAddPort,ControllerActivity.this,myData)));

                        Log.d("Client","현재 Thread개수 = " + mcontrollerThread.size());

                        mcontrollerThread.get(mcontrollerThread.size()-1).start();

                        // 이 부분의 수정이 필요하다
                    }
                });

                dialog.show();

                Log.d("Controllee 추가","Add Controllee");
            }
        });

        Init();

    }

    public RecyclerView.Adapter getAdapter()
    {
        return this.adapter;
    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            Log.d("Adapter","Adapter");

            mcontrollerThread.get(position).sendMessage("EXIT:");
            mcontrollerThread.remove(position);
            myData.remove(position);
            controlleeView.getAdapter().notifyDataSetChanged();

        }
    };



    class MyAdpater extends RecyclerView.Adapter {
        private Context context;
        private ArrayList<ControlleeData> mItems;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public MyAdpater(ArrayList<ControlleeData> items, Context mContext) {
            mItems = items;
            context = mContext;
        }

        @Override
        public MyAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
            MyAdpater.ViewHolder holder = new MyAdpater.ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyAdpater.ViewHolder)holder).imageView.setImageResource(mItems.get(position).getIcon());
            ((MyAdpater.ViewHolder)holder).nameView.setText(mItems.get(position).getDeviceName());

            setAnimation(((MyAdpater.ViewHolder)holder).imageView, position);

        }



        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView nameView;


            public ViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imageControllee);
                nameView = (TextView) view.findViewById(R.id.nameControllee);


                view.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v)
                    {
                        // 그 채팅방으로 들어가는 기능이 필요하다.
                        // 셋팅
                    }
                });
                // 이 부분에서, 드래그 삭제 기능을 넣어야한다.
            }

        }

        private void setAnimation(View viewToAnimate, int position) {
            // 새로 보여지는 뷰라면 애니메이션을 해줍니다
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }


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

        if(view.getId() == R.id.led1_er)
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
        else if(view.getId() == R.id.led2_er)
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
        else if(view.getId() == R.id.led3_er)
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
        else if(view.getId() == R.id.led4_er)
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
        else if(view.getId() == R.id.led5_er)
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
        else if(view.getId() == R.id.led6_er)
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
        else if(view.getId() == R.id.led7_er)
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
        }else if(view.getId() == R.id.led8_er)
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

        myFull[0] = (TextView) findViewById(R.id.cr_led1);
        myFull[1] = (TextView) findViewById(R.id.cr_led2);
        myFull[2] = (TextView) findViewById(R.id.cr_led3);
        myFull[3] = (TextView) findViewById(R.id.cr_led4);

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<3;j++)
            {
                this.fullData[i][j] = 255;
            }
        }


        controlleeIP_view = (TextView) findViewById(R.id.device_IP_controller_er);
        // controllerIP를 띄워주는 TextView

        et_textlcd = (EditText) findViewById(R.id.inputText_er);

        et_7seg = (EditText) findViewById(R.id.inputSegment_er);

        et_dot = (EditText) findViewById(R.id.inputDot_er);


        ledButton = new Button[8];
        // for 문으로 button을 가져오고 하나의 setOnClick을 줘야한다.

        ledButton[0] = (Button) findViewById(R.id.led1_er);
        ledButton[1] = (Button) findViewById(R.id.led2_er);
        ledButton[2] = (Button) findViewById(R.id.led3_er);
        ledButton[3] = (Button) findViewById(R.id.led4_er);
        ledButton[4] = (Button) findViewById(R.id.led5_er);
        ledButton[5] = (Button) findViewById(R.id.led6_er);
        ledButton[6] = (Button) findViewById(R.id.led7_er);
        ledButton[7] = (Button) findViewById(R.id.led8_er);
        ///

        for(int i=0;i<8;i++)
        {
            ledButton[i].setOnClickListener(this);
            // 이 클래스 자체를 넣어버려서 하나로 통합했다.
        }

        ///

        bt_textlcd = (Button) findViewById(R.id.adjustText_er);
        bt_textlcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Text_LCD_Clicked();
            }
        });

        bt_7seg = (Button) findViewById(R.id.adjustSegment_er);
        bt_7seg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 7segment 이벤트
                Seg7_Clicked();
            }
        });

        bt_dot = (Button) findViewById(R.id.adjustDot_er);
        bt_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dotmatrix 클릭되었음
                Dot_Send_Clicked();
            }
        });

        bt_full = (LinearLayout) findViewById(R.id.adjustFull_er);
        bt_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // full 적용버튼Full_Led_Clicked();
                FullColorLEDDialog dialog = new FullColorLEDDialog(ControllerActivity.this, new FullColorLEDDialog.FullDialogEventListener() {
                    @Override
                    public void FullDialogSetEvent(int[][] input) {
                        fullData = input;

                        for(int i=0;i<4;i++)
                        {
                            myFull[i].setBackgroundColor(Color.rgb(fullData[i][0],fullData[i][1],fullData[i][2]));
                        }

                        Full_Led_Clicked();
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

    }

    // 여기는, 유정이가 Receive한 데이터들을 나한테 보내주었을 때 내가 사용하기 위함이다
    //---------------------------------------------------------------------------------

    // 화면에서, Controller IP TextView를 설정하기 위함
    public void setControlleeIP_Port(String IP, int Port)
    {
        this.controlleeIP_view.setText("Controller IP : " + IP + "/" + Port);
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
        setDot_fromText(input);
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

    public void set7SegView(String input)
    {
        et_7seg.setText(input);
        Set7SegmentDevice();
    }

    public void setLedView(String input)
    {
        // 00000000 과 같은 형식

        char [] inputArr = input.toCharArray();

        for(int i=0;i<8;i++)
        {
            if(inputArr[i]=='0')
                isRed[i] = false;
            else if(inputArr[i]=='1')
                isRed[i] = true;
        }

        notifyLED();
        SetLEDDevice();
    }

    public void setFullLedView(String input)
    {
        // 들어온 인풋으로 값을 셋팅하고

        SetFullLEDDevice();
    }



    // 여기부터 미구현 메소드들을 채워넣기 시작한다. - 유정 & 내가 채워넣어야 하는 부분이다
    //---------------------------------------------------------------------------------


    // Socket을 끊는다. Discon, onBackPressed, onDestroy와 같은 경우에서 사용해야한다.
    public void DisconnectSock()
    {


        // 1. Sock 종료
        // 2. 이 Activity내의 Thread 값 null로 변경
        // 3. 그리고 onBackPressed가 아닌 경우, 다시 생성해주어야한다. 메소드로 분리필요
        for(int position=0;position<mcontrollerThread.size();position++) {
            //mcontrollerThread.get(i).sendCheck("EXIT:");
            try {
                mcontrollerThread.get(position).sendMessage("EXIT:");
                mcontrollerThread.remove(position);
                myData.remove(position);
                //mcontrollerThread.get(i).closeConnection();
            } catch (Exception e) {
            }
        }
        controlleeView.getAdapter().notifyDataSetChanged();
    }

    // Read Only - Read/Write 를 설정하는 순간 Controller에게 보내야 한다.
    public void SendMode(boolean mode)
    {
        // 1. Controller에게 전송해야한다.
        // 2. mode가 true라면 On , false라면 Off
    }

    public void SendTextLCD(String text)
    {

    }

    public void Send7Segment(int input)
    {

    }

    public void SendLED(boolean [] input)
    {

    }

    public void SendDotMatrix(String input)
    {

    }

    // 이 메소드는 DotMatrix를 하나씩 찍도록 하기 위함..
    public void SendDotMatrix(boolean[][] input)
    {

    }

    public void SendFullLED(String [][] input)
    {

    }

    // 여기까지는 통신과 관련된 부분 -> 유정이랑 함께 짜야하는 부분
    //---------------------------------------------------------------------------------


    //---------------------------------------------------------------------------------
    // 여기부터는 버튼 등의 이벤트 부분, 내 스스로 짜야하는 부분이다.

    // 입력받은 Text로부터 Dotview로 바꾸어준다.
    public void setDot_fromText(String input)
    {
        // 여기서 Text를 DotView로 띄워줘야한다.
    }


    // 이건 DotMatrix 이미지 클릭형식
    public void Dot_Advanced_Clicked()
    {
        boolean [][] tmpDot = new boolean[7][10];
        // 데이터를 가져와야한다.

        et_dot.setText("");
        // Grid View를 건드리게 되면, setDot EditText를 그냥 날려버린다.
        SendDotMatrix(tmpDot);
        // 오버로딩 되어있어
    }

    // 일반 DotMatrix 전송
    public void Dot_Send_Clicked()
    {
        SetDotDevice();
        setDot_fromText(et_dot.getText().toString());
        SendDotMatrix(et_dot.getText().toString());
    }

    public void Seg7_Clicked()
    {
        Set7SegmentDevice();

        Send7Segment(Integer.valueOf(et_7seg.getText().toString()));
    }

    public void Led_Clicked()
    {
        SetLEDDevice();

        boolean [] sendLed = new boolean[8];
        for(int i= 0;i<8;i++)
        {
            sendLed[i] = isRed[i];
        }

        SendLED(sendLed);
    }

    public void Full_Led_Clicked()
    {
        // 쓰레드가 필요하지 않음
        SetFullLEDDevice();

        // 이부분 추가 요망, UI의 변경이 우선이다.

        String [][] tmp  = new String[4][3];

        for(int i=0;i<4;i++)
        {
            for(int j=0;j<3;j++)
            {
                tmp[i][j] = new String(String.valueOf(fullData[i][j]));
            }
        }

        SendFullLED(tmp);

    }
    public void Text_LCD_Clicked()
    {
        // 쓰레드가 필요하지 않음
        // 1. 나 자신의 Device를 적용하고
        SetTextDevice();

        // 2. Controller로 내 Status를 전송한다.
        SendTextLCD(et_textlcd.getText().toString());
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
