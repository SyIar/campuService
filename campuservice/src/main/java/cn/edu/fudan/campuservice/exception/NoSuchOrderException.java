package cn.edu.fudan.campuservice.exception;

public class NoSuchOrderException extends Exception {
    public NoSuchOrderException() {
        super("no such order");
    }
}
