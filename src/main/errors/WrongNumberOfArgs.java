package main.errors;

public class WrongNumberOfArgs extends Exception {
    public WrongNumberOfArgs(String reason) {
        super(reason);
    }
}
