package kg.itacademy.xakaton.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAuthModel {

    @NotBlank(message = "Login can't be blank")
    String login;

    @NotBlank(message = "Password can't be blank")
    String password;
}
