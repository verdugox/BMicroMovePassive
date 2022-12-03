package com.MovePassive.MovePassive.entity;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductPassive {


    private String id;
    private String identityAccount;
    private String document;
    private String typeAccount;
    private Double availableAmount;
    private LocalDate dateRegister;



}