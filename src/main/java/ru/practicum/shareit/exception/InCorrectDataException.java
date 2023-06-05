package ru.practicum.shareit.exception;

public class InCorrectDataException extends RuntimeException {
    private final String parameter;

    public InCorrectDataException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
