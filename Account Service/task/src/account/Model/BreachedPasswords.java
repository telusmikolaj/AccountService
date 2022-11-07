package account.Model;

import java.util.Arrays;
import java.util.List;

public class BreachedPasswords {

    List<String> breachedPasswords;
    public BreachedPasswords() {
         this.breachedPasswords = Arrays.asList("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
    }


    public  boolean ifContainsBreachedPassword(String password) {
        return this.breachedPasswords.contains(password);
    }

    public List<String> getBreachedPasswords() {
        return breachedPasswords;
    }
}
