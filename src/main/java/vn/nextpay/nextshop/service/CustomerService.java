/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.service;

import vn.nextpay.nextshop.controller.dto.CoCustomerDTO;
import vn.nextpay.nextshop.controller.dto.request.CustomerCreateRequestDTO;
import vn.nextpay.nextshop.controller.dto.request.CustomerUpdateRequestDTO;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.controller.dto.response.CustomerResponseDTO;
import vn.nextpay.nextshop.util.HttpResponseObject;

import javax.servlet.http.HttpServletRequest;

public interface CustomerService {
    CoreResponseObject<CoCustomerDTO> createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO, HttpServletRequest servletRequest);
    CoreResponseObject<CoCustomerDTO> updateCustomer(String customerId, CustomerUpdateRequestDTO customerCreateRequestDTO, HttpServletRequest servletRequest);
    CoreResponseObject<CoCustomerDTO> getCustomerById(String customerId, HttpServletRequest servletRequest);

}
