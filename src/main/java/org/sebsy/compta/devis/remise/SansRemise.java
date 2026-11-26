package org.sebsy.compta.devis.remise;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SansRemise implements Remise {

    @Override
    public BigDecimal taux(int quantite) {
        return BigDecimal.ZERO;
    }
}
