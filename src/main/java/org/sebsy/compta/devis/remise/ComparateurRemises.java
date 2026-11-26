package org.sebsy.compta.devis.remise;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ComparateurRemises {

    private final List<Remise> remises;

    public ComparateurRemises(List<Remise> remises) {
        this.remises = remises;
    }

    public BigDecimal meilleurTaux(int quantite) {
        return remises.stream()
            .map(r -> r.taux(quantite))
            .filter(Objects::nonNull)
            .max(Comparator.naturalOrder())
            .orElse(BigDecimal.ZERO);
    }
}
