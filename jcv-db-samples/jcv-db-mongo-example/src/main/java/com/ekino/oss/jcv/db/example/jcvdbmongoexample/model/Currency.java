package com.ekino.oss.jcv.db.example.jcvdbmongoexample.model;

import com.ekino.oss.jcv.db.example.jcvdbmongoexample.dto.CurrencyDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "currencies")
public class Currency {

    @Id
    private UUID id;
    private String code;
    private String label;

    @Version
    private Integer version;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    private Instant lastModifiedDate;

    public CurrencyDto toDto() {
        return new CurrencyDto(this.id, this.code, this.label);
    }
}
