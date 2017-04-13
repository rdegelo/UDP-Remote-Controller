package com.technocreatives.rdegelo.creativeremotecontroller.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rdegelo on 12/04/2017.
 */

public class Sequence {
    private List<Command> commands;
    private String title;

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> cmd) {
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
        setCommands(new ArrayList<Command>());
    }
}
