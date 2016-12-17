package com.kaseka.boxmaptest1.global;



public enum DayOfWeek {
    MONDAY(1, "pon."),
    TUESDAY(2, "wt."),
    WENSDAY(3, "śr."),
    THURSDAY(4, "czw."),
    FRIDAY(5, "pt."),
    SATURDAY(6, "sob."),
    SUNDAY(7, "niedz.");

    private int numberOfWeekDay;
    private String shortNameOfWeekDay;

    //prywatny konstruktor emun
    private DayOfWeek(int numberOfWeekDay, String shortNameOfWeekDay){
        this.numberOfWeekDay = numberOfWeekDay;
        this.shortNameOfWeekDay = shortNameOfWeekDay;
    }

    
}
