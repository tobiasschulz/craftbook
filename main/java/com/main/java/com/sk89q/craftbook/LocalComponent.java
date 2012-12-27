package com.sk89q.craftbook;
/**
 * Author: Turtle9598
 */
public abstract class LocalComponent {

    private static LocalComponent localComponent;

    public LocalComponent() {

        localComponent = this;
    }

    public static LocalComponent inst() {

        return localComponent;
    }

    public abstract void enable();

    public abstract void disable();
}
