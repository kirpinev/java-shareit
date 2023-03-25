package ru.practicum.shareit.exception;

public class IncorrectStateException extends IllegalArgumentException {
    public IncorrectStateException(String s) {
        super(s);
    }
}
