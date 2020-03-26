package com.ekino.oss.jcv.db.example.jcvdbjdbcexample.model;

import com.ekino.oss.jcv.db.example.jcvdbjdbcexample.dto.CurrencyDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Currency {

    @Id
    @GeneratedValue
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
