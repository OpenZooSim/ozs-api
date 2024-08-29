package com.openzoosim;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.openzoosim.models.interfaces.IEnvService;

@QuarkusTest
class EnvServiceTest {

    @Inject
    Instance<IEnvService> _envService;  

    @Test
    void testEnvServiceLoading() {

        String appVersion = _envService.get().GetAppVersion();

        assertNotNull(appVersion);
        assertFalse(appVersion.isEmpty());

    } 

}
