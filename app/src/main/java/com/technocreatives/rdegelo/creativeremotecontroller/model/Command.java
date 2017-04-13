package com.technocreatives.rdegelo.creativeremotecontroller.model;

/**
 * Created by rdegelo on 13/04/2017.
 */

public class Command {
    private String text;
    private int delayBefore;

    public Command() {

    }

    public Command(String text, int delayBefore) {
        setText(text);
        setDelayBefore(delayBefore);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDelayBefore() {
        return delayBefore;
    }

    public void setDelayBefore(int delayBefore) {
        this.delayBefore = delayBefore;
    }
}
