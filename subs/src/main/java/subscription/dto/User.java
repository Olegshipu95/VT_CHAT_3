package subscription.dto;

import subscription.utils.ErrorMessages;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class User {

    @Id
    @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
    @Column(name = "id")
    private UUID id;

    @NotBlank(message = "Name can't be blank")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Surname can't be blank")
    @Column(name = "surname")
    private String surname;

    @NotBlank(message = ErrorMessages.EMAIL_CANNOT_BE_NULL)
    @Column(name = "email")
    private String email;

    @Column(name = "brief_description")
    private String briefDescription;

    @Column(name = "city")
    private String city;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "logo_url")
    private String logoUrl;
}
