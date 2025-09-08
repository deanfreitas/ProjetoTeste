package br.com.orderservice.domain.enuns;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderStatus Enum Tests")
class OrderStatusTest {

    @Nested
    @DisplayName("Enum Values")
    class EnumValues {

        @Test
        @DisplayName("Should have COMPLETED status value")
        void shouldHaveCompletedStatusValue() {
            OrderStatus status = OrderStatus.COMPLETED;

            assertNotNull(status);
            assertEquals("COMPLETED", status.name());
        }

        @Test
        @DisplayName("Should return correct number of enum values")
        void shouldReturnCorrectNumberOfEnumValues() {
            OrderStatus[] values = OrderStatus.values();

            assertEquals(1, values.length);
            assertEquals(OrderStatus.COMPLETED, values[0]);
        }

        @Test
        @DisplayName("Should convert string to enum value")
        void shouldConvertStringToEnumValue() {
            OrderStatus status = OrderStatus.valueOf("COMPLETED");

            assertEquals(OrderStatus.COMPLETED, status);
        }

        @Test
        @DisplayName("Should throw exception for invalid string value")
        void shouldThrowExceptionForInvalidStringValue() {
            assertThrows(IllegalArgumentException.class, () -> {
                OrderStatus.valueOf("INVALID_STATUS");
            });
        }

        @Test
        @DisplayName("Should throw exception for null value")
        void shouldThrowExceptionForNullValue() {
            assertThrows(NullPointerException.class, () -> {
                OrderStatus.valueOf(null);
            });
        }
    }

    @Nested
    @DisplayName("Enum Behavior")
    class EnumBehavior {

        @Test
        @DisplayName("Should be equal to same enum value")
        void shouldBeEqualToSameEnumValue() {
            OrderStatus status1 = OrderStatus.COMPLETED;
            OrderStatus status2 = OrderStatus.COMPLETED;

            assertEquals(status1, status2);
            assertSame(status1, status2); // enum instances are singletons
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            OrderStatus status1 = OrderStatus.COMPLETED;
            OrderStatus status2 = OrderStatus.COMPLETED;

            assertEquals(status1.hashCode(), status2.hashCode());
        }

        @Test
        @DisplayName("Should have correct toString representation")
        void shouldHaveCorrectToStringRepresentation() {
            OrderStatus status = OrderStatus.COMPLETED;

            assertEquals("COMPLETED", status.toString());
        }

        @Test
        @DisplayName("Should have correct ordinal value")
        void shouldHaveCorrectOrdinalValue() {
            OrderStatus status = OrderStatus.COMPLETED;

            assertEquals(0, status.ordinal());
        }

        @Test
        @DisplayName("Should be comparable")
        void shouldBeComparable() {
            OrderStatus status = OrderStatus.COMPLETED;

            assertEquals(0, status.compareTo(OrderStatus.COMPLETED));
        }
    }

    @Nested
    @DisplayName("Usage in Switch Statements")
    class UsageInSwitchStatements {

        @Test
        @DisplayName("Should work correctly in switch statement")
        void shouldWorkCorrectlyInSwitchStatement() {
            OrderStatus status = OrderStatus.COMPLETED;
            String result;

            switch (status) {
                case COMPLETED:
                    result = "Order is completed";
                    break;
                default:
                    result = "Unknown status";
                    break;
            }

            assertEquals("Order is completed", result);
        }

        @Test
        @DisplayName("Should handle all enum values in exhaustive switch")
        void shouldHandleAllEnumValuesInExhaustiveSwitch() {
            for (OrderStatus status : OrderStatus.values()) {
                String description = getStatusDescription(status);
                assertNotNull(description);
                assertFalse(description.isEmpty());
            }
        }

        private String getStatusDescription(OrderStatus status) {
            switch (status) {
                case COMPLETED:
                    return "The order has been completed successfully";
                default:
                    return "Unknown status";
            }
        }
    }
}