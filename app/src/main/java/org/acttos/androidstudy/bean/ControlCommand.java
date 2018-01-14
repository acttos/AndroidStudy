package org.acttos.androidstudy.bean;

/**
 * Created by 天才猪 on 2018/1/14.
 */

/**
 * 入参：
 名称	类型	必传	描述
 protocol
 string

 是	协议版本
 event	i32	是	指令
 token	string	是	用户token
 roomId	i64	是	房间号
 出参：
 名称	类型	必传	描述
 protocol	string	是	协议版本
 event	i32	是	指令
 status	i32	是	0成功，1失败，2娃娃机故障
 catchCount	i32	否	event=106时返回，抓到wawa数量，0为抓中 1抓中一只

 */
public class ControlCommand {
    private String protocol;
    private ControlCommandEvent event;
    private String token;
    private long roomId;
    private int status;

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

    private int catchCount;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
