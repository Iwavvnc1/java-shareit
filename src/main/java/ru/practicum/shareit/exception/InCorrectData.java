package ru.practicum.shareit.exception;

public class InCorrectData extends RuntimeException {
    private final String parameter;

    public InCorrectData(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
