/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.service;

import vn.nextpay.nextshop.controller.dto.request.FilterPartner;
import vn.nextpay.nextshop.controller.dto.request.GroupCreateDTO;
import vn.nextpay.nextshop.controller.dto.request.GroupDeleteDTO;
import vn.nextpay.nextshop.controller.dto.request.GroupUpdateDTO;
import vn.nextpay.nextshop.controller.dto.response.CoreResponseObject;
import vn.nextpay.nextshop.dao.entity.Group;
import vn.nextpay.nextshop.dao.entity.Partner;

import java.util.List;

public interface GroupService {
    CoreResponseObject<List<Group>> getAllGroups(FilterPartner filterPartner, String type);
    CoreResponseObject<Group> createGroup(GroupCreateDTO groupCreateDTO, String  type);
    CoreResponseObject<Group> getGroupById(String groupId);
    CoreResponseObject<Group> updateGroup(String id, GroupUpdateDTO groupUpdateDTO, String type);
    CoreResponseObject<List<Group>> deleteGroup(GroupDeleteDTO groupDeleteDTO, String type);
    CoreResponseObject<String> initGroup();
    CoreResponseObject<String> deleteGroupByMerchant(String merchantId);
}
