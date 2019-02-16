package hr.java.vjezbe.validator;

import hr.java.vjezbe.model.VrstaMjesta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Scanner;

public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class.getName());

    public static BigDecimal isBigDecimal(Scanner scanner) {
        while (!scanner.hasNextBigDecimal()) {
            LOGGER.error("Uneseni tip podatka nije BigDecimal");
            System.out.println("Unos mora biti broj!");
            scanner.nextLine();
        }
        return scanner.nextBigDecimal();
    }

    public static int isInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            LOGGER.error("Uneseni tip podatka nije int");
            System.out.println("Unos mora biti broj!");
            scanner.nextLine();
        }
        return scanner.nextInt();
    }

    public static String isNullOrEmpty(Scanner scanner) {
        while (scanner.nextLine() == null || scanner.nextLine().isEmpty()) {
            LOGGER.error("Uneseni tip podatka (String) ne smije biti null or empty");
            System.out.println("Unos ne smije biti prazan");
        }
        return scanner.nextLine();
    }

    public static VrstaMjesta isVrstaMjesta(Scanner scanner){
        while(!scanner.hasNextInt()){
            LOGGER.error("Neispravan odabir vrste mjesta");
            System.out.println("Neispravan unos! Odabirite jedan od ponuđenih brojeva");
            System.out.println("Odabir vrste mjesta >> ");
            scanner.nextLine();
        }

        int rbVrsteMjesta = scanner.nextInt();
        if (rbVrsteMjesta >= 1 && rbVrsteMjesta < VrstaMjesta.values().length) {
            scanner.nextLine();
            return VrstaMjesta.values()[rbVrsteMjesta - 1];
        } else {
            scanner.nextLine();
            return VrstaMjesta.OSTALO;
        }
    }
}
