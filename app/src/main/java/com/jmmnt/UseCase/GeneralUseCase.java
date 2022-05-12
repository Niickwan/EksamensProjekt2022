package com.jmmnt.UseCase;

public class GeneralUseCase {

    //This method takes two strings and checks if one of the strings (s)
    //Contains the same chars as the other string (input)
    //The method then returns true if they do contain the same chars, and false if they do not.
    public boolean checkIfStringMatchesInput(String s, String input){
        return s.toLowerCase().matches("(.*)" + input.toLowerCase() + "(.*)");
    }

    private Boolean checkIfNumber(String text, int lengthOfNumber) {
        return text.matches("[0-9]+") && text.length() == lengthOfNumber;
    }

    private Boolean checkIfLetters(String text){
        return !text.matches(".*[0-9].*");
    }

    private Boolean checkIfEmail(String text){
        String emailTjek = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        return text.matches(emailTjek);
    }


}
