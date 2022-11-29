package com.MovePassive.MovePassive.entity;


import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@ToString
@EqualsAndHashCode(of = {"identityAccount"})
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "movePassive")
public class MovePassive {

    @Id
    private String id;
    @NotEmpty
    @Indexed(unique = true)
    @Size(min = 0, max = 20)
    @Column(nullable = false, length = 20)
    private String identityAccount;
    @NotNull
    @DecimalMax("10000000.00") @DecimalMin("0.0")
    @Column(nullable = false, length = 50)
    private Double amount;
    @NotEmpty
    @Size(min = 0, max = 50)
    @Column(nullable = false, length = 50)
    private String operationType;
    @NotNull
    @Column(nullable = false)
    private LocalDate dateRegister;

}
