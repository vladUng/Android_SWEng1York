package com.example.i2lc.edi.backend;

/**
 * Created by vlad on 06/04/2017.
 */

public class FinalWrapper {
    Object toNonFinalise;

    /**
     * Designed as a hack around Java Lambdas. Any variable within a lambda or anonymous inner class needs to be declared final.
     * If we declare this class final, but the contents within it as usual, we can modify a variable inside lambda.
     * @param toNonFinalise Object to cheat finalisation on
     * @author Amrik Sadhra
     */
    public FinalWrapper(Object toNonFinalise){
        this.toNonFinalise = toNonFinalise;
    }

    public Object getNonFinal(){
        return toNonFinalise;
    }

    public void setNonFinal(Object value){
        toNonFinalise = value;
    }
}
