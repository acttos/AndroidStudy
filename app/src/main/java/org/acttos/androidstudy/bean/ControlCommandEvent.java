package org.acttos.androidstudy.bean;

/**
 * Created by 天才猪 on 2018/1/14.
 */

public enum ControlCommandEvent {
    Heartbeat("Heartbeat", 0), Begin("Begin", 100), Forward("Forward", 101), Backward("Backward", 102), Left("Left", 103), Right("Right", 104), Catch("Catch", 105), CatchResult("CatchResult", 106), Quit("Quit", 107), Error("Error", 108);

    private String name;
    private int code;

    private ControlCommandEvent(String name, int code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return "NAME:" + this.name + ", CODE:" + this.code;
    }
}
