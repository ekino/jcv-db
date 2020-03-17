package com.ekino.oss.jcv.db.example.jcvdbcassandraexample.model;

import com.ekino.oss.jcv.db.example.jcvdbcassandraexample.dto.CurrencyDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table("currency")
public class Currency {

    @PrimaryKey
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
