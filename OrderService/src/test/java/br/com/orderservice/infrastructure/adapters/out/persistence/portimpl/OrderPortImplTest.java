package br.com.orderservice.infrastructure.adapters.out.persistence.portimpl;

import br.com.orderservice.domain.enuns.OrderStatus;
import br.com.orderservice.domain.model.Order;
import br.com.orderservice.infrastructure.adapters.out.persistence.entity.OrderEntity;
import br.com.orderservice.infrastructure.adapters.out.persistence.mapper.OrderMapper;
import br.com.orderservice.infrastructure.adapters.out.persistence.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderPortImpl Tests")
class OrderPortImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderPortImpl orderPortImpl;

    private Order mockOrder;
    private OrderEntity mockOrderEntity;

    @BeforeEach
    void setUp() {
        mockOrder = Order.builder()
                .id(1L)
                .customerId("customer123")
                .totalCentavos(10000L)
                .status(OrderStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        mockOrderEntity = OrderEntity.builder()
                .id(1L)
                .customerId("customer123")
                .totalCentavos(10000L)
                .status(OrderStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Save Order Tests")
    class SaveOrderTests {

        @Test
        @DisplayName("Should save order successfully")
        void shouldSaveOrderSuccessfully() {
            // Given
            when(orderMapper.toEntity(mockOrder)).thenReturn(mockOrderEntity);
            when(orderRepository.save(mockOrderEntity)).thenReturn(mockOrderEntity);
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(mockOrder);

            // When
            Order result = orderPortImpl.save(mockOrder);

            // Then
            assertNotNull(result);
            assertEquals(mockOrder.getId(), result.getId());
            assertEquals(mockOrder.getCustomerId(), result.getCustomerId());
            assertEquals(mockOrder.getTotalCentavos(), result.getTotalCentavos());
            assertEquals(mockOrder.getStatus(), result.getStatus());
        }

        @Test
        @DisplayName("Should delegate to mapper and repository correctly")
        void shouldDelegateToMapperAndRepositoryCorrectly() {
            // Given
            when(orderMapper.toEntity(mockOrder)).thenReturn(mockOrderEntity);
            when(orderRepository.save(mockOrderEntity)).thenReturn(mockOrderEntity);
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(mockOrder);

            // When
            orderPortImpl.save(mockOrder);

            // Then
            verify(orderMapper).toEntity(mockOrder);
            verify(orderRepository).save(mockOrderEntity);
            verify(orderMapper).toDomain(mockOrderEntity);
        }

        @Test
        @DisplayName("Should return mapped domain object from saved entity")
        void shouldReturnMappedDomainObjectFromSavedEntity() {
            // Given
            Order expectedOrder = Order.builder()
                    .id(2L)
                    .customerId("customer456")
                    .totalCentavos(20000L)
                    .status(OrderStatus.COMPLETED)
                    .createdAt(LocalDateTime.now())
                    .build();

            when(orderMapper.toEntity(any(Order.class))).thenReturn(mockOrderEntity);
            when(orderRepository.save(any(OrderEntity.class))).thenReturn(mockOrderEntity);
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(expectedOrder);

            // When
            Order result = orderPortImpl.save(mockOrder);

            // Then
            assertEquals(expectedOrder, result);
        }
    }

    @Nested
    @DisplayName("Find Order By ID Tests")
    class FindOrderByIdTests {

        @Test
        @DisplayName("Should return order when found")
        void shouldReturnOrderWhenFound() {
            // Given
            Long orderId = 1L;
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrderEntity));
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(mockOrder);

            // When
            Optional<Order> result = orderPortImpl.findById(orderId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(mockOrder, result.get());
        }

        @Test
        @DisplayName("Should return empty optional when order not found")
        void shouldReturnEmptyOptionalWhenOrderNotFound() {
            // Given
            Long orderId = 999L;
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            // When
            Optional<Order> result = orderPortImpl.findById(orderId);

            // Then
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should delegate to repository findById")
        void shouldDelegateToRepositoryFindById() {
            // Given
            Long orderId = 1L;
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrderEntity));
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(mockOrder);

            // When
            orderPortImpl.findById(orderId);

            // Then
            verify(orderRepository).findById(orderId);
        }

        @Test
        @DisplayName("Should map entity to domain when found")
        void shouldMapEntityToDomainWhenFound() {
            // Given
            Long orderId = 1L;
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrderEntity));
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(mockOrder);

            // When
            orderPortImpl.findById(orderId);

            // Then
            verify(orderMapper).toDomain(mockOrderEntity);
        }
    }

    @Nested
    @DisplayName("Find Orders By Customer ID Tests")
    class FindOrdersByCustomerIdTests {

        @Test
        @DisplayName("Should return orders when customer has orders")
        void shouldReturnOrdersWhenCustomerHasOrders() {
            // Given
            String customerId = "customer123";
            List<OrderEntity> orderEntities = Arrays.asList(mockOrderEntity, mockOrderEntity);
            List<Order> expectedOrders = Arrays.asList(mockOrder, mockOrder);

            when(orderRepository.findByCustomerId(customerId)).thenReturn(orderEntities);
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(mockOrder);

            // When
            List<Order> result = orderPortImpl.findByCustomerId(customerId);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedOrders, result);
        }

        @Test
        @DisplayName("Should return empty list when customer has no orders")
        void shouldReturnEmptyListWhenCustomerHasNoOrders() {
            // Given
            String customerId = "customer456";
            when(orderRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

            // When
            List<Order> result = orderPortImpl.findByCustomerId(customerId);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should delegate to repository findByCustomerId")
        void shouldDelegateToRepositoryFindByCustomerId() {
            // Given
            String customerId = "customer123";
            when(orderRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

            // When
            orderPortImpl.findByCustomerId(customerId);

            // Then
            verify(orderRepository).findByCustomerId(customerId);
        }

        @Test
        @DisplayName("Should map all entities to domain objects")
        void shouldMapAllEntitiesToDomainObjects() {
            // Given
            String customerId = "customer123";
            OrderEntity entity1 = OrderEntity.builder().id(1L).customerId(customerId).build();
            OrderEntity entity2 = OrderEntity.builder().id(2L).customerId(customerId).build();
            List<OrderEntity> orderEntities = Arrays.asList(entity1, entity2);

            when(orderRepository.findByCustomerId(customerId)).thenReturn(orderEntities);
            when(orderMapper.toDomain(any(OrderEntity.class))).thenReturn(mockOrder);

            // When
            orderPortImpl.findByCustomerId(customerId);

            // Then
            verify(orderMapper, times(2)).toDomain(any(OrderEntity.class));
        }
    }

    @Nested
    @DisplayName("Find All Orders Tests")
    class FindAllOrdersTests {

        @Test
        @DisplayName("Should return all orders when orders exist")
        void shouldReturnAllOrdersWhenOrdersExist() {
            // Given
            List<OrderEntity> orderEntities = Arrays.asList(mockOrderEntity, mockOrderEntity);
            List<Order> expectedOrders = Arrays.asList(mockOrder, mockOrder);

            when(orderRepository.findAll()).thenReturn(orderEntities);
            when(orderMapper.toDomain(mockOrderEntity)).thenReturn(mockOrder);

            // When
            List<Order> result = orderPortImpl.findAll();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(expectedOrders, result);
        }

        @Test
        @DisplayName("Should return empty list when no orders exist")
        void shouldReturnEmptyListWhenNoOrdersExist() {
            // Given
            when(orderRepository.findAll()).thenReturn(Collections.emptyList());

            // When
            List<Order> result = orderPortImpl.findAll();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should delegate to repository findAll")
        void shouldDelegateToRepositoryFindAll() {
            // Given
            when(orderRepository.findAll()).thenReturn(Collections.emptyList());

            // When
            orderPortImpl.findAll();

            // Then
            verify(orderRepository).findAll();
        }

        @Test
        @DisplayName("Should map all entities to domain objects")
        void shouldMapAllEntitiesToDomainObjects() {
            // Given
            OrderEntity entity1 = OrderEntity.builder().id(1L).build();
            OrderEntity entity2 = OrderEntity.builder().id(2L).build();
            OrderEntity entity3 = OrderEntity.builder().id(3L).build();
            List<OrderEntity> orderEntities = Arrays.asList(entity1, entity2, entity3);

            when(orderRepository.findAll()).thenReturn(orderEntities);
            when(orderMapper.toDomain(any(OrderEntity.class))).thenReturn(mockOrder);

            // When
            orderPortImpl.findAll();

            // Then
            verify(orderMapper, times(3)).toDomain(any(OrderEntity.class));
        }
    }

    @Nested
    @DisplayName("Delete Order By ID Tests")
    class DeleteOrderByIdTests {

        @Test
        @DisplayName("Should delete order by ID successfully")
        void shouldDeleteOrderByIdSuccessfully() {
            // Given
            Long orderId = 1L;
            doNothing().when(orderRepository).deleteById(orderId);

            // When & Then
            assertDoesNotThrow(() -> orderPortImpl.deleteById(orderId));
        }

        @Test
        @DisplayName("Should delegate to repository deleteById")
        void shouldDelegateToRepositoryDeleteById() {
            // Given
            Long orderId = 1L;
            doNothing().when(orderRepository).deleteById(orderId);

            // When
            orderPortImpl.deleteById(orderId);

            // Then
            verify(orderRepository).deleteById(orderId);
        }

        @Test
        @DisplayName("Should handle repository exceptions gracefully")
        void shouldHandleRepositoryExceptionsGracefully() {
            // Given
            Long orderId = 1L;
            doThrow(new RuntimeException("Database error")).when(orderRepository).deleteById(orderId);

            // When & Then
            assertThrows(RuntimeException.class, () -> orderPortImpl.deleteById(orderId));
        }
    }

    @Nested
    @DisplayName("Exists By ID Tests")
    class ExistsByIdTests {

        @Test
        @DisplayName("Should return true when order exists")
        void shouldReturnTrueWhenOrderExists() {
            // Given
            Long orderId = 1L;
            when(orderRepository.existsById(orderId)).thenReturn(true);

            // When
            boolean result = orderPortImpl.existsById(orderId);

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false when order does not exist")
        void shouldReturnFalseWhenOrderDoesNotExist() {
            // Given
            Long orderId = 999L;
            when(orderRepository.existsById(orderId)).thenReturn(false);

            // When
            boolean result = orderPortImpl.existsById(orderId);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("Should delegate to repository existsById")
        void shouldDelegateToRepositoryExistsById() {
            // Given
            Long orderId = 1L;
            when(orderRepository.existsById(orderId)).thenReturn(true);

            // When
            orderPortImpl.existsById(orderId);

            // Then
            verify(orderRepository).existsById(orderId);
        }
    }
}