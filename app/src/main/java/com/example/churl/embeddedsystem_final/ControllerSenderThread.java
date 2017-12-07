package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by churl on 2017-11-29.
 */

public class ControllerSenderThread extends Thread {
    Socket socket;
    boolean sendCheck = false;
    DataOutputStream writer;
    String str="start";
    private static boolean mShouldStop;
    private Activity activity;
    public ControllerSenderThread(Socket socket,Activity activity)
    {
        this.socket = socket;
        this.activity = activity;
        mShouldStop = true;
    }

    @Override
    public void run()
    {
        try {
            writer = new DataOutputStream(socket.getOutputStream());
            while(true)
            {
                try{
                    if(!sendCheck){
                       // str=((ControllerActivity)activity).setString();
                        writer.writeUTF(str);
                        sendCheck = true;
                        Log.d("Client THREAD", "write sender : "+str);
                    }
                }catch (Exception ingored){}
            }
            //String str = reader.readUTF();
            } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            if(socket!=null)
            {
                try {
                    socket.close();

                } catch (Exception ignored) {
                }
            }
            socket = null;
        }
    }
    public void setMSG(String str){
        this.str = str;
        sendCheck = false;
        Log.d("THREAD","senderThread str:"+str);
    }
}
