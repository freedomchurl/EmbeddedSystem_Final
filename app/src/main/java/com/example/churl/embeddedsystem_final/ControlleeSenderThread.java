package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by churl on 2017-11-29.
 */

public class ControlleeSenderThread extends Thread {
    private Socket socket;
    private Activity activity;
    boolean sendCheck = false;
    DataOutputStream writer;
    String str="힝";
    public ControlleeSenderThread(Socket socket, Activity activity)
    {
        this.socket = socket;
        this.activity = activity;
    }
    @Override
    public void run()
    {
        try {
            writer = new DataOutputStream(socket.getOutputStream());
            //reader = new DataInputStream(socket.getInputStream());
            Log.d("Server THREAD", "힝");
            while(true)
            {
                try{
                    if(!sendCheck){
                        writer.writeUTF(str);
                        sendCheck = true;
                        Log.d("Server THREAD", "write server : "+ str);

                    }
                }catch (Exception ingored){}
            }

            } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            if(socket!=null)
            {
                try {
                    sendCheck = false;
                    socket.close();
                } catch (Exception ignored) {
                }
            }
            socket = null;
        }
    }

    public void setSend(){
        sendCheck = false;
    }

    public void setMSG(String str){
        this.str = str;
    }

}
