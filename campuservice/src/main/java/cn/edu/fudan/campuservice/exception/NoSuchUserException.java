package cn.edu.fudan.campuservice.exception;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(){
        super("no such user");
    }
}
