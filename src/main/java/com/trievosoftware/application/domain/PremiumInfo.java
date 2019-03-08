package com.trievosoftware.application.domain;

import java.time.Instant;

public class PremiumInfo
{
    public final Level level;
    public final Instant until;

    public PremiumInfo(Integer level, Instant until)
    {
        this.level = Level.values()[level];
        this.until = until;
    }

    public PremiumInfo()
    {
        level = Level.NONE;
        until = null;
    }

    public String getFooterString()
    {
        if(level== Level.NONE)
            return "This server does not have Sia Pro";
        if(until.getEpochSecond()==Instant.MAX.getEpochSecond())
            return "This server has "+level.name+" permanently";
        return "This server has "+level.name+" until";
    }

    public Instant getTimestamp()
    {
        if(level== Level.NONE || until==null || until.getEpochSecond()==Instant.MAX.getEpochSecond())
            return null;
        return until;
    }
}
