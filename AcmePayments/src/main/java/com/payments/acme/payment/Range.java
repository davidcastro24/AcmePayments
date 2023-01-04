package com.payments.acme.payment;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Range {

    private LocalTime rangeStart;
    private LocalTime rangeEnd;
    private Integer hourlyAmount;

    public Range(LocalTime rangeStart, LocalTime rangeEnd, Integer hourlyAmount) {
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.hourlyAmount = hourlyAmount;
    }

    public Range(List<String> data){
        setRangeStart(data.get(0));
        setRangeEnd(data.get(1));
        this.hourlyAmount = Integer.parseInt(data.get(2));
    }

    public LocalTime getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(LocalTime rangeStart) {
        this.rangeStart = rangeStart;
    }

    public LocalTime getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(LocalTime rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public void setRangeStart(String rangeStartString){
        this.rangeStart = LocalTime.parse(rangeStartString);
    }

    public void setRangeEnd(String rangeEndString){
        this.rangeEnd = LocalTime.parse(rangeEndString);
    }
    public Integer getHourlyAmount() {
        return hourlyAmount;
    }

    public void setHourlyAmount(Integer hourlyAmount) {
        this.hourlyAmount = hourlyAmount;
    }

    public long amountInRange(LocalTime time, LocalTime time2){
          if (isInRange(time)){
            if(rangeEnd.isAfter(time2) || rangeEnd.equals(time2)){
                return time.until(time2, ChronoUnit.HOURS);
            }else{
                return time.until(rangeEnd,ChronoUnit.HOURS);
            }
        }
        return 0;
    }

    public boolean isInRange(LocalTime time){
        return (rangeStart.isBefore(time) || rangeStart.equals(time)) && rangeEnd.isAfter(time);
    }

    @Override
    public String toString() {
        return "Range{" +
                "Start =" + rangeStart + '\n' +
                "End =" + rangeEnd + '\n' +
                "Hourly Amount=" + hourlyAmount + '\n' +
                '}';
    }

    public Range() {

    }
}
