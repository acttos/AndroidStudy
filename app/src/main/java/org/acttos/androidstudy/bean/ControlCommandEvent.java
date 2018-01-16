package org.acttos.androidstudy.bean;

/**
 * Created by 天才猪 on 2018/1/14.
 */

public enum ControlCommandEvent {
    Heartbeat(0), Begin(100), Forward(101), Backward(102), Left(103), Right(104), Catch(105), CatchResult(106), Quit(107), Error(108), None(-1);

    private int code;

    ControlCommandEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name();
    }
}
