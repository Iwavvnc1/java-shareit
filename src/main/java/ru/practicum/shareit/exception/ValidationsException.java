package ru.practicum.shareit.exception;

public class ValidationsException extends RuntimeException {
    private final String parameter;

    public ValidationsException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}