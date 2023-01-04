package com.payments.acme.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rate {

    private String dayOfWeek;
    private List<Range> rangesInDay;

    public Rate(String dayOfWeek, List<Range> rangesInDay) {
        this.dayOfWeek = dayOfWeek;
        this.rangesInDay = rangesInDay;
    }

    public void addRangeWithHourlyAmount(Range range){
        rangesInDay.add(range);
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<Range> getRangesInDay() {
        return rangesInDay;
    }

    public void setRangesInDay(List<Range> rangesInDay) {
        this.rangesInDay = rangesInDay;
    }

    public String getCompleteDayOfWeek(){
        Map<String,String> daysLookup = new HashMap<>();
        daysLookup.put("MO","Monday");
        daysLookup.put("TU","Tuesday");
        daysLookup.put("WE","Wednesday");
        daysLookup.put("TH","Thursday");
        daysLookup.put("FR","Friday");
        daysLookup.put("SA","Saturday");
        daysLookup.put("SU","Sunday");
        return daysLookup.get(dayOfWeek);
    }

    @Override
    public String toString() {
        return "Rate{" +
                "Day of Week Code ='" + dayOfWeek + '\n' +
                "Daily Ranges =" + rangesInDay + '\n' +
                '}';
    }
}
