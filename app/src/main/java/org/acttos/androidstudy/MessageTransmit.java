package org.acttos.androidstudy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by 天才猪 on 2018/1/13.
 */

public class MessageTransmit implements Runnable {

    private static final String TAG = "MessageTransmit";

    private static final String HOST = "111.207.81.208";
    private static final int PORT = 12345;

    private Socket mSocket;
    private BufferedReader mReader = null;
    private OutputStream mWriter = null;

    @Override
    public void run() {
        mSocket = new Socket();

        try {
            mSocket.connect(new InetSocketAddress(HOST, PORT) ,3000);
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mWriter = mSocket.getOutputStream();

            new RecvThread().start();
            Looper.prepare();
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Handler mRecvHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Log.d(TAG, "handle message:" + message.obj);

            String messageToSend = message.obj.toString() + "\n";

            try {
                mWriter.write(messageToSend.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private class RecvThread extends Thread {
        @Override
        public void run() {
            try {
                String content = null;
                while ((content = mReader.readLine()) != null) {
                    Message msg = Message.obtain();
                    msg.obj = content;

                    MainActivity.mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
