package org.sebsy.compta.devis;

import java.math.BigDecimal;

public interface Remise {

    BigDecimal taux(int quantite) ;
}
