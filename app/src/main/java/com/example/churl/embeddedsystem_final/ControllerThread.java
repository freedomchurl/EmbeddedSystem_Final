package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by YuJeong on 2017-12-04.
 */

public class ControllerThread extends Thread{

    private static int GIVEN_PORT;
    private static String GIVEN_IP;
    private static final int TIMEOUT =5000;
    private Socket mSocket;
    private String str;
    private boolean mShouldStop;
    private Thread sender;
    private Thread reader;



    private Activity activity;
    public ControllerThread(String GIVEN_IP, int GIVEN_PORT, Activity activity)
    {
        this.GIVEN_PORT =GIVEN_PORT ;
        this.GIVEN_IP = GIVEN_IP;
        this.activity = activity;
        mShouldStop = true;

    }
    @Override
    public void run(){
        try{

            mSocket = new Socket( GIVEN_IP, GIVEN_PORT);
            Log.d("Thread","start controller :"+GIVEN_IP +" port : "+GIVEN_PORT);
            mSocket.setSoTimeout(TIMEOUT);

           sender = new ControllerSenderThread(mSocket,activity);
           reader = new ControllerReceiverThread(mSocket,activity);


            Log.d("Thread","controlee str : "+str);
            Log.d("Thread","start d :"+GIVEN_IP +" port : "+GIVEN_PORT);
            sender.start();
            reader.start();

        }catch (IOException e){
            Log.d("Thread","이미 이건 Controller가 있는 Controllee지롱!");
            e.printStackTrace();
        }
    }

    public void closeConnection() throws IOException{
        mShouldStop = true;
        if(mSocket!=null){
            mSocket.close();
        }
        mSocket = null;
    }

    public void sendCheck(String msg){

      ((ControllerSenderThread)sender).setMSG(msg);
        Log.d("THREAD","함수부분 : "+msg);
    }

}
