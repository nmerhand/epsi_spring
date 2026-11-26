package org.sebsy.compta.devis;

import org.sebsy.compta.devis.remise.Remise;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class DevisPublicService {

    private final Remise remise;
    public DevisPublicService(@Qualifier("sansRemise") Remise remise) {
        this.remise = remise;
    }

}
