package com.kaseka.boxmaptest1.global;


public enum Constants {
    RECUEST_URL ("https://maps.googleapis.com/maps/api/directions/json?");

    private final String value;

    private Constants(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
