package org.sebsy.compta.devis;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RemiseAucune implements Remise {

    @Override
    public BigDecimal taux(int quantite) {
        return BigDecimal.ZERO;
    }
}
