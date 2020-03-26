package com.ekino.oss.jcv.db.example.jcvdbmongoexample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyDto {
    private UUID id;
    private String code;
    private String label;
}
