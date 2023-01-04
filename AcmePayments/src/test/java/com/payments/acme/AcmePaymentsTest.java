package com.payments.acme;

import com.payments.acme.payment.Payment;
import com.payments.acme.payment.Range;
import com.payments.acme.payment.Rate;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcmePaymentsTest {

    private final String simpleRatesFileName = "ratesTestSimple.txt";
    private final String simpleRatesFileNameFail = "ratesTestSimpleFail.txt";
    private final String simpleRatesFileNameFailTime = "ratesTestSimpleFailTime.txt";
    private final String paymentDataWrongInput = "workFormFail.txt";
    private final String paymentDataWrongTimeInput = "workFormFailTime.txt";
    private final String paymentData = "workForm.txt";
    private final String ratesFile = "rates.txt";
    private final List<Rate> simpleRates = createSimpleRates();
    private final List<Rate> rates = createCompleteRateList();

    @Test
    public void createSimpleRatesList_whenValid_OK() throws FileNotFoundException {
        List<Rate> rates = AcmePayments.createRatesListWithFile(simpleRatesFileName) ;
        assertEquals(createSimpleRates().size(),rates.size());
        for (int i = 0; i < rates.size(); i++){
            assertEquals(simpleRates.get(i).getDayOfWeek(),rates.get(i).getDayOfWeek());
            for (int j = 0; j < rates.size(); j++){
                assertEquals(createSimpleRates().get(i).getRangesInDay().get(j).getRangeEnd(),
                                rates.get(i).getRangesInDay().get(j).getRangeEnd());
                assertEquals(createSimpleRates().get(i).getRangesInDay().get(j).getRangeStart(),
                        rates.get(i).getRangesInDay().get(j).getRangeStart());
                assertEquals(createSimpleRates().get(i).getRangesInDay().get(j).getHourlyAmount(),
                        rates.get(i).getRangesInDay().get(j).getHourlyAmount());
            }
        }

    }

    @Test
    public void createSimpleRatesList_invalidData_Fail(){
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> { List<Rate> r = AcmePayments.createRatesListWithFile(simpleRatesFileNameFail);
        });
        assertEquals("Wrong input in Rates!",exception.getMessage());
    }

    @Test
    public void createSimpleRatesList_invalidTimeData_Fail(){
        assertThrows(DateTimeParseException.class,
                () -> { List<Rate> r = AcmePayments.createRatesListWithFile(simpleRatesFileNameFailTime);
                });
    }

    @Test
    public void createPaymentList_invalidData_Fail(){
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> { List<Payment> p = AcmePayments.createPaymentsListWithFile(rates,paymentDataWrongInput);
                });
        assertEquals("Unspecified Work Day Inputted!",exception.getMessage());
    }

    @Test
    public void createPaymentList_invalidRangeData_Fail(){
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> { List<Payment> p = AcmePayments.createPaymentsListWithFile(rates,paymentDataWrongTimeInput);
                });
        assertEquals("Payment Data Range Inputted Incorrectly",exception.getMessage());
    }

    @Test
    public void createPaymentList_whenEmployeeRangeIsInMultipleRanges_Ok() throws FileNotFoundException {
        List<Payment> payments = AcmePayments.createPaymentsListWithFile(rates,paymentData);
        Payment testPayment = payments.get(0);
        assertEquals("GEORGE",testPayment.getEmployeeName());
        assertEquals(1111.0,testPayment.calculatePaymentAmount());
        assertEquals(4,testPayment.getDailyRates().get(0).getRangesInDay().size());
    }

    @Test
    public void createPaymentList_whenEmployeeRangeIsInOneRange_Ok() throws FileNotFoundException {
        List<Payment> payments = AcmePayments.createPaymentsListWithFile(rates,paymentData);
        Payment testPayment = payments.get(0);
        assertEquals("GEORGE",testPayment.getEmployeeName());
        assertEquals(1111.0,testPayment.calculatePaymentAmount());
        assertEquals(1,testPayment.getDailyRates().get(1).getRangesInDay().size());
    }



    public final List<Rate> createCompleteRateList() {
        try {
            return AcmePayments.createRatesListWithFile(ratesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public final List<Rate> createSimpleRates (){
        return List.of(
                new Rate(
                        "MO",
                        List.of(
                                new Range(
                                        List.of("00:00","09:00","25")
                                ),
                                new Range(
                                        List.of("09:00","18:00","15")
                                ),
                                new Range(
                                        List.of("18:00","23:59","20")
                                )
                        )
                ),
                new Rate(
                        "TU",
                        List.of(
                                new Range(
                                        List.of("00:00","09:00","25")
                                ),
                                new Range(
                                        List.of("09:00","18:00","15")
                                ),
                                new Range(
                                        List.of("18:00","23:59","20")
                                )
                        )
                )
        );
    }
}
