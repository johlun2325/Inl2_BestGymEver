package BestGymEver2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Validate {
    boolean clientFound = false;

    protected String errFileNotFound = "Filen kunde inte hittas";
    protected String errIOProblem = "Filen kunde inte läsas";
    protected String errCouldNotWrite = "Kunde inte skriva till filen, innehåll saknas";
    protected String promptToUser = "Skriv in fullständigt namn eller personnummer(10 siffror):  ";
    protected String noValidInput = "Du har inte angett ett giltigt sökord";


    public List<String> readFromFileToList(Path path) {
        List<String> listOfPayingClients = new ArrayList<>();
//        String temp;

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            while (true) {

                String line = reader.readLine();

                if (line == null)
                    break;

                listOfPayingClients.add(line);
            }

        } catch (FileNotFoundException e) {
            System.out.println(errFileNotFound);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(errIOProblem);
            e.printStackTrace();
        }
        return listOfPayingClients;
    }

    public String getSearchInput(boolean isTest, String testValue) throws NoSuchElementException{
        Scanner sc;
        String input;

        if (isTest) {
            sc = new Scanner(testValue);
        } else {
            sc = new Scanner(System.in);
            System.out.println(promptToUser);
        }
         input = sc.nextLine();

        if (input.isBlank())
            System.out.println(noValidInput);

        return input;
    }

    public boolean checkIfStringContainsDigits(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean checkLength(String inputToCheck) {
        if (inputToCheck.length() != 10)
            return false;
        return true;
    }

    public List<String> findClientInList(String searchWord, List<String> listOfClients) {
        List<String> client = new ArrayList<>();
        String[] temp;

        for (int i = 0; i < listOfClients.size(); i++) {
            if (listOfClients.get(i).contains(",")) {
                temp = listOfClients.get(i).split(",");
                temp[0] = temp[0].trim();
                temp[1] = temp[1].trim();

                if (searchWord.trim().equalsIgnoreCase(temp[0]) ||
                        searchWord.trim().equalsIgnoreCase(temp[1])) {
                    client.add(temp[0]);
                    client.add(temp[1]);
                    if (i + 1 < listOfClients.size())
                        client.add(listOfClients.get(i + 1));
                    clientFound = true;
                }
            }
        }
        return client;
    }

    public LocalDate parseToLocalDate(String listValue) throws NullPointerException{
        LocalDate dateOfPayment = null;
        if(listValue == null)
            throw new NullPointerException();

        try {
            dateOfPayment = LocalDate.parse(listValue);
        } catch (Exception e) {
            System.out.println("Could not parse String-date to LocalDate");
            e.printStackTrace();
        }
        return dateOfPayment;
    }

    public boolean validatePaymentDate(LocalDate clientDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate oneYearAgo = currentDate.minusYears(1);

        if (!clientDate.isBefore(oneYearAgo) && !clientDate.isAfter(currentDate)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean printPayingClientToFile(List<String> client, Path path) {
        boolean hasWritten = false;
        String first = client.get(0);
        String second = client.get(1);

        try (BufferedWriter bw = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            if (!first.isBlank() && !second.isBlank()) {
                if (Files.exists(path)) {
                    bw.append(first).append(" ").append(second).append(", workout date: ").append(LocalDate.now().toString());
                    bw.newLine();
                    hasWritten = true;
                }
            } else {
                System.out.println(errCouldNotWrite);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hasWritten;
    }

    public void printWorkoutStatistics(List<String> workoutList){
        for (String line: workoutList){
            System.out.println(line);
        }
    }
}

