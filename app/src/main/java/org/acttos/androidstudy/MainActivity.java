package org.acttos.androidstudy;

import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.acttos.androidstudy.bean.ControlCommand;
import org.acttos.androidstudy.bean.ControlCommandEvent;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.net.InetAddress;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";
    private MessageTransmit mMessageTransmit;

    private static String token = "6e7de5f465a4011a42b8a8241ba05dbe";
    private static long roomId = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connectButton = this.findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "ConnectButton clicked.");
                new Thread(networkTask).start();
            }
        });

        Button upButton = findViewById(R.id.up_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "UpButton clicked.");
                ControlCommand command = ControlCommand.generateCommand(MainActivity.token, MainActivity.roomId, ControlCommandEvent.Forward);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mSendMessageHandler.sendMessage(message);

//                new Thread(mMessageSender).start();
            }
        });

        Button downButton = findViewById(R.id.down_button);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DownButton clicked.");
                ControlCommand command = ControlCommand.generateCommand(MainActivity.token, MainActivity.roomId, ControlCommandEvent.Backward);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mSendMessageHandler.sendMessage(message);
            }
        });

        Button leftButton = this.findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "LeftButton clicked.");
                ControlCommand command = ControlCommand.generateCommand(MainActivity.token, MainActivity.roomId, ControlCommandEvent.Left);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mSendMessageHandler.sendMessage(message);
            }
        });

        Button rightButton = this.findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "RightButton clicked.");
                ControlCommand command = ControlCommand.generateCommand(MainActivity.token, MainActivity.roomId, ControlCommandEvent.Right);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mSendMessageHandler.sendMessage(message);
            }
        });

        Button catchButton = this.findViewById(R.id.catch_button);
        catchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Catch Button Clicked.");
                ControlCommand command = ControlCommand.generateCommand(MainActivity.token, MainActivity.roomId, ControlCommandEvent.Catch);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mSendMessageHandler.sendMessage(message);
            }
        });
    }

//    Runnable mMessageSender = new Runnable(){
//        @Override
//        public void run() {
//            // TODO: http request.
//            Message message = Message.obtain();
//            message.obj = String.format("%d", new Date().getTime());
//            Log.d(TAG, "Out Msg:" +  message.obj.toString());
//            mMessageTransmit.mRecvHandler.sendMessage(message);
//
//        }
//    };

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            String JSON = message.obj.toString();
            Log.d(TAG, "Rcv Msg:" + JSON);

            ControlCommand command = ControlCommand.commandFromJSON(JSON);
        }
    };

    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            Looper.prepare();
            mMessageTransmit = new MessageTransmit();
            new Thread(mMessageTransmit).start();
            Looper.loop();
        }
    };
}


