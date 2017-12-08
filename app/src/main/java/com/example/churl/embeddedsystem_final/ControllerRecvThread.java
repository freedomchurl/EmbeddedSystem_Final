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
                    else if(token.equals("DATA"))
                    {
                        // Data라면,
                        Log.d("Server","DATA에 들어왔다");

                        String [] dataList = message.split("\n");
                        Log.d("Server","DATA에 들어왔다1");
                        String textData = dataList[1].substring(5);
                        String dotmData = dataList[2].substring(5);
                        String segmData = dataList[3].substring(5);
                        String fullData = dataList[4].substring(5);

                        Log.d("Server","DATA에 들어왔다2");
                        String [] splitFullData = fullData.split("//");

                        Log.d("Server","DATA에 들어왔3" + fullData + " Size = " + splitFullData.length);
                        int [][] fullArray = new int[4][3];

                        for(int i=0;i<4;i++)
                        {
                            for(int j=0;j<3;j++)
                            {
                                Log.d("FULLDATA",splitFullData[(i*3)+j]);
                                fullArray[i][j] = Integer.valueOf(splitFullData[(i*3)+j]);
                            }
                        }

                        Log.d("Server","DATA에 들어왔다4");
                        boolean modeData;

                        if(dataList[5].substring(5).equals("TRUE"))
                            modeData = true;
                        else
                            modeData = false;
                        Log.d("Server","DATA에 들어왔다4");
                        char [] ledData = dataList[6].substring(5).toCharArray();
                        boolean [] ledValues = new boolean[8];
                        for(int i=0;i<8;i++)
                        {
                            if(ledData[i]=='1')
                                ledValues[i] = true;
                            else
                                ledValues[i] = false;
                        }

                        Log.d("Server",myTmp.isReadMode + " 무슨 모드일까");

                        Log.d("수정이들어간다","수정");
                        myTmp.setFull(fullArray);
                        myTmp.setTextData(textData);
                        myTmp.setDotData(dotmData);
                        myTmp.setSegData(Integer.valueOf(segmData));
                        myTmp.setLed(ledValues);
                        myTmp.setReadMode(modeData);

                        //  자기꺼일때만 UI변경
                        if(activity.CurrentClickedItem==myData.indexOf(myTmp)) {
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
