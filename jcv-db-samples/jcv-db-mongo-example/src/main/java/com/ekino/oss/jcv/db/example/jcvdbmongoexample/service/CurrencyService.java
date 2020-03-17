package com.ekino.oss.jcv.db.example.jcvdbmongoexample.service;

import com.ekino.oss.jcv.db.example.jcvdbmongoexample.dto.CurrencyDto;
import com.ekino.oss.jcv.db.example.jcvdbmongoexample.model.Currency;
import com.ekino.oss.jcv.db.example.jcvdbmongoexample.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public CurrencyDto createCurrency(String code, String label) {
        Currency newCurrency = new Currency();
        newCurrency.setId(UUID.randomUUID());
        newCurrency.setCode(code);
        newCurrency.setLabel(label);

        Currency saveCurrency = currencyRepository.save(newCurrency);
        return saveCurrency.toDto();
    }
}
