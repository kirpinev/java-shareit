package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum State {
    ALL,
    CURRENT,
    FUTURE,
    WAITING,
    REJECTED,
    PAST;

    public static Optional<State> from(String stateString) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stateString)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
