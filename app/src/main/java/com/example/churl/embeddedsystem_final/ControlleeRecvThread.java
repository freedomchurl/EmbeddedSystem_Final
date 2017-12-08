package com.example.churl.embeddedsystem_final;

import android.util.Log;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * Created by churl on 2017-12-08.
 */

public class ControlleeRecvThread extends Thread {

    String message = null;
    DataInputStream dis = null;
    Socket sock = null;
    ControlleeMainActivity activity;

    boolean isRunning = true;

    ControlleeRecvThread(Socket sock , ControlleeMainActivity activity)
    {
        this.sock = sock;
        this.activity = activity;
        try{
            dis = new DataInputStream(sock.getInputStream());
        }catch (Exception e){}
    }

    public void run()
    {
        Log.d("Server","RecvThread Create");

        while(isRunning)
        {
            try {
                if (dis.available() != 0) {
                    message = dis.readUTF();

                    Log.d("Server",message + " 읽기 완료");

                    // 여기서 메시지 처리를 해야한다.

                    if(message.equals("EXIT:"))
                    {
                        // 종료를 해야한다면!

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.getControllerIP().setText("Not Connected!");
                            }
                        });

                        activity.ReStartServerSock();
                    }
                }
            }catch (Exception e){}
        }
    }
}
