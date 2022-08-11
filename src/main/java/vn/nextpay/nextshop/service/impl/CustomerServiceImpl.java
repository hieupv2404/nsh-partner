/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.nextpay.nextshop.controller.dto.CoCustomerDTO;
import vn.nextpay.nextshop.controller.dto.request.CustomerCreateRequestDTO;
import vn.nextpay.nextshop.controller.dto.request.CustomerUpdateRequestDTO;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.enums.EResponseStatus;
import vn.nextpay.nextshop.enums.ResponseStatus;
import vn.nextpay.nextshop.exception.NextshopException;
import vn.nextpay.nextshop.service.CustomerService;
import vn.nextpay.nextshop.util.RestFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;

    private final String CORE_API_URL = System.getenv("CORE_API_URL");

    public static CoreResponseObject<CoCustomerDTO> response;

    private static Long startTime = 0L;
    private static Long endTime = 0L;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public  CoreResponseObject<CoCustomerDTO> createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO, HttpServletRequest servletRequest) {
        final String CREATE_CUSTOMER_URL = CORE_API_URL + "/partner/non-platform";
        log.info("====== API Create customer: "+ CREATE_CUSTOMER_URL);
        try {
            Map<String, String> note = new HashMap<>();
            note.put("note", customerCreateRequestDTO.getNote());

            Map<String, Object> body = new HashMap<>();
            body.put("name", customerCreateRequestDTO.getName());
            body.put("phoneNo", customerCreateRequestDTO.getPhoneNo());
            body.put("address", customerCreateRequestDTO.getAddress());
            body.put("isPerson", true); // là cá nhân
            body.put("metaData", note); // thông tin thêm về ghi chú
            body.put("isUnknown", true); //khách vãng lai
            startTime = System.currentTimeMillis();
            Object customerFound = checkCustomerExistByPhone(customerCreateRequestDTO.getPhoneNo(), customerCreateRequestDTO.getMerchantId(), servletRequest);
            if(!((ArrayList) customerFound).isEmpty()){
                log.info("PHONE NUMBER IS EXISTED");
                CoCustomerDTO coCustomerDTO = objectMapper.convertValue(((ArrayList<?>) customerFound).get(0), CoCustomerDTO.class);
                return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, coCustomerDTO);

            }else {
                response = RestFactory.invokePostRequest
                        (CREATE_CUSTOMER_URL,
                                body,
                                new ParameterizedTypeReference<>() {
                                },
                                customerCreateRequestDTO.getMerchantId(),
                                servletRequest
                        );
                endTime = System.currentTimeMillis();
                log.info("======= Total Time to Create Customer (ms): " + (endTime - startTime));

                if (isDataCreated(response.getCode())) {
                    final String REGISTER_RELATIONSHIP_URL = CORE_API_URL + "/partner/register-relationship";
                    log.info("====== API Register Relation Customer: " + REGISTER_RELATIONSHIP_URL);
                    String newCustomerId = response.getData().id;
                    log.info("======== ID Customer Created: " + newCustomerId);
                    Map<String, Object> bodyForRegisterRelationship = new HashMap<>();
                    bodyForRegisterRelationship.put("id", newCustomerId);
                    bodyForRegisterRelationship.put("relationshipType", "CUSTOMER");

                    startTime = System.currentTimeMillis();
                    CoreResponseObject<Object> registerRelationship = RestFactory.invokePostRequest
                            (REGISTER_RELATIONSHIP_URL,
                                    bodyForRegisterRelationship,
                                    new ParameterizedTypeReference<>() {
                                    },
                                    customerCreateRequestDTO.getMerchantId(),
                                    servletRequest
                            );
                    endTime = System.currentTimeMillis();

                    log.info("======= Total Time to Update Relationship Customer (ms): " + (endTime - startTime));
                    log.info("========= Register Relationship Body Res: " + registerRelationship.toString());
                }
                log.info("======== Response done: " + response.toString());
            }
        } catch (Exception e) {
            throw new NextshopException(EResponseStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
        response.getData().relationshipType.add(CoCustomerDTO.RelationshipType.builder().type("CUSTOMER").build());
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, response.getData());
    }

    @Override
    public  CoreResponseObject<CoCustomerDTO> updateCustomer(String customerId, CustomerUpdateRequestDTO customerUpdateRequestDTO, HttpServletRequest servletRequest) {
        final String UPDATE_CUSTOMER_URL = CORE_API_URL + "/partner/"+customerId;
        try {
            Map<String, String> note = new HashMap<>();
            note.put("note", customerUpdateRequestDTO.getNote());

            Map<String, Object> body = new HashMap<>();
            body.put("name", customerUpdateRequestDTO.getName());
            body.put("phoneNo", customerUpdateRequestDTO.getPhoneNo());
            body.put("address", customerUpdateRequestDTO.getAddress());
            body.put("metaData", note);
            body.put("optimisticVersion", customerUpdateRequestDTO.getOptimisticVersion());

            startTime = System.currentTimeMillis();
            response = RestFactory.invokePatchRequest
                    (UPDATE_CUSTOMER_URL,
                            body,
                            new ParameterizedTypeReference<>() {
                            },
                            customerUpdateRequestDTO.getMerchantId(),
                            servletRequest
                    );
            endTime = System.currentTimeMillis();
            log.info("======= Total Time to Update Customer (ms): "+ (endTime - startTime));

            log.info("======== Response done: " + response.toString());

        } catch (Exception e) {
            throw new NextshopException(EResponseStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, response.getData());
    }

    @Override
    public  CoreResponseObject<CoCustomerDTO> getCustomerById(String customerId, HttpServletRequest servletRequest) {
        final String CREATE_CUSTOMER_URL = CORE_API_URL + "/partner/" + customerId;
        CoreResponseObject<CoCustomerDTO> response;
        try {
            response = RestFactory.invokeGetRequest
                    (CREATE_CUSTOMER_URL,
                            servletRequest.getHeader("np-merchant-id"),
                            servletRequest,
                            new ParameterizedTypeReference<>() {
                            }
                    );
            log.info(response.toString());
        } catch (Exception e) {
            throw new NextshopException(EResponseStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
        return new CoreResponseObject<>(true, ResponseStatus.SUCCESSFUL, response.getData());

    }

    public boolean isDataCreated(String strValue){
        if(strValue.equals("SUCCESS")){
            return true;
        } else{
            return false;
        }
    }

    public Object checkCustomerExistByPhone(String phoneNo, String merchantId, HttpServletRequest servletRequest) {
        final String SEARCH_CUSTOMER_URL = CORE_API_URL + "/partner/search";
        log.info("====== API Search Customer's Phone: " + SEARCH_CUSTOMER_URL);
        CoreResponseObject result = new CoreResponseObject();
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("queryString", " {phoneNo} == '''" + phoneNo + "'''");
            body.put("limit", 1);
            body.put("lastIndex", "");
            body.put("sort", "ASC");
            result = RestFactory.invokePostRequest
                    (SEARCH_CUSTOMER_URL,
                            body,
                            new ParameterizedTypeReference<>() {
                            },
                            merchantId,
                            servletRequest
                    );
        } catch (Exception e) {
            throw new NextshopException(EResponseStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
        if (result == null || !result.getSuccess() || result.getData() == null) {
            return null;
        } else {
            return result.getData();
        }
    }
}
