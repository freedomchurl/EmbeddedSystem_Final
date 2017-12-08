package com.example.churl.embeddedsystem_final;

import android.util.Log;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by churl on 2017-12-08.
 */

public class ControllerRecvThread extends Thread {

    String message = null;
    DataInputStream dis = null;
    Socket sock = null;
    ControllerActivity activity;

    ControlleeData myTmp = null;
    boolean isRunning = true;

    ArrayList<ControlleeData> myData = null;
    ControllerThread crThread = null;

    ControllerRecvThread(Socket sock, ControllerActivity activity, ArrayList<ControlleeData> input,ControllerThread crThread)
    {
        this.crThread = crThread;
        this.sock = sock;
        this.activity = activity;
        this.myData = input;
        try{
            dis = new DataInputStream(sock.getInputStream());
        }catch (Exception e){}
    }

    public void run()
    {
        Log.d("Client","RecvThread Create");

        while(isRunning)
        {
            try {
                if (dis.available() != 0) {
                    message = dis.readUTF();

                    Log.d("Client",message + " 읽기 완료");

                    // 여기서 메시지 처리를 해야한다.

                    String token = message.split(":")[0]; // 첫번째 토큰

                    if(token.equals("INFO"))
                    {
                        String inputMessage = message.substring(5);
                        String [] dataList = inputMessage.split("\n");

                        myTmp = new ControlleeData(dataList[0],sock.getInetAddress().toString(),Integer.valueOf(dataList[1]),dataList[2]);
                        myData.add(myTmp);


                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                    else if(token.equals("EXIT") || token.equals("REST"))
                    {
                        Log.d("Client","EXIT 명령");
                        myData.remove(myTmp); // 데이터를 지우고
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.getAdapter().notifyDataSetChanged();
                            }
                        });

                        activity.mcontrollerThread.remove(crThread);
                        crThread.stopThread();
                        Log.d("Client","EXIT 명령");
                    }


                }
            }catch (Exception e){}
        }
    }
}
