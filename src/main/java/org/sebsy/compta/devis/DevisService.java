package org.sebsy.compta.devis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DevisService {

    private static final BigDecimal TVA = new BigDecimal("1.20");
    private final Remise remise;

    @Autowired
    public DevisService(Remise remise) {
        this.remise = remise;
    }

    public Devis calculer(BigDecimal prixHt, int quantite) {
        BigDecimal ht =  prixHt
                .multiply(new BigDecimal(quantite))
                .multiply(BigDecimal.ONE.subtract(remise.taux(quantite)))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal ttc = ht.multiply(TVA).setScale(2, RoundingMode.HALF_UP);
        return new Devis(quantite, ht, ttc);
    }
}
