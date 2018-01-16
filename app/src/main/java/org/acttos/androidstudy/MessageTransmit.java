package org.acttos.androidstudy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    private BufferedReader mReader;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    @Override
    public void run() {
        mSocket = new Socket();

        try {
            if (!mSocket.isConnected() || !mSocket.isBound()) {
                mSocket.connect(new InetSocketAddress(HOST, PORT) ,3000);
            }

            mInputStream = mSocket.getInputStream();
            mReader = new BufferedReader(new InputStreamReader(mInputStream));
            mOutputStream = mSocket.getOutputStream();

            Thread t = new MessageReceiverThread();
            t.start();
        } catch (Exception e) {
            Log.e(TAG, "Error occurs when connecting the socket", e);
            try {
                mSocket.close();
            } catch (IOException e1) {
                Log.e(TAG, "Error occurs when closing the socket", e);
            }
        }
    }

    public Handler mSendMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
//            Log.d(TAG, "handle message:" + message.obj);

            String messageToSend = message.obj.toString();

            try {
                byte[] bytes = MessageTransmit.addLengthToBytes(messageToSend.getBytes());
                Log.d(TAG,"SEND_MSG:" + new String(bytes));
                mOutputStream.write(bytes);
            } catch (Exception e) {
                Log.e(TAG, "Error occurs when writing data with mOutputStream through the socket", e);
                try {
                    mOutputStream.close();
                } catch (IOException e1) {
                    Log.e(TAG, "Error occurs when closing the mOutputStream", e);
                }
            }
        }
    };

    public static int bytesToInt(byte[] bytes) {
        int result;
        result = (int) (((bytes[0] & 0xFF) << 24)
                | ((bytes[1] & 0xFF) << 16)
                | ((bytes[2] & 0xFF) << 8)
                | (bytes[3] & 0xFF));

        return result;
    }

    public static byte[] intToBytes(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);

        return result;
    }

    public static byte[] addLengthToBytes(byte[] bytes) {
        int length = bytes.length;
        Log.d(TAG, "Data Length to send:" + length);
        byte[] lengthBytes = MessageTransmit.intToBytes(length);
        Log.d(TAG,"Data Length Parsed:" + MessageTransmit.bytesToInt(lengthBytes));

        byte[] result = new byte[lengthBytes.length + length];
        System.arraycopy(lengthBytes, 0, result, 0, 4);
        System.arraycopy(bytes, 0, result, 4, bytes.length);

        return result;
    }

    public static byte[] removeLengthFromBytes(byte[] bytes) {
        byte[] result = new byte[bytes.length - 4];
        System.arraycopy(bytes, 4, result, 0, bytes.length - 4);

        return result;
    }

    /* 定义一个消息接收的内部类，专门负责处理接收到的消息 */
    private class MessageReceiverThread extends Thread {
        @Override
        public void run() {
            try {
//                while (true) {
                    String content = null;
                    byte[] headerBytes = new byte[4];
                    int dataLength = 0;

                    if (mInputStream.read(headerBytes, 0, 4) != -1) {
                        dataLength = MessageTransmit.bytesToInt(headerBytes);
                        Log.d(TAG,"Data Received length is:" + dataLength);
                    }

                    byte[] dataBytes = new byte[dataLength];
                    if (mInputStream.read(dataBytes, 0, dataLength) != -1) {
                        Message msg = Message.obtain();
                        msg.obj = new String(dataBytes);
                        Log.d(TAG,"RECV_MSG:" + msg.obj.toString());
//                        Log.d(TAG, "Received Message In MessageTransmit:" + content);
                        MainActivity.mHandler.sendMessage(msg);
                    }
//                }
//            while (true) {
//                Log.d(TAG, "Message Received");
//            //                for (int i = 0; i < 4; i++) {
//            //                    mInputStream.read();
//            //                }
//
//                int length;
//                byte[] totalDataBytes = new byte[2048];
//                while ((length = mInputStream.read(totalDataBytes, 4, 2048)) != -1) {
//                    Log.d(TAG, "Read Length:" + length);
//                }
//
//                Message msg = Message.obtain();
//                msg.obj = new String(totalDataBytes);
//                Log.d(TAG,"RECV_MSG:" + msg.obj.toString());
//            //                        Log.d(TAG, "Received Message In MessageTransmit:" + content);
//                MainActivity.mHandler.sendMessage(msg);
//            }

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
}
