package main.errors;

public class WrongReplacementPolicy extends Exception {
    public WrongReplacementPolicy(String reason) {
        super(reason);
    }
}
