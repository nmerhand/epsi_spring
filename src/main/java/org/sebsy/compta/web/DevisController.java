package org.sebsy.compta.web;

import org.sebsy.compta.devis.Devis;
import org.sebsy.compta.devis.DevisService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/devis")
public class DevisController {

    private  final DevisService devis;
    public DevisController(DevisService devis) {
        this.devis = devis;
    }

    @GetMapping
    Devis calculer(@RequestParam BigDecimal prixHt, @RequestParam(defaultValue = "1") int quantite) {
        return devis.calculer(prixHt, quantite);
    }

    @GetMapping("/{quantite}")
    Devis parChemin(@PathVariable int quantite, @RequestParam BigDecimal prixHt) {
        return devis.calculer(prixHt, quantite);
    }
}
