package com.payments.acme;

import com.payments.acme.payment.Payment;
import com.payments.acme.payment.Range;
import com.payments.acme.payment.Rate;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class AcmePayments {

    enum daysInWeek {
        MO,
        TU,
        WE,
        TH,
        FR,
        SA,
        SU
    }

    public static void main(String[] args) {
        try {
            List<Rate> rates = createRatesListWithFile("rates.txt");
            createPaymentsListWithFile(rates, "workForm.txt").forEach(System.out::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Scanner OpenFileAndRetrieveData(String fileName) {
        InputStream fileLocation = AcmePayments.class.getClassLoader()
                .getResourceAsStream(fileName);
        return new Scanner(fileLocation);
    }

    public static boolean isDayOfWeek(String data) {
        return Arrays.stream(AcmePayments.daysInWeek.values())
                .anyMatch(day -> day.name().equals(data));
    }

    public static List<String> getDelimitedData(String data) {
        String[] splitAmount = data.split("\\|");
        String[] splitRange = splitAmount[0].split("-");
        if (splitRange.length != 2)
            throw new RuntimeException("Data inputted incorrectly!");
        return List.of(splitRange[0], splitRange[1], splitAmount[1]);
    }

    public static Optional<Rate> getRateFromDay(String day, List<Rate> rates) {
        return rates.stream()
                .filter(rate -> rate.getDayOfWeek().equals(day))
                .findFirst();
    }

    public static Optional<Range> returnRangeInWorkHours(String day, LocalTime start, List<Rate> rates) {
        Optional<Rate> rate = getRateFromDay(day, rates);
        return rate.flatMap(rt -> rt
                .getRangesInDay()
                .stream()
                .filter(range -> range.isInRange(start))
                .findFirst());

    }

    public static List<Rate> createRatesListWithFile(String fileName) throws FileNotFoundException, RuntimeException {
        List<Rate> rates = new ArrayList<>();
        List<Range> ranges = new ArrayList<>();
        String dayOfWeekHead = "";

        Scanner reader = OpenFileAndRetrieveData(fileName);
        while (reader.hasNextLine()) {
            String data = reader
                    .nextLine()
                    .replaceAll("\\s", "")
                    .toUpperCase();
            if (isDayOfWeek(data)) {
                if (!ranges.isEmpty()) {
                    rates.add(new Rate(dayOfWeekHead, ranges));
                    ranges = new ArrayList<>();
                }
                dayOfWeekHead = data;
            } else {
                ranges.add(new Range(getDelimitedData(data)));
            }
            if (dayOfWeekHead.isEmpty())
                throw new RuntimeException("Wrong input in Rates!");
        }
        rates.add(new Rate(dayOfWeekHead, ranges));

        return rates;
    }

    public static List<String> getDelimitedDataPayment(String data) {
        String day = data.substring(0, 2);
        if (!isDayOfWeek(day))
            throw new RuntimeException("Unspecified Work Day Inputted!");
        String[] range = data.replace(day, "").split("-");
        if (range.length != 2)
            throw new RuntimeException("Payment Data Range Inputted Incorrectly");
        return List.of(day, range[0], range[1]);
    }

    public static Rate generatePaymentData(List<String> delimitedData, List<Rate> rates) {
        List<Range> rangesInDay = new ArrayList<>();
        String day = delimitedData.get(0);
        LocalTime startTime = LocalTime.parse(delimitedData.get(1));
        LocalTime endTime = LocalTime.parse(delimitedData.get(2));
        long timeWorked = startTime.until(endTime, ChronoUnit.HOURS);
        while (timeWorked > 0) {
            Optional<Range> matchedRange = returnRangeInWorkHours(day, startTime, rates);

            if (matchedRange.isEmpty())
                throw new RuntimeException("Mismatch in inputted range and employee work hours");

            long diffInTime = matchedRange.get().amountInRange(startTime, endTime);
            timeWorked -= diffInTime;
            LocalTime newEndTime = timeWorked <= 0 ? endTime : matchedRange.get().getRangeEnd();
            rangesInDay.add(new Range(
                    startTime,
                    newEndTime,
                    matchedRange.get().getHourlyAmount()
            ));
            startTime = timeWorked > 0 ? newEndTime : startTime;
        }
        return new Rate(day, rangesInDay);
    }

    public static List<Payment> createPaymentsListWithFile(List<Rate> rates, String fileName) throws FileNotFoundException {
        List<Payment> payments = new ArrayList<>();

        Scanner reader = OpenFileAndRetrieveData(fileName);
        while (reader.hasNextLine()) {
            String data = reader.nextLine().replaceAll("\\s", "").toUpperCase();
            String employeeName = data.split("=")[0];
            String worked = data.split("=")[1];
            String[] hoursPerDay = worked.split(",");
            List<Rate> ratesPerEmployee = new ArrayList<>();
            for (String hoursWorked : hoursPerDay) {
                ratesPerEmployee.add(generatePaymentData(
                        getDelimitedDataPayment(hoursWorked),
                        rates
                ));
            }
            payments.add(new Payment(
                    employeeName,
                    ratesPerEmployee
            ));
        }

        return payments;
    }
}
