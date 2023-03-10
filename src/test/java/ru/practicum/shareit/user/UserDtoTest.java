package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    private final ObjectMapper mapper = new ObjectMapper();

    private final UserDto userDto = new UserDto(
            1L,
            "igor",
            "igor@gmail.com");

    @Test
    void testSerialize() throws Exception {

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("igor");
        assertThat(result).hasJsonPathStringValue("$.email");
        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo("igor@gmail.com");
    }

    @Test
    public void testDeserialize() throws Exception {
        mapper.findAndRegisterModules();

        String jsonContent = mapper.writeValueAsString(userDto);
        UserDto result = json.parse(jsonContent).getObject();

        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
    }
}
