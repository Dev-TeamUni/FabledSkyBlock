package com.songoda.skyblock.upgrade;

public class Upgrade {

    private double cost;
    private int value;
    private String strValue;
    private boolean enabled = true;

    public Upgrade(double cost) {
        this.cost = cost;
    }

    public Upgrade(double cost, int value) {
        this.cost = cost;
        this.value = value;
    }

    public Upgrade(double cost, String value) {
        this.cost = cost;
        this.strValue = value;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public enum Type {

        Crop, Spawner, Fly, Drops, Size, Speed, Jump, Members, Hoppers, Generator

    }
}
