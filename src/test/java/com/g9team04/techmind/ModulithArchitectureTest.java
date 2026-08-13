package com.g9team04.techmind;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

 class ModulithArchitectureTest {
    ApplicationModules modules = ApplicationModules.of(TechmindApplication.class);

    @Test
    void verifyArchitecture() {
        modules.verify();
    }
}
