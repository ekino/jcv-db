package com.ekino.oss.jcv.db.example.jcvdbjdbcexample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyDto {
    private UUID id;
    private String code;
    private String label;
}
