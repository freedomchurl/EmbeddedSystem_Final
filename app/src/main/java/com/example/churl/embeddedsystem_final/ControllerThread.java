package com.example.churl.embeddedsystem_final;

import android.util.Log;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by churl on 2017-12-08.
 */

public class ControllerThread extends Thread {

    Socket sock = null;
    int port;
    ControllerActivity activity;
    String ip = null;

    ControllerSendThread send = null;
    ControllerRecvThread recv = null;

    ArrayList<ControlleeData> myData = null;

    ControllerThread(String ip, int port, ControllerActivity activity, ArrayList<ControlleeData> input)
    {
        this.port = port;
        this.ip = ip;
        this.myData = input;
        this.activity = activity;
    }

    public void run()
    {
        try{

            sock = new Socket(ip,port);

            send = new ControllerSendThread(sock,activity,myData,this);
            recv = new ControllerRecvThread(sock,activity,myData,this);

            send.start();
            recv.start();

            Log.d("Client","Send Recv Create");

        }catch (Exception e){}
    }

    public void stopThread()
    {
        if(send!=null && recv!=null) {
            send.isRunning = false;
            recv.isRunning = false;
            try {
                sock.close();
            } catch (Exception e) {
            }
        }
    }

    public void sendMessage(String msg)
    {
        if(send!=null) {
            send.message = new String(msg);
            send.hasMessage = true;
        }
    }


    public void sendData()
    {
        String allMessage = recv.myTmp.getData();
        // 여기서 가져오고 나서

        if(send!=null && recv.myTmp.isReadMode==false)
        {
            send.message = new String(allMessage);
            send.hasMessage = true;
        }
    }

}
