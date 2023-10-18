package BestGymEver2;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidateTest {

    List<String> tempListOfClients = new ArrayList<>();
    Path pr = Paths.get("Test/BestGymEver2/clientsTestFile.txt");
    Path pw = Paths.get("Test/BestGymEver2/payingClientsTestFile.txt");

    Validate v = new Validate();

    @Test
    void readFromFileToListTest() {
        tempListOfClients = v.readFromFileToList(pr);
        int expectedLength = 10;
        int actualLength = tempListOfClients.size();
        assertEquals(expectedLength, actualLength);

    }

    @Test
    void getSearchInput(){
        boolean isTest = true;
        String userMockDataWrong = "Pippi";
        String userMockData = "Greger Ganache";
        String actual = v.getSearchInput(isTest, userMockData);
        String actualWrong = v.getSearchInput(isTest, userMockDataWrong);
        assertEquals(userMockData, actual);
        assertEquals(userMockDataWrong, actualWrong);

    }

    @Test
    public void checkIfStringContainsDigitsTest(){
        String digits = "123";
        String characters = "abc";
        assertTrue(v.checkIfStringContainsDigits(digits));
        assertFalse(v.checkIfStringContainsDigits(characters));
    }

    @Test
    public void checkLengthTest(){
        String s1= "123";
        String s2 = "asd2asd";
        String s3 =  "1231231234";
        assertFalse(v.checkLength(s1));
        assertFalse(v.checkLength(s2));
        assertTrue(v.checkLength(s3));
    }

    @Test
    void findClientInListTest() {
        tempListOfClients.add("198801030000, Johanna Lundström");
        tempListOfClients.add("Rad2 Johanna");
        tempListOfClients.add("198401030000, Hampus Karlsson");
        tempListOfClients.add("Rad2 Hampus");
        tempListOfClients.add("198701030000, Pippi Långstrump");
        tempListOfClients.add("Rad2 datum");
        tempListOfClients.add("198901030000, Martin Lundgren");
        tempListOfClients.add("Rad2 Martin");

        String searchWord = "Pippi Långstrump";

        List<String> client = v.findClientInList(searchWord, tempListOfClients);
        List<String> expected = new ArrayList<>();
        expected.add("198701030000");
        expected.add("Pippi Långstrump");
        expected.add("Rad2 datum");

        assertEquals(expected.get(0), client.get(0));
        assertEquals(expected.get(1), client.get(1));
        assertEquals(expected.get(2), client.get(2));

    }

    @Test
    void parseToLocalDateTest() {

        List<String> listWithDate = new ArrayList<>();
        listWithDate.add("not a date");
        listWithDate.add("not a date either");
        listWithDate.add("2022-11-07");
        listWithDate.add(null);
        LocalDate expectedDate = LocalDate.of(2022, 11, 7);
        LocalDate d = v.parseToLocalDate(listWithDate.get(2));
        assertTrue(expectedDate.isEqual(d));
        assertThrows(NullPointerException.class, () -> v.parseToLocalDate(listWithDate.get(3)));
    }

    @Test
    void validatePaymentDateTest() {
        LocalDate valid = LocalDate.now();
        LocalDate alsoValid = LocalDate.now().minusDays(1);
        LocalDate alsoNotValid = LocalDate.now().plusDays(1);
        LocalDate notValid = LocalDate.now().minusYears(1).minusMonths(1);
        LocalDate almostValid = LocalDate.now().minusYears(1).minusDays(1);

        assertTrue(v.validatePaymentDate(valid));
        assertTrue(v.validatePaymentDate(alsoValid));
        assertFalse(v.validatePaymentDate(alsoNotValid));
        assertFalse(v.validatePaymentDate(notValid));
        assertFalse(v.validatePaymentDate(almostValid));

    }

    @Test
    void printPayingClientToFileTest() {
        List<String> client = new ArrayList<>();
        client.add("198701030000");
        client.add("Pippi Långstrump");
        client.add("Rad2 datum");

        boolean hasWritten = false;
        hasWritten = v.printPayingClientToFile(client, pw);
        assertTrue(hasWritten);
    }

}