package com.tui.proof.controller;

import com.tui.proof.dto.OrderCreateRequest;
import com.tui.proof.dto.OrderDto;
import com.tui.proof.dto.OrderFilterResponse;
import com.tui.proof.dto.OrderUpdateRequest;
import com.tui.proof.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest) {
        OrderDto result = orderService.createNewOrder(orderCreateRequest);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        OrderDto result = orderService.updateOrder(orderUpdateRequest);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderFilterResponse>> filterOrders(@RequestParam(value = "clientName", required = false) String clientName,
                                                                  @RequestParam(value = "clientSurname", required = false) String clientSurname,
                                                                  @RequestParam(value = "clientUsername", required = false) String clientUsername,
                                                                  @RequestParam(value = "clientEmail", required = false) String clientEmail) {
        List<OrderFilterResponse> result = orderService.filterOrders(StringUtils.isEmpty(clientEmail) ? null : clientName,
                StringUtils.isEmpty(clientSurname) ? null : clientSurname,
                StringUtils.isEmpty(clientUsername) ? null : clientUsername,
                StringUtils.isEmpty(clientEmail) ? null : clientEmail);
        return ResponseEntity.ok().body(result);
    }
}
