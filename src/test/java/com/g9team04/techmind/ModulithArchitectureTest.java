package com.g9team04.techmind;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithArchitectureTest {
    ApplicationModules modules = ApplicationModules.of(TechmindApplication.class);

    @Test
    void verifyArchitecture() {
        modules.verify();
    }
     @Test
     void writeDocumentation() {
         new Documenter(modules)
                 .writeModulesAsPlantUml()
                 .writeIndividualModulesAsPlantUml();
     }
}
