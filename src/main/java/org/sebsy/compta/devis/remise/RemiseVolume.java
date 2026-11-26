package org.sebsy.compta.devis.remise;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Primary
public class RemiseVolume implements Remise {

    @Override
    public BigDecimal taux(int quantite) {
        if (quantite >= 10) {
            return new BigDecimal("0.05");
        } else {
            return BigDecimal.ZERO;
        }
    }
}
