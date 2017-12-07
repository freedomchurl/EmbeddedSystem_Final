package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by YuJeong on 2017-12-04.
 */

public class ControlleeThread extends Thread {
    private static int GIVEN_PORT;
    private  ServerSocket mServerSocket;
    private  Socket connection = null;
    private String str;
    private ControlleeSenderThread sender;
    private ControlleeReceiverThread reader;
    private Activity activity;
    public ControlleeThread(int PORT, Activity activity){
        GIVEN_PORT = PORT;
        this.activity = activity;
    }

    @Override
    public void run(){
        try{

            Log.d("Thread","start controllee ");
            mServerSocket = new ServerSocket(GIVEN_PORT);
            connection = mServerSocket.accept();
            sender = new ControlleeSenderThread(connection,activity);
            reader = new ControlleeReceiverThread(connection,activity);
            Log.d("Thread","controlee str : ");
            reader.start();
            sender.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closeServer() throws  IOException{
        if(mServerSocket!=null&&!mServerSocket.isClosed()){
            mServerSocket.close();
        }
    }
    public void setSendMSG(String msg){
        ((ControlleeSenderThread)sender).setMSG(msg);
    }
    public void sendCheck(String msg){
        ((ControlleeSenderThread)sender).setMSG(msg);
        Log.d("THREAD","함수부분 : "+msg);
    }
}
