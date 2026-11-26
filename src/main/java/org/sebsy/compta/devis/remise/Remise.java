package org.sebsy.compta.devis.remise;

import java.math.BigDecimal;

public interface Remise {

    BigDecimal taux(int quantite) ;
}
