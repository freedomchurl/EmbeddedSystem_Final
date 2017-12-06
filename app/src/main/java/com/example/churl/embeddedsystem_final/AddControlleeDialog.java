package com.example.churl.embeddedsystem_final;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by churl on 2017-12-07.
 */

public class AddControlleeDialog extends Dialog{

    private Context context;

    private Button CancelButton = null;
    private Button OkButton = null;

    private EditText inputIP = null;
    private EditText inputPort = null;
    private EditText inputPwd = null;

    private String name;

    private CustomDialogEventListener onCustomDialogEventListener;

    public AddControlleeDialog(@NonNull Context context, CustomDialogEventListener onCustomDialogEventListener)
    {
        super(context);
        this.onCustomDialogEventListener = onCustomDialogEventListener;
        this.context = context;
    }

    public AddControlleeDialog(Context context,String name)
    {
        super(context);
        this.context = context;
        this.name = name;
    }

    public interface CustomDialogEventListener{
        public void customDialogEvent(String ip, String pwd, int port);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addcontrollee);

        inputIP = (EditText) findViewById(R.id.add_controllee_ip);
        inputPort = (EditText) findViewById(R.id.add_controllee_port);
        inputPwd = (EditText) findViewById(R.id.add_controllee_pwd);

        CancelButton = (Button) findViewById(R.id.add_cancel_Button);
        OkButton = (Button) findViewById(R.id.add_ok_Button);

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ip = inputIP.getText().toString();
                int port = Integer.valueOf(inputPort.getText().toString());
                String pwd = inputPwd.getText().toString();

                // Activity로 전달해주어야한다. Interface를 사용하자.
                onCustomDialogEventListener.customDialogEvent(ip,pwd,port);

                cancel();

            }
        });

    }

}
