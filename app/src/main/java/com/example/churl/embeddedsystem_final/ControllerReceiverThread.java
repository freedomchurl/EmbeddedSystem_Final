package com.example.churl.embeddedsystem_final;

import android.app.Activity;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by churl on 2017-11-29.
 */

public class ControllerReceiverThread extends Thread {

    Socket socket;
    String str=null;
    boolean sendCheck = false;
    DataInputStream reader;
    private boolean mShouldStop;
    private Activity activity;

    public ControllerReceiverThread(Socket socket,Activity activity)
    {
        this.socket = socket;
        this.activity = activity;
        mShouldStop = true;

    }

    @Override
    public void run()
    {
        try {
            reader = new DataInputStream(socket.getInputStream());
        }catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try{
                str = reader.readUTF();
                if(str != null){
                    Log.d("Client THREAD", "read from server : "+str);
                }

                final String finalRecv = str;
                final String type = str.substring(0,5);
                final String data = str.substring(5);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("THREAD","first : "+type+"finarl : "+finalRecv);
                       // ((ControlleeMainActivity)activity).setYujeongText(finalRecv);
                        switch (type){
                            case "TEXT:" :
                                setTEXT(data);
                                break;
                            case "DOTM:" :
                                setDOTM();
                                break;
                            case "7SEG:" :
                                set7SEG();;
                                break;
                            case "FCOL:" :
                                setFCOL();
                                break;
                            case "LED :" :
                                setLED();
                                break;
                            default:
                        }
                    }
                });
                Thread.sleep(1000);
            }catch (Exception e){
            }
        }
    }

    public void setTEXT(String data) {
        byte [] text = data.getBytes();
        ((ControlleeMainActivity)activity).TextLCDWrite(text,0);

        Log.d("THREAD","textLcd");
    }
    private void setDOTM() {
        Log.d("THREAD","DotMatrix");
    }
    private void set7SEG() {
        Log.d("THREAD","7Segment");
    }
    private void setLED() {
        Log.d("THREAD","LED");
    }
    private void setFCOL() {
        Log.d("THREAD","FColorLED");
    }




   /* public void closeConnection() throws IOException{
        mShouldStop = true;
        if(socket!=null){
            socket.close();
        }
        socket = null;
    }*/
}
