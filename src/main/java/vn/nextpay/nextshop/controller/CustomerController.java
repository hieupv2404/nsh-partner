/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.nextpay.nextshop.controller.dto.CoCustomerDTO;
import vn.nextpay.nextshop.controller.dto.request.CustomerCreateRequestDTO;
import vn.nextpay.nextshop.controller.dto.request.CustomerUpdateRequestDTO;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.controller.dto.response.CustomerResponseDTO;
import vn.nextpay.nextshop.service.CustomerService;
import vn.nextpay.nextshop.util.HttpResponseObject;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/customers")
@CrossOrigin(origins = "**", maxAge = 3600)
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @ApiOperation(value = "Test connection Server")
    @GetMapping
    public ResponseEntity<String> testConnection() {
        log.info("Connection successful");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @ApiOperation(value = "Tạo mới Customer- Đồng bộ vào Core")
    @PostMapping
    public ResponseEntity< CoreResponseObject<CoCustomerDTO>> createCustomer(@RequestBody @Valid CustomerCreateRequestDTO customerCreateRequestDTO, HttpServletRequest servletRequest) {
        log.info("Customer request create controller: "+  customerCreateRequestDTO.toString());
        return new ResponseEntity<>(customerService.createCustomer(customerCreateRequestDTO, servletRequest), HttpStatus.OK);
    }

    @ApiOperation(value = "Chỉnh sửa thông tin Customer- Đồng bộ vào Core")
    @PutMapping("/{customerId}")
    public ResponseEntity< CoreResponseObject<CoCustomerDTO>> updateCustomer(@PathVariable String customerId, @RequestBody @Valid CustomerUpdateRequestDTO customerUpdateRequestDTO, HttpServletRequest servletRequest) {
        log.info("Customer request id controller: "+ customerId);
        return new ResponseEntity<>(customerService.updateCustomer(customerId, customerUpdateRequestDTO, servletRequest), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy thông tin từ 1 Customer nhất định")
    @GetMapping("/{customerId}")
    public ResponseEntity< CoreResponseObject<CoCustomerDTO>> getCustomerById(@PathVariable String customerId, HttpServletRequest servletRequest) {
        log.info("Customer request id controller: "+ customerId);
        return new ResponseEntity<>(customerService.getCustomerById(customerId, servletRequest), HttpStatus.OK);
    }
}
