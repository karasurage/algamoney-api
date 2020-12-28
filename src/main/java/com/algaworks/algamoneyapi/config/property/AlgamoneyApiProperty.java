package com.algaworks.algamoneyapi.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

    @Getter
    @Setter
    private String originPermitida = "http://localhost:8000";

    @Getter
    private final Seguranca seguranca = new Seguranca();

    @Getter
    @Setter
    public static class Seguranca {
        private boolean enableHttps;
    }
}
