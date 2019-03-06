package com.trievosoftware.application.domain;

/**
 * @author marktripoli (Triippz)
 *
 * The membership levels for premium service
 */
public enum Level
{
    NONE("No Premium"),
    BASIC("Sia Basic"),
    PRO("Sia Pro"),
    ULTRA("Sia Ultra");

    public final String name;

    private Level(String name)
    {
        this.name = name;
    }

    public boolean isAtLeast(Level other)
    {
        return ordinal() >= other.ordinal();
    }
}
