package com.technocreatives.rdegelo.creativeremotecontroller.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rdegelo on 12/04/2017.
 */

public class Sequence {
    private LinkedList<Command> commands;
    private String title;

    public LinkedList<Command> getCommands() {
        return commands;
    }

    public void setCommands(LinkedList<Command> cmd) {
        commands = cmd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Sequence(String title) {
        setTitle(title);
        setCommands(new LinkedList<Command>());
    }
}
