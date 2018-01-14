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

        mMessageTransmit = new MessageTransmit();
        new Thread(mMessageTransmit).start();

        Button connectButton = this.findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(mMessageSender).start();
            }
        });

        Button upButton = findViewById(R.id.up_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "UpButton clicked.");
                new Thread(mMessageSender).start();
            }
        });

        Button downButton = findViewById(R.id.down_button);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DownButton clicked.");
                new Thread(mMessageSender).start();
            }
        });

        Button leftButton = this.findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "LeftButton clicked.");
                new Thread(mMessageSender).start();
            }
        });

        Button rightButton = this.findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "RightButton clicked.");
                new Thread(mMessageSender).start();
            }
        });

        Button catchButton = this.findViewById(R.id.catch_button);
        catchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Catch Button Clicked.");
                new Thread(mMessageSender).start();
            }
        });
    }

    Runnable mMessageSender = new Runnable(){
        @Override
        public void run() {
            // TODO: http request.
            Message message = Message.obtain();
            message.obj = String.format("%d", new Date().getTime());

            mMessageTransmit.mRecvHandler.sendMessage(message);

        }
    };

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            Log.d(TAG, message.obj.toString());
        }
    };

    private class CheckThread extends Thread {
        private String mHostName;

        public CheckThread(String hostName) {
            this.mHostName = hostName;
        }

        @Override
        public void run() {
            Message message = Message.obtain();

            try {
                InetAddress host = InetAddress.getByName(mHostName);
                boolean isReachable = host.isReachable(5000);

                String desc = isReachable ? "网络可以连通" : "网络不可达";
                if (isReachable) {
                    desc = String.format("%s\n 主机名为%s\n主机地址为%s", desc, host.getHostName(), host.getHostAddress());
                }
                message.what = 0;
                message.obj = desc;
            } catch (Exception e) {
                e.printStackTrace();
                message.what = -1;
                message.obj = e.getMessage();
            }

            mHandler.sendMessage(message);
        }
    }
}


