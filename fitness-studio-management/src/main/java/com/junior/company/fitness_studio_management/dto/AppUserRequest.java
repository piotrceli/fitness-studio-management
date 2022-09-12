package com.junior.company.fitness_studio_management.dto;

import com.junior.company.fitness_studio_management.validation.FieldMatch;
import com.junior.company.fitness_studio_management.validation.ValidEmail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@FieldMatch.List({
        @FieldMatch(firstField = "password", secondField = "matchingPassword", message = "The password must match")})
@Getter
@SuperBuilder
@NoArgsConstructor
public class AppUserRequest {

    private Long id;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "Unique user's username", example = "username100")
    private String username;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 8, message = "Min length is 8")
    @ApiModelProperty(notes = "User's password (min 8 signs)", example = "1Q3w5E7r")
    private String password;

    @NotBlank(message = "Cannot be empty")
    @ApiModelProperty(notes = "Matching user's password with minimum 8 signs", example = "1Q3w5E7r")
    private String matchingPassword;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "User's first name", example = "Tom")
    private String firstName;

    @NotBlank(message = "Cannot be empty")
    @Length(min = 2, message = "Min length is 2")
    @ApiModelProperty(notes = "User's last name", example = "Smith")
    private String lastName;

    @NotBlank(message = "Cannot be empty")
    @ValidEmail
    @ApiModelProperty(notes = "User's email", example = "email@domain.com")
    private String email;

    @NotNull(message = "Cannot be empty")
    @ApiModelProperty(notes = "Date of birth of an user (dd-MM-yyyy)", example = "1990-02-20")
    private LocalDate dob;

}
