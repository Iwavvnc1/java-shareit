package ru.practicum.shareit.exception;

public class Validations extends RuntimeException {
    private final String parameter;

    public Validations(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}