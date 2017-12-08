package com.example.churl.embeddedsystem_final;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by churl on 2017-12-08.
 */

public class ControllerSendThread extends Thread {

    boolean isRunning = true;
    boolean hasMessage = false;
    String message = null;
    DataOutputStream dos = null;

    Socket sock = null;
    ControllerActivity activity = null;
    ArrayList<ControlleeData> myData = null;

    ControllerThread crThread = null;

    ControllerSendThread(Socket sock,ControllerActivity activity, ArrayList<ControlleeData> input,ControllerThread crThread)
    {
        this.crThread = crThread;
        this.sock = sock;
        this.activity = activity;
        this.myData = input;

        try {
            this.dos = new DataOutputStream(sock.getOutputStream());
        }catch (Exception e){}
    }

    public void run()
    {
        Log.d("Client","Client Send Thread create");

        while(isRunning)
        {
            if(hasMessage)
            {
                try
                {
                    dos.writeUTF(message);
                    Log.d("Client",message + " 쓰기 완료");
                    hasMessage = false;

                    if(message.equals("EXIT:"))
                    {
                        // EXIT: 일 경우
                        crThread.stopThread();

                    }

                }catch (Exception e){}
            }
        }
    }
}
