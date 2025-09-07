package br.com.inventoryservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class InventoryServiceApplicationTest {

    @Test
    void shouldRunSpringApplication() {
        // Given
        ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
        
        // When
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            mockedSpringApplication.when(() -> SpringApplication.run(eq(InventoryServiceApplication.class), any(String[].class)))
                    .thenReturn(mockContext);

            InventoryServiceApplication.main(new String[]{});
            
            // Then
            mockedSpringApplication.verify(() -> SpringApplication.run(eq(InventoryServiceApplication.class), any(String[].class)));
        }
    }
    
    @Test
    void shouldCreateMainInstance() throws Exception {
        // Test constructor coverage
        Constructor<InventoryServiceApplication> constructor = InventoryServiceApplication.class.getDeclaredConstructor();
        InventoryServiceApplication instance = constructor.newInstance();
        assertNotNull(instance);
    }
}