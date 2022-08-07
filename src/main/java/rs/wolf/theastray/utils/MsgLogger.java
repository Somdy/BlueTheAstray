package rs.wolf.theastray.utils;

import rs.lazymankits.LMDebug;

import java.util.ArrayList;
import java.util.List;

public class MsgLogger {
    private static final List<String> Msg = new ArrayList<>();
    private static StringBuilder MSG = new StringBuilder();
    private static StringBuilder MsgBuilder;
    
    public static void PreLoad(String title) {
        if (MSG == null) MSG = new StringBuilder();
        MsgBuilder = new StringBuilder();
        Msg.clear();
        MsgBuilder.append(title).append(" { ");
    }
    
    public static void PreLoad() {
        PreLoad("");
    }
    
    public static void Append(String msg) {
        Msg.add(msg);
    }
    
    public static void Append(int msg) {
        Msg.add(Integer.toString(msg));
    }
    
    public static void End() {
        if (!Msg.isEmpty()) {
            for (int i = 0; i < Msg.size(); i++) {
                MsgBuilder.append(Msg.get(i));
                if (i != Msg.size() - 1)
                    MsgBuilder.append(", ");
            }
        }
        MsgBuilder.append(" }");
        MSG.append(MsgBuilder.toString()).append("//");
        MsgBuilder = null;
    }
    
    public static void Log() {
        String[] msg = MSG.toString().split("//");
        MSG = null;
        if (msg.length > 0) {
            for (String s : msg) {
                if (!s.isEmpty()) {
                    LMDebug.deLog(MsgLogger.class, "THE ASTRAY-[MSG]> " + s);
                }
            }
        }
    }
}