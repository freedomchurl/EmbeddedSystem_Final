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

    ControlleeData myData = null;

    ControlleeRecvThread(Socket sock , ControlleeMainActivity activity,ControlleeData myData)
    {
        this.myData = myData;
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
                    else if(message.startsWith("DATA:"))
                    {
                        // Data라면,
                        Log.d("Server","DATA에 들어왔다");

                        String [] dataList = message.split("\n");

                        String textData = dataList[1].substring(5);
                        String dotmData = dataList[2].substring(5);
                        String segmData = dataList[3].substring(5);
                        String fullData = dataList[4].substring(5);

                        String [] splitFullData = fullData.split("//");

                        int [][] fullArray = new int[4][3];
                        for(int i=0;i<4;i++)
                        {
                            for(int j =0;j<3;j++)
                            {
                                fullArray[i][j] = Integer.valueOf(splitFullData[i*3+j]);
                            }
                        }

                        boolean modeData;

                        if(dataList[5].substring(5).equals("TRUE"))
                            modeData = true;
                        else
                            modeData = false;

                        char [] ledData = dataList[6].substring(5).toCharArray();
                        boolean [] ledValues = new boolean[8];
                        for(int i=0;i<8;i++)
                        {
                            if(ledData[i]=='1')
                                ledValues[i] = true;
                            else
                                ledValues[i] = false;
                        }

                        Log.d("Server",myData.isReadMode + " 무슨 모드일까");
                        if(myData.isReadMode==false) // 읽기모드가 아닐때
                        {
                            Log.d("수정이들어간다","수정");
                            myData.setFull(fullArray);
                            myData.setTextData(textData);
                            myData.setDotData(dotmData);
                            myData.setSegData(Integer.valueOf(segmData));
                            myData.setLed(ledValues);
                            myData.setReadMode(modeData);

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    activity.UpdateUI();

                                }
                            });
                        }

                    }
                }
            }catch (Exception e){}
        }
    }
}
