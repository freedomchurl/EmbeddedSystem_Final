package com.example.churl.embeddedsystem_final;

import android.util.Log;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by churl on 2017-12-08.
 */

public class ControlleeThread extends Thread {

    ServerSocket serverSocket = null;
    Socket sock = null;
    int port;
    ControlleeMainActivity activity;

    ControlleeData myData = null;

    ControlleeSendThread send = null;
    ControlleeRecvThread recv = null;

    ControlleeThread(int port, ControlleeMainActivity activity,ControlleeData myData)
    {
        this.myData = myData;
        this.port = port;
        this.activity = activity;
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);

            Log.d("Server","Waiting Accept");
            sock = serverSocket.accept();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.setControllerIP_Port(sock.getInetAddress().toString() ,sock.getPort());
                }
            });


            send = new ControlleeSendThread(sock,activity);
            recv = new ControlleeRecvThread(sock,activity);

            send.start();
            recv.start();

            Log.d("Server","Send Recv Create");


        }catch (Exception e){}
    }

    public void stopThread()
    {
        if(send!=null && recv!=null) {
            Log.d("이것도","이것도");
            send.isRunning = false;
            recv.isRunning = false;
            try {
                sock.close();
                Log.d("StopThread 정상","정상인가");
            } catch (Exception e) {
            }
        }
    }

    public void Restart()
    {
        try {
            Log.d("Server","Waiting Accept");
            sock = serverSocket.accept();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.setControllerIP_Port(sock.getInetAddress().toString() ,sock.getPort());
                }
            });


            send = new ControlleeSendThread(sock,activity);
            recv = new ControlleeRecvThread(sock,activity);

            send.start();
            recv.start();

            Log.d("Server","Send Recv Create");


        }catch (Exception e){}
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
       String allMessage = myData.getData();

        if(send!=null)
        {
            send.message = new String(allMessage);
            send.hasMessage = true;
        }
    }
}
