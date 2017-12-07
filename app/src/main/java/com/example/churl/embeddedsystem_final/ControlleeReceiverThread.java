package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by churl on 2017-11-29.
 */

public class ControlleeReceiverThread extends Thread {
    private Socket socket;
    DataInputStream reader;
    private Activity activity;
    public ControlleeReceiverThread(Socket socket,Activity activity)
    {
        this.socket = socket;
        this.activity = activity;
    }
    @Override
    public void run()
    {
        try {

            reader = new DataInputStream(socket.getInputStream());

            while(true)
            {
                String str = reader.readUTF();
                Log.d("Server THREAD", "read from client : "+str);
                if(str==null){
                    break;
                }
                final String finalRecv = str;
                final String type = str.substring(0,5);
                final String data = str.substring(5);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("THREAD","first : "+type+"final : "+finalRecv);
                        switch (type){
                            case "TEXT:" :
                                ((ControlleeMainActivity)activity).eventTextLCD(data);
                                break;
                            case "DOTM:" :
                                ((ControlleeMainActivity)activity).eventDOTM(data);
                                break;
                            case "7SEG:" :
                                ((ControlleeMainActivity)activity).event7SEG(data);
                                break;
                            case "FULL:" :
                                ((ControlleeMainActivity)activity).eventFullColor(data);
                                break;
                            case "FLED:" :
                                ((ControlleeMainActivity)activity).eventLED(data);
                                break;
                            default:
                        }
                    }
                });
                Thread.sleep(1000);
            }
            reader.close();
            } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
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

}
