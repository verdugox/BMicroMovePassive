package com.MovePassive.MovePassive.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovePassiveModel {

    @JsonIgnore
    private String id;
    @NotBlank(message="Account Number cannot be null or empty")
    private String identityAccount;
    @NotNull(message="Amount cannot be null or empty")
    private Double amount;
    @NotBlank(message="OperationType cannot be null or empty")
    private String operationType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateRegister;

}
