package com.trievosoftware.application.domain;

import com.trievosoftware.discord.Action;
import com.trievosoftware.discord.Constants;

public class Punishment
{
    public final Action action;
    public final int numStrikes;
    public final int time;
    public static final int MAX_STRIKES = 100;
    public static final int MAX_SET = 20;
    public static final String DEFAULT_SETUP_MESSAGE = "\n" + Constants.WARNING + " It looks like you've set up some " +
        "automoderation without assigning any punishments! "
        + "I've gone ahead and set up some default punishments; you can see the settings with `" + Constants.PREFIX
        + "settings` and set or change any punishments with the `" + Constants.PREFIX+"punishment` command!";

    public Punishment(Integer action, Integer numStrikes, Integer time)
    {
        this.action = Action.fromBit(action);
        this.numStrikes = numStrikes;
        this.time = time;
    }
}
