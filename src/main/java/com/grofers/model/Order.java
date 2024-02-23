package com.grofers.model;

import com.grofers.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "all_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private LocalDateTime orderedTime;

    private LocalDateTime deliveredTime;

    @NotNull(message = "orderStatus is mandatory!")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany()
    private List<Product> orderedProducts;

    @ManyToOne
    @JoinColumn(name = "userId")
    @NotNull(message = "user should be able to place orders")
    private User user;
}
