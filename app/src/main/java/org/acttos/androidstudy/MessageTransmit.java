package org.acttos.androidstudy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by 天才猪 on 2018/1/13.
 */

public class MessageTransmit implements Runnable {

    private static final String TAG = "MessageTransmit";

    private static final String HOST = "192.8.205.192";//"111.207.81.208";//;
    private static final int PORT = 8090;//12345;//;

    private Socket mSocket;
    private BufferedReader mReader;
    private OutputStream mWriter;

    @Override
    public void run() {
        mSocket = new Socket();

        try {
            if (!mSocket.isConnected() || !mSocket.isBound()) {
                mSocket.connect(new InetSocketAddress(HOST, PORT) ,3000);
            }

            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mWriter = mSocket.getOutputStream();

            Thread t = new RecvThread();
            t.start();

            Looper.prepare();
            Looper.loop();
        } catch (Exception e) {
            Log.e(TAG, "Error occurs when connecting the socket", e);
            try {
                mSocket.close();
            } catch (IOException e1) {
                Log.e(TAG, "Error occurs when closing the socket", e);
            }
        }
    }

    public Handler mRecvHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
//            Log.d(TAG, "handle message:" + message.obj);

            String messageToSend = message.obj.toString() + "\n";

            try {
                byte[] bytes = MessageTransmit.addLengthToBytes(messageToSend.getBytes());
                Log.d(TAG,"MSG:" + new String(bytes));
                mWriter.write(bytes);
            } catch (Exception e) {
                Log.e(TAG, "Error occurs when writing data with mWriter through the socket", e);
                try {
                    mWriter.close();
                } catch (IOException e1) {
                    Log.e(TAG, "Error occurs when closing the mWriter", e);
                }
            }
        }
    };

    private class RecvThread extends Thread {
        @Override
        public void run() {
            try {
                String content = null;
                while (true) {
                    if ((content = mReader.readLine()) != null) {
                        byte[] bytes = MessageTransmit.removeLengthFromBytes(content.getBytes("UTF-8"));
                        Message msg = Message.obtain();
                        msg.obj = new String(bytes);
                        Log.d(TAG,"MSG:" + msg.obj.toString());
//                        Log.d(TAG, "Received Message In MessageTransmit:" + content);
                        MainActivity.mHandler.sendMessage(msg);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error occurs when reading data from mReader of the socket", e);
                try {
                    mReader.close();
                } catch (IOException e1) {
                    Log.e(TAG, "Error occurs when closing the mReader", e);
                }
            }
        }
    }

    public static byte[] addLengthToBytes(byte[] bytes) {
        int length = bytes.length;
        byte[] lengthBytes = MessageTransmit.intToByteArray4(length);

        byte[] result = new byte[lengthBytes.length + length];
        System.arraycopy(lengthBytes, 0, result, 0, lengthBytes.length);
        System.arraycopy(bytes, 0, result, lengthBytes.length, bytes.length);

        return result;
    }

    public static byte[] removeLengthFromBytes(byte[] bytes) {
        byte[] result = new byte[bytes.length - 4];
        System.arraycopy(bytes, 4, result, 0, bytes.length - 4);

        return result;
    }

    public static byte[] intToByteArray4(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);

        return result;
    }
}
