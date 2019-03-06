package com.trievosoftware.application.domain;

import com.trievosoftware.discord.Action;

public class Punishment
{
    public final Action action;
    public final int numStrikes;
    public final int time;

    public Punishment(Integer action, Integer numStrikes, Integer time)
    {
        this.action = Action.fromBit(action);
        this.numStrikes = numStrikes;
        this.time = time;
    }
}
