package br.com.orderservice.infrastructure.adapters.in.messaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StoreResolver")
class StoreResolverTest {

    private StoreResolver storeResolver;

    @BeforeEach
    void setUp() {
        storeResolver = new StoreResolver();
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when store code is null")
        void testResolveStoreIdWithNullStoreCode() {
            // When & Then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> storeResolver.resolveStoreId(null)
            );
            assertEquals("Store code cannot be null", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Basic Functionality Tests")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("Should resolve valid store code to ID within expected range")
        void testResolveStoreIdWithValidStoreCode() {
            // Given
            String storeCode = "STORE001";

            // When
            Long storeId = storeResolver.resolveStoreId(storeCode);

            // Then
            assertNotNull(storeId);
            assertTrue(storeId >= 1L && storeId <= 1000L);
        }

        @Test
        @DisplayName("Should return different IDs for different store codes")
        void testResolveStoreIdDifferentCodesReturnDifferentIds() {
            // Given
            String storeCode1 = "STORE001";
            String storeCode2 = "STORE002";

            // When
            Long storeId1 = storeResolver.resolveStoreId(storeCode1);
            Long storeId2 = storeResolver.resolveStoreId(storeCode2);

            // Then
            assertNotEquals(storeId1, storeId2);
        }
    }

    @Nested
    @DisplayName("Consistency and Caching Tests")
    class ConsistencyAndCachingTests {

        @Test
        @DisplayName("Should cache and return same result for repeated calls")
        void testResolveStoreIdCachesResult() {
            // Given
            String storeCode = "STORE002";

            // When
            Long firstCall = storeResolver.resolveStoreId(storeCode);
            Long secondCall = storeResolver.resolveStoreId(storeCode);

            // Then
            assertEquals(firstCall, secondCall);
        }

        @Test
        @DisplayName("Should consistently map same store code to same ID across multiple calls")
        void testResolveStoreIdConsistentHashMapping() {
            // Given
            String storeCode = "CONSISTENT_STORE";

            // When - Call multiple times
            Long id1 = storeResolver.resolveStoreId(storeCode);
            Long id2 = storeResolver.resolveStoreId(storeCode);
            Long id3 = storeResolver.resolveStoreId(storeCode);

            // Then - All calls should return the same ID
            assertEquals(id1, id2);
            assertEquals(id2, id3);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty string store code")
        void testResolveStoreIdWithEmptyString() {
            // Given
            String emptyStoreCode = "";

            // When
            Long storeId = storeResolver.resolveStoreId(emptyStoreCode);

            // Then
            assertNotNull(storeId);
            assertTrue(storeId >= 1L && storeId <= 1000L);
        }

        @Test
        @DisplayName("Should handle store code with special characters")
        void testResolveStoreIdWithSpecialCharacters() {
            // Given
            String specialStoreCode = "STORE@#$%";

            // When
            Long storeId = storeResolver.resolveStoreId(specialStoreCode);

            // Then
            assertNotNull(storeId);
            assertTrue(storeId >= 1L && storeId <= 1000L);
        }

        @Test
        @DisplayName("Should handle very long store code")
        void testResolveStoreIdWithLongString() {
            // Given
            String longStoreCode = "VERYLONGSTORECODETOTESTBOUNDARIES";

            // When
            Long storeId = storeResolver.resolveStoreId(longStoreCode);

            // Then
            assertNotNull(storeId);
            assertTrue(storeId >= 1L && storeId <= 1000L);
        }
    }
}