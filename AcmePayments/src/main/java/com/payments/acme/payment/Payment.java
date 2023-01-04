package com.payments.acme.payment;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Payment {
    private String employeeName;
    private List<Rate> dailyRates;

    public Payment(String employeeName, List<Rate> dailyRates) {
        this.employeeName = employeeName;
        this.dailyRates = dailyRates;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public List<Rate> getDailyRates() {
        return dailyRates;
    }

    public void setDailyRates(List<Rate> dailyRates) {
        this.dailyRates = dailyRates;
    }

    public double calculatePaymentAmount() {
        return dailyRates.stream()
                .map(Rate::getRangesInDay)
                .flatMap(Collection::stream)
                .toList()
                .stream()
                .mapToDouble(r -> r.getHourlyAmount() * (r.getRangeStart().until(r.getRangeEnd(), ChronoUnit.HOURS)))
                .sum();
    }

    @Override
    public String toString() {
        String retVal = employeeName + "'s Payment" + '\n';
        retVal += dailyRates.stream().map(rate -> '\n' + "Daily Rate " + rate.getCompleteDayOfWeek() + '\n' + "Ranges in Day " + '\n'+
                        rate.getRangesInDay()
                                .stream()
                                .map(range -> "Start : " + range.getRangeStart()
                                        + " End : " + range.getRangeEnd() + " Hourly Amount : " + range.getHourlyAmount() + " \n")
                                .reduce("", String::concat))
                .reduce("", String::concat);
        retVal += '\n' + "Total Payment Amount " + calculatePaymentAmount() +'\n';
        return retVal;
    }
}
