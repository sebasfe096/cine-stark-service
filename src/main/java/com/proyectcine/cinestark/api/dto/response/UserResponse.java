package com.proyectcine.cinestark.api.dto.response;

import com.proyectcine.cinestark.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String documentNumber;
    private String role;
    private Boolean enabled;
    private String birthDate;
    private Boolean acceptedTerms;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .documentNumber(user.getDocumentNumber())
                .role(user.getRole().getName())
                .enabled(user.getEnabled())
                .birthDate(user.getBirthDate() != null ? user.getBirthDate().toLocalDate().toString() : null)
                .acceptedTerms(user.getAcceptedTerms())
                .build();
    }
}
