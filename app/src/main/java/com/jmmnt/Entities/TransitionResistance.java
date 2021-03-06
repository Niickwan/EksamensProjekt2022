package com.jmmnt.Entities;

public class TransitionResistance {

    /**
     * Transition resistance keeps the data for one assignment.
     */

    private double transitionResistance;

    public TransitionResistance() {
    }

    public TransitionResistance(double transitionResistance) {
        this.transitionResistance = transitionResistance;
    }

    public double getTransitionResistance() {
        return transitionResistance;
    }

    public void setTransitionResistance(double transitionResistance) {
        this.transitionResistance = transitionResistance;
    }
}
