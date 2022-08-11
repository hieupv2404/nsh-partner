/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.controller.dto;

import cloud.nextsol.core.models.Contact;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import vn.nextpay.nextshop.dao.entity.Partner;

public class ContactMapper extends PropertyMap<Partner.Contact, Contact> {
    @Override
    protected void configure() {
        map().setEmail(source.getEmail());
        map().setName(source.getName());
        map().setPhone(source.getPhone());
        map().setRule(source.getRule());
//        destination.setEmail(source.getEmail());
//        destination().setName(source.getName());
//        destination().setPhone(source.getPhone());
//        destination().setRule(source.getRule());
    }
}

