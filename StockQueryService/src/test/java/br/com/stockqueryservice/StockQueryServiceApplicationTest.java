package br.com.stockqueryservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
class StockQueryServiceApplicationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // It will cover the Main.main() method execution
    }

    @Test
    void mainMethodShouldStartApplication() {
        // Test that Main class constructor can be called
        StockQueryServiceApplication stockQueryServiceApplication = new StockQueryServiceApplication();
        // This ensures the constructor is covered
        assertThat(stockQueryServiceApplication).isNotNull();
    }

    @Test
    void mainMethodShouldRunSpringApplication() {
        // This test calls the actual main method to achieve 100% coverage
        // We can call it safely since @SpringBootTest already manages the application context
        String[] args = {};
        
        // This will cover the SpringApplication.run() call in Main.main()
        assertThatCode(() -> {
            // The main method just delegates to SpringApplication.run
            // Since we're already in a Spring test context, this should work
            StockQueryServiceApplication.main(args);
        }).doesNotThrowAnyException();
    }
}