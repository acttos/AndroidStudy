package org.acttos.androidstudy;

import android.nfc.Tag;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        Button connectButton = this.findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "ConnectButton clicked.");
                mMessageTransmit = new MessageTransmit();
                new Thread(mMessageTransmit).start();
            }
        });

        Button upButton = findViewById(R.id.up_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "UpButton clicked.");
                ControlCommand command = ControlCommand.generateCommand("token", 100241, ControlCommandEvent.Forward);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mRecvHandler.sendMessage(message);

//                new Thread(mMessageSender).start();
            }
        });

        Button downButton = findViewById(R.id.down_button);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DownButton clicked.");
                ControlCommand command = ControlCommand.generateCommand("token", 100241, ControlCommandEvent.Backward);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mRecvHandler.sendMessage(message);
            }
        });

        Button leftButton = this.findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "LeftButton clicked.");
                ControlCommand command = ControlCommand.generateCommand("token", 100241, ControlCommandEvent.Left);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mRecvHandler.sendMessage(message);
            }
        });

        Button rightButton = this.findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "RightButton clicked.");
                ControlCommand command = ControlCommand.generateCommand("token", 100241, ControlCommandEvent.Right);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mRecvHandler.sendMessage(message);
            }
        });

        Button catchButton = this.findViewById(R.id.catch_button);
        catchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Catch Button Clicked.");
                ControlCommand command = ControlCommand.generateCommand("token", 100241, ControlCommandEvent.Catch);
                String JSON = command.JSONString();

                Message message = Message.obtain();
                message.obj = JSON;
                Log.d(TAG, "Out Msg:" +  message.obj.toString());
                mMessageTransmit.mRecvHandler.sendMessage(message);
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
}


