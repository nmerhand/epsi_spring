package org.sebsy.compta.devis;

import java.math.BigDecimal;

public record Devis(
    int quantite,
    BigDecimal ht,
    BigDecimal ttc
) {}
