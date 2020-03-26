package com.ekino.oss.jcv.db.example.jcvdbjdbcexample.service;

import com.ekino.oss.jcv.db.example.jcvdbjdbcexample.dto.CurrencyDto;
import com.ekino.oss.jcv.db.example.jcvdbjdbcexample.model.Currency;
import com.ekino.oss.jcv.db.example.jcvdbjdbcexample.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public CurrencyDto createCurrency(String code, String label) {
        Currency newCurrency = new Currency();
        newCurrency.setCode(code);
        newCurrency.setLabel(label);

        return currencyRepository.save(newCurrency).toDto();
    }
}
