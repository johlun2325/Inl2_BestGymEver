package BestGymEver2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

public class RunProgram {

    //Två strängar till pathsen.
    private final String allPastClientsFile = "src/BestGymEver2/clients.txt";
    private final String workoutStatisticsPayingClients = "src/BestGymEver2/clientsWorkouts.txt";

    // 2 paths för läsning respektive skapande och skrivning till
     private final Path pathClientsFile = Paths.get(allPastClientsFile);
    private final Path pathWritePayingClients = Paths.get(workoutStatisticsPayingClients);

    //två listor för hantering av kunddata
    private List<String> listOfClients;
    private List<String> foundClient;


    public void run() {
        Validate v = new Validate();
        RunProgram r = new RunProgram();

        //variables used
        boolean isTest = false;
        boolean isValid = false;
        boolean containsDigits;

        String testValueNotUsing = "nothing";
        String validMsg = "Personen är en giltig kund. Giltigt betalningsdatum.";
        String notValidMsg = "Personen är fd kund. Ogiltigt betalningsdatum.";
        String notAClientMsg = "Personen är obehörig.";
        String tooShortMsg = "Personnumret är av ogiltigt format";
        String errNull = "Kunde inte kontrollera datum, datum finns inte för kunden.";
        String errWrongFormat = "Du har skrivit in ogiltigt tecken";
        String userInput = "";


        // sparar filens innehåll till en lista
        r.listOfClients = v.readFromFileToList(r.pathClientsFile);

        do {
            //frågar efter och hämtar sökning efter person - tom = no valid inputMSG

            try {
                userInput = v.getSearchInput(isTest, testValueNotUsing);
            }
            catch(NoSuchElementException e){
                System.out.println(errWrongFormat);
            }
            //kollar om sökord innehåller minst en siffra
            containsDigits = v.checkIfStringContainsDigits(userInput);

            //villkorssats som delar koden om innehåller siffror eller inte och sedan kollar längden för innehåller siffror
            if (!containsDigits) {
                r.foundClient = v.findClientInList(userInput.trim(), r.listOfClients);
            } else {
                if (v.checkLength(userInput)) {
                    r.foundClient = v.findClientInList(userInput.trim(), r.listOfClients);
                } else {
                    System.out.println(tooShortMsg);
                    continue;
                }
            }

            if (v.clientFound) {
                //parsar och validerar om betalningsdatum är och sparar booleanvärde
                try {
                    isValid = v.validatePaymentDate(v.parseToLocalDate(r.foundClient.get(2)));
                } catch (NullPointerException e) {
                    System.out.println(errNull);
                    e.printStackTrace();
                }

                if (isValid) {
                    System.out.println(validMsg);
                    v.printPayingClientToFile(r.foundClient, r.pathWritePayingClients);
                } else {
                    System.out.println(notValidMsg);
                }
            } else {
                System.out.println(notAClientMsg);
            }
        } while(!v.clientFound);
    }
}
