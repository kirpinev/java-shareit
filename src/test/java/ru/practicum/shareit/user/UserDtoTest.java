package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    private final String name = "Igor";

    private final String email = "igor@gmail.com";

    private final Long id = 1L;

    private final String jsonToDeserialize =
            "{\"name\":\""
                    + name
                    + "\",\"email\":\""
                    + email
                    + "\",\"id\":\""
                    + id
                    + "\"}";

    private UserDto userDto = null;

    @BeforeEach
    public void setup() {
        userDto = new UserDto(null, name, email);
    }

    @Test
    public void firstNameSerializes() throws IOException {
        assertThat(json.write(userDto))
                .extractingJsonPathStringValue("$.name")
                .isEqualTo(name);
    }

    @Test
    public void emailSerializes() throws IOException {
        assertThat(json.write(userDto))
                .extractingJsonPathStringValue("$.email")
                .isEqualTo(email);
    }

    @Test
    public void firstNameDeserializes() throws IOException {
        assertThat(this.json.parseObject(jsonToDeserialize).getName()).isEqualTo(name);
    }

    @Test
    public void emailDeserializes() throws IOException {
        assertThat(this.json.parseObject(jsonToDeserialize).getEmail()).isEqualTo(email);
    }
}
