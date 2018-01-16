package org.acttos.androidstudy.bean;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;

/**
 * Created by 天才猪 on 2018/1/14.
 */

public class ControlCommand {

    private static final String TAG = "ControlCommand";

    private String protocol;
    private ControlCommandEvent event;
    private String eventName;
    private String token;
    private long roomId;
    private int status;
    private int catchCount;

    public ControlCommand() {
        this.protocol = "";
        this.event = ControlCommandEvent.None;
        this.eventName = this.event.getName();
        this.token = "";
        this.roomId = -1;
        this.status = -1;
        this.catchCount = -1;
    }

    private ControlCommand(String protocol, ControlCommandEvent event, String token, long roomId, int status, int catchCount) {
        this.protocol = protocol;
        this.event = event;
        this.eventName = event.getName();
        this.token = token;
        this.roomId = roomId;
        this.status = status;
        this.catchCount = catchCount;
    }

    @Override
    public String toString() {
        return this.JSONString();
    }

    public String JSONString() {
        JSONObject object = new JSONObject();

        try {
            object.put("protocol", this.protocol);
            object.put("event", this.event.getCode());
            object.put("eventName", this.eventName);
            object.put("token", this.token);
            object.put("roomId", this.roomId);
            object.put("status", this.status);
            object.put("catchCount", this.catchCount);
        } catch (JSONException e) {
            Log.e(TAG, "Error occurs when generating JSON", e);
            object = null;
        }

        return object.toString();
    }

    public static ControlCommand commandFromJSON(String JSON) {
        if (JSON != null && !JSON.isEmpty()) {
            ControlCommand command = null;
            try {
                command = new ControlCommand();
                JSONObject object = new JSONObject(JSON);

                command.protocol = object.getString("protocol");
                int eventCode = object.getInt("event");
                ControlCommandEvent event = ControlCommandEvent.None;
                switch (eventCode) {
                    case 0:
                        event = ControlCommandEvent.Heartbeat;
                        break;
                    case 100:
                        event = ControlCommandEvent.Begin;
                        break;
                    case 101:
                        event = ControlCommandEvent.Forward;
                        break;
                    case 102:
                        event = ControlCommandEvent.Backward;
                        break;
                    case 103:
                        event = ControlCommandEvent.Left;
                        break;
                    case 104:
                        event = ControlCommandEvent.Right;
                        break;
                    case 105:
                        event = ControlCommandEvent.Catch;
                        break;
                    case 106:
                        event = ControlCommandEvent.CatchResult;
                        break;
                    case 107:
                        event = ControlCommandEvent.Quit;
                        break;
                    case 108:
                        event = ControlCommandEvent.Error;
                        break;

                    default:
                        event = ControlCommandEvent.None;
                        break;
                }
                command.event = event;
                command.token = object.getString("token");
                command.roomId = object.getLong("roomId");
                command.status = object.getInt("status");
                command.catchCount = object.getInt("catchCount");

            } catch (JSONException e) {
                Log.e(TAG, "Error occurs when parsing JSONString to ControlCommand", e);
                command = null;
            }

            return command;
        }

        return null;
    }

    public static ControlCommand generateCommand(String token, long roomId, ControlCommandEvent event) {
        return new ControlCommand("1.0", event, token, roomId, -1, -1);
    }
}
