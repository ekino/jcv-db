package com.ekino.oss.jcv.db.example.jcvdbassertjdbexample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyDto {
    @Id
    private UUID id;
    private String code;
    private String label;
}
