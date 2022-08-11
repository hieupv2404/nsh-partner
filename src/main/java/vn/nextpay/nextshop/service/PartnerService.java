/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.service;

import vn.nextpay.nextshop.controller.dto.request.*;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.dao.entity.Partner;

import java.util.List;

public interface PartnerService {
    CoreResponseObject<Partner> createPartner(PartnerCreateDTO partnerCreateDTO, String type);
    CoreResponseObject<Partner> getPartnerById(FilterPartner filterPartner);
    CoreResponseObject<List<Partner>> getAllPartner(FilterPartner filterPartner, String type);
    CoreResponseObject<Partner> updatePartner(String id, PartnerUpdateDTO partnerUpdateDTO,  String type);
    CoreResponseObject<List<Partner>> deletePartners(PartnerListDeleteDTO partnerListDeleteDTO,  String type);
    CoreResponseObject<List<Partner>> getPartnersByMerchantId(FilterPartner filterPartner);
    CoreResponseObject<Partner> updateContact(String id, ContactCreateDTO ContactCreateDTO);
    CoreResponseObject<String> deletePartnerByMerchant(String merchantId);

}
