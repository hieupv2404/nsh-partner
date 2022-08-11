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
import vn.nextpay.nextshop.controller.dto.request.FilterPartner;
import vn.nextpay.nextshop.controller.dto.request.GroupCreateDTO;
import vn.nextpay.nextshop.controller.dto.request.GroupDeleteDTO;
import vn.nextpay.nextshop.controller.dto.request.GroupUpdateDTO;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.dao.entity.Group;
import vn.nextpay.nextshop.enums.TypePartnerEnum;
import vn.nextpay.nextshop.security.AuthorizationFilter;
import vn.nextpay.nextshop.service.GroupService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/groups")
@CrossOrigin(origins = "**", maxAge = 3600)
public class GroupController {
    @Autowired
    private GroupService groupService;

    /*
 ======= CUSTOMER ===========
  */

    @ApiOperation(value = "Tạo mới Group cho Customer")
    @PostMapping("/customers/create")
    public ResponseEntity<CoreResponseObject<Group>> createGroupCustomer(@RequestBody @Valid GroupCreateDTO groupCreateDTO) {
        log.info("===== Group request create controller: " + groupCreateDTO.toString());
        return new ResponseEntity<>(groupService.createGroup(groupCreateDTO, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách Group Customers")
    @PostMapping("/customers")
    public ResponseEntity<CoreResponseObject<List<Group>>> getListGroupsCustomers(@RequestBody @Valid FilterPartner filterPartner) {
        log.info("===== Group get all");
        return new ResponseEntity<>(groupService.getAllGroups(filterPartner, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Sửa thông tin Group Customers")
    @PutMapping("/customers/edit/{id}")
    public ResponseEntity<CoreResponseObject<Group>> updateGroupsCustomer(@PathVariable String id, @RequestBody @Valid GroupUpdateDTO groupUpdateDTO) {
        log.info("===== Group Update");
        return new ResponseEntity<>(groupService.updateGroup(id, groupUpdateDTO, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Xoá thông tin Group Customers")
    @PutMapping("/customers/delete")
    public ResponseEntity<CoreResponseObject<List<Group>>> deleteGroupsCustomers(@RequestBody @Valid GroupDeleteDTO groupDeleteDTO) {
        log.info("===== Group Delete");
        return new ResponseEntity<>(groupService.deleteGroup(groupDeleteDTO, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }

    /*
    ======= SUPPLIER ===========
     */

    @ApiOperation(value = "Tạo mới Group cho Supplier")
    @PostMapping("/suppliers/create")
    public ResponseEntity<CoreResponseObject<Group>> createGroupSupplier(@RequestBody @Valid GroupCreateDTO groupCreateDTO) {
        log.info("===== Group request create controller: " + groupCreateDTO.toString());
        return new ResponseEntity<>(groupService.createGroup(groupCreateDTO, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách Group Supplier")
    @PostMapping("/suppliers")
    public ResponseEntity<CoreResponseObject<List<Group>>> getListGroupsSuppliers(@RequestBody @Valid FilterPartner filterPartner) {
        log.info("===== Group get all");
        return new ResponseEntity<>(groupService.getAllGroups(filterPartner, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Sửa thông tin Group Suppliers")
    @PutMapping("/suppliers/edit/{id}")
    public ResponseEntity<CoreResponseObject<Group>> updateGroupsSupplier(@PathVariable String id, @RequestBody @Valid GroupUpdateDTO groupUpdateDTO) {
        log.info("===== Group Update");
        return new ResponseEntity<>(groupService.updateGroup(id, groupUpdateDTO, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Xoá thông tin Group Suppliers")
    @PutMapping("/suppliers/delete")
    public ResponseEntity<CoreResponseObject<List<Group>>> deleteGroupsSuppliers(@RequestBody @Valid GroupDeleteDTO groupDeleteDTO) {
        log.info("===== Group Delete");
        return new ResponseEntity<>(groupService.deleteGroup(groupDeleteDTO, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy thông tin Group bằng ID")
    @GetMapping("/{groupId}")
    public ResponseEntity<CoreResponseObject<Group>> getGroupById(@PathVariable String groupId) {
        log.info("===== Get Group By ID: " + groupId);
        return new ResponseEntity<>(groupService.getGroupById(groupId), HttpStatus.OK);
    }

    @ApiOperation(value = "Khởi tạo group mặc định")
    @GetMapping("/init")
    public ResponseEntity<CoreResponseObject<String>> initGroup() {
        log.info("===== Group Init By Merchant: " + AuthorizationFilter.MERCHANT_ID);
        return new ResponseEntity<>(groupService.initGroup(), HttpStatus.OK);
    }

    @ApiOperation(value = "Xoá thông tin Group khi xoá Merchant")
    @PutMapping("/deleted-by-merchant")
    public ResponseEntity<CoreResponseObject<String>> deleteGroupsByMerchant() {
        String merchantId = AuthorizationFilter.MERCHANT_ID;
        log.info("===== Group Deleted By Merchant: " + merchantId);
        return new ResponseEntity<>(groupService.deleteGroupByMerchant(merchantId), HttpStatus.OK);
    }

}
