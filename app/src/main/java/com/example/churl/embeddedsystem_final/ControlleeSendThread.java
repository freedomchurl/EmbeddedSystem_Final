package com.example.churl.embeddedsystem_final;

import android.util.Log;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Created by churl on 2017-12-08.
 */

public class ControlleeSendThread extends Thread {

    boolean isRunning = true;
    boolean hasMessage = false;

    Socket sock = null;
    ControlleeMainActivity activity;
    String message = null;
    DataOutputStream dos = null;

    ControlleeData myData = null;

    ControlleeSendThread(Socket sock, ControlleeMainActivity activity,ControlleeData myData)
    {
        this.myData = myData;
        this.sock = sock;
        this.activity = activity;
        try {
            this.dos = new DataOutputStream(sock.getOutputStream());
        }catch (Exception e){}
    }

    public void run()
    {
        Log.d("Server","SendThread Create");

        // 여기서 디바이스 이름/아이콘/모드를 보내야한다.
        String info = "INFO:";

        if(activity.isReadMode()==true)
        {
            message = info + activity.getDeviceName() + "\n" + activity.getIconNum() + "\n" + "TRUE";
        }
        else
        {
            message = info + activity.getDeviceName() + "\n" + activity.getIconNum() + "\n" + "FALSE";
        }

        try {
            dos.writeUTF(message);
        }catch (Exception e){}
        //INFO:NAME\nICONNUM\nTRUE;

        String currentDevice = myData.getData();
        try {
            dos.writeUTF(currentDevice);
        }catch (Exception e){}
        Log.d("Server","초기 정보를 보내는데 성공");


        while(isRunning)
        {
            if(hasMessage)
            {
                try {
                    dos.writeUTF(message);
                    Log.d("Server",message + " 쓰기 완료");
                    hasMessage = false;

                    if(message.equals("EXIT:"))
                    {
                        // EXIT: 일 경우
                        activity.StopThread();
                    }
                    else if(message.equals("REST:"))
                    {
                        Log.d("여길 들어가긴하나?","궁금하네");
                        activity.ReStartServerSock();
                    }


                }catch (Exception e){}
            }
        }
    }
}
