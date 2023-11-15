package nl.fontys.sioux.siouxbackend.business.impl.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class CustomRegexValidator {
    public static boolean checkPhoneNumberValid(String phoneNumber){
        String regexRaw = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
        Pattern regexPattern = Pattern.compile(regexRaw);
        Matcher regexMatcher = regexPattern.matcher(phoneNumber);
        return regexMatcher.matches();
    }

    public static boolean checkLicensePlateValid(String licensePlate){
        if(licensePlate.isEmpty()){ return true; }

        String regexRaw = "^(?>[A-Z]{2}|\\d\\d)-(?>[A-Z]{2}|\\d\\d)-(?<!\\d\\d-\\d\\d-)\\d\\d$|^(?>[A-Z]{2}|\\d\\d)-(?>[A-Z]{2}|\\d\\d)-(?<![A-Z]{2}-[A-Z]{2}-)[A-Z]{2}$|^\\d\\d-[A-Z]{3}-\\d$\n";
        Pattern regexPattern = Pattern.compile(regexRaw);
        Matcher regexMatcher = regexPattern.matcher(licensePlate);
        return regexMatcher.matches();
    }
}
