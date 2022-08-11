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
import vn.nextpay.nextshop.controller.dto.request.*;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.dao.entity.Partner;
import vn.nextpay.nextshop.dao.repository.PartnerRepository;
import vn.nextpay.nextshop.enums.TypePartnerEnum;
import vn.nextpay.nextshop.security.AuthorizationFilter;
import vn.nextpay.nextshop.service.PartnerService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/partners")
@CrossOrigin(origins = "**", maxAge = 3600)
public class PartnerController {
    @Autowired
    private PartnerService partnerService;

    @Autowired
    private PartnerRepository partnerRepository;

    @ApiOperation(value = "Lấy danh sách Partner")
    @PostMapping
    public ResponseEntity<CoreResponseObject<List<Partner>>> getListPartners(@RequestBody @Valid FilterPartner filterPartner) {
        log.info("===== Partner get all controller");
        return new ResponseEntity<>(partnerService.getPartnersByMerchantId(filterPartner), HttpStatus.OK);
    }

       /*
    ======= CUSTOMER ===========
     */

    @ApiOperation(value = "Tạo mới Customer")
    @PostMapping("/customers/create")
    public ResponseEntity<CoreResponseObject<Partner>> createCustomer(@RequestBody @Valid PartnerCreateDTO partnerCreateDTO) {
        log.info("===== Customer request create controller: " + partnerCreateDTO.toString());
        return new ResponseEntity<>(partnerService.createPartner(partnerCreateDTO, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách Customer")
    @PostMapping("/customers")
    public ResponseEntity<CoreResponseObject<List<Partner>>> getListCustomers(@RequestBody @Valid FilterPartner filterPartner) {
        log.info("===== Customer get all");
        return new ResponseEntity<>(partnerService.getAllPartner(filterPartner, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Sửa thông tin Customer")
    @PutMapping("/customers/edit/{id}")
    public ResponseEntity<CoreResponseObject<Partner>> updateCustomers(@PathVariable String id, @RequestBody @Valid PartnerUpdateDTO partnerUpdateDTO) {
        log.info("===== Customer Update");
        return new ResponseEntity<>(partnerService.updatePartner(id, partnerUpdateDTO, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Xoá thông tin Customer")
    @PutMapping("/customers/delete")
    public ResponseEntity<CoreResponseObject<List<Partner>>> deleteCustomers(@RequestBody @Valid PartnerListDeleteDTO partnerListDeleteDTO) {
        log.info("===== Customer Delete");
        return new ResponseEntity<>(partnerService.deletePartners(partnerListDeleteDTO, TypePartnerEnum.CUSTOMER.toString()), HttpStatus.OK);
    }


    /*
     ======= SUPPLIER ===========
      */


    @ApiOperation(value = "Tạo mới Supplier")
    @PostMapping("/suppliers/create")
    public ResponseEntity<CoreResponseObject<Partner>> createSupplier(@RequestBody @Valid PartnerCreateDTO partnerCreateDTO) {
        log.info("=====  Supplier request create controller: " + partnerCreateDTO.toString());
        return new ResponseEntity<>(partnerService.createPartner(partnerCreateDTO, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy danh sách Supplier")
    @PostMapping("/suppliers")
    public ResponseEntity<CoreResponseObject<List<Partner>>> getListSuppliers(@RequestBody @Valid FilterPartner filterPartner) {
        log.info("===== Supplier get all");
        String name = filterPartner.getQuery();
        String phone = filterPartner.getQuery();
        return new ResponseEntity<>(partnerService.getAllPartner(filterPartner, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Sửa thông tin Suppliers")
    @PutMapping("/suppliers/edit/{id}")
    public ResponseEntity<CoreResponseObject<Partner>> updateSuppliers(@PathVariable String id, @RequestBody @Valid PartnerUpdateDTO partnerUpdateDTO) {
        log.info("===== Suppliers Update");
        return new ResponseEntity<>(partnerService.updatePartner(id, partnerUpdateDTO, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Xoá thông tin Suppliers")
    @PutMapping("/suppliers/delete")
    public ResponseEntity<CoreResponseObject<List<Partner>>> deleteSuppliers(@RequestBody @Valid PartnerListDeleteDTO partnerListDeleteDTO) {
        log.info("===== Suppliers Delete");
        return new ResponseEntity<>(partnerService.deletePartners(partnerListDeleteDTO, TypePartnerEnum.SUPPLIER.toString()), HttpStatus.OK);
    }

    @ApiOperation(value = "Sửa thông tin Contact")
    @PutMapping("/contacts/{id}")
    public ResponseEntity<CoreResponseObject<Partner>> updateContacts(@PathVariable String id, @RequestBody @Valid ContactCreateDTO contactCreateDTO) {
        log.info("===== Contact Update");
        return new ResponseEntity<>(partnerService.updateContact(id, contactCreateDTO), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy thông tin Partner bằng ID")
    @PostMapping("/detail")
    public ResponseEntity<CoreResponseObject<Partner>> getPartnerById(@RequestBody @Valid FilterPartner filterPartner) {
        log.info("===== Get Partner By ID: " + filterPartner.getQuery());
        return new ResponseEntity<>(partnerService.getPartnerById(filterPartner), HttpStatus.OK);
    }

    @ApiOperation(value = "Xoá Partner khi xoá Merchant")
    @PutMapping("/deleted-by-merchant")
    public ResponseEntity<CoreResponseObject<String>> deletePartnerByMerchant() {
        String merchantId = AuthorizationFilter.MERCHANT_ID;
        log.info("===== Delete Partner By Merchant " + merchantId);
        return new ResponseEntity<>(partnerService.deletePartnerByMerchant(merchantId), HttpStatus.OK);
    }

}
