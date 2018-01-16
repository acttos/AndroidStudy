package org.acttos.androidstudy.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 天才猪 on 2018/1/14.
 */

public class ControlCommand {
    private String protocol;
    private ControlCommandEvent event;
    private String token;
    private long roomId;
    private int status;
    private int catchCount;

    public ControlCommand(String protocol, ControlCommandEvent event, String token, long roomId, int status, int catchCount) {
        this.protocol = protocol;
        this.event = event;
        this.token = token;
        this.roomId = roomId;
        this.status = status;
        this.catchCount = catchCount;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public ControlCommandEvent getEvent() {
        return event;
    }

    public void setEvent(ControlCommandEvent event) {
        this.event = event;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCatchCount() {
        return catchCount;
    }

    public void setCatchCount(int catchCount) {
        this.catchCount = catchCount;
    }


    public String JSONString() {
        JSONObject object = new JSONObject();

        try {
            object.put("protocol", this.protocol);
            object.put("event", this.event);
            object.put("token", this.token);
            object.put("roomId", this.roomId);
            object.put("status", this.status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }
    public static ControlCommand generateCommand(String token, long roomId, ControlCommandEvent event) {
        return new ControlCommand("1.0", event, token, roomId, -1, -1);
    }
}
