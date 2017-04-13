package com.technocreatives.rdegelo.creativeremotecontroller.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rdegelo on 12/04/2017.
 */

public class Settings {
    public static final String PREFS_NAME = "CRC_PREFS";
    public static final String PREFS_USE_BROADCAST_NAME = "USE_BROADCAST";
    public static final String PREFS_IP_NAME = "IP";
    public static final String PREFS_PORT_NAME = "PORT";
    public static final String PREFS_SEQUENCIES_NAME = "SEQUENCIES";

    private List<Sequence> sequencies;
    private boolean use_broadcast;
    private String ip;
    private int port;

    public void Load(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, 0);
        setUse_broadcast(prefs.getBoolean(PREFS_USE_BROADCAST_NAME, false));
        setIp(prefs.getString(PREFS_IP_NAME, ""));
        setPort(prefs.getInt(PREFS_PORT_NAME, 2593));

        Type listType = new TypeToken<ArrayList<Sequence>>() {
        }.getType();
        //setSequencies((List<Sequence>)new Gson().fromJson(prefs.getString(PREFS_SEQUENCIES_NAME, null), listType));

        List<Command> commands = new ArrayList<>();
        commands.add(new Command("start video1", 0));
        commands.add(new Command("stop video1", 500));
        commands.add(new Command("start video2", 0));
        commands.add(new Command("start fade", 1200));
        commands.add(new Command("stop fade", 200));
        commands.add(new Command("stop video2", 0));

        Sequence s1 = new Sequence("Sequence 1");
        s1.setCommands(commands);

        Sequence s2 = new Sequence("Sequence 2");
        s2.setCommands(commands);

        Sequence s3 = new Sequence("Sequence 3");
        s3.setCommands(commands);

        List<Sequence> sequencies = new ArrayList<Sequence>();
        sequencies.add(s1);
        sequencies.add(s2);
        sequencies.add(s3);

        setSequencies(sequencies);
    }

    public void Save(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(PREFS_USE_BROADCAST_NAME, isUse_broadcast());
        editor.putString(PREFS_IP_NAME, getIp());
        editor.putInt(PREFS_PORT_NAME, getPort());
        editor.putString(PREFS_SEQUENCIES_NAME, new Gson().toJson(sequencies));

        editor.commit();
    }

    public List<Sequence> getSequencies() {
        return sequencies;
    }

    public void setSequencies(List<Sequence> sequencies) {
        this.sequencies = sequencies;
    }

    public boolean isUse_broadcast() {
        return use_broadcast;
    }

    public void setUse_broadcast(boolean use_broadcast) {
        this.use_broadcast = use_broadcast;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
