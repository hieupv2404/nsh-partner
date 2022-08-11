/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import vn.nextpay.nextshop.dao.entity.Partner;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends MongoRepository<Partner, String> {

    Optional<Partner> findPartnerByPhoneNoAndIsArchiveAndRelationshipTypesAndMerchantId(String phoneNo, boolean isArchive, String type, String merchantId);
    List<Partner> findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContaining(String merchantId1, String type, boolean isArchive, String name, String merchantId2,  String type2, boolean isArchive2 , String phoneNo);
    List<Partner> findAllByMerchantIdAndRelationshipTypesAndIsArchiveAndFullNameSortIsContainingAndIsPersonOrMerchantIdAndRelationshipTypesAndIsArchiveAndPhoneNoIsContainingAndIsPerson(String merchantId1, String type, boolean isArchive, String name, boolean isPerson, String merchantId2,  String type2, boolean isArchive2 , String phoneNo, boolean isPerson1);
    Optional<Partner> findAllByIdAndIsArchiveAndMerchantId(String id, boolean isArchive, String merchantId);
    Optional<Partner> findAllByIdAndIsArchive(String id, boolean isArchive);

    @Query(value = "{$and: [{'groups._id':?0}, {'isArchive':false},{'relationshipTypes':?1} ]}")
    List<Partner> findPartnerByGroupId(String name, String type);

    @Query(value = "{$and: [{ $or: [{'phoneNo' : ?0}, {'name' : ?0 }] }, {'isArchive':false},{'merchantId':?1} ]}")
    List<Partner> findPartnerByMerchantId(String query, String merchantId);

    List<Partner> findAllByIsArchiveAndMerchantId(boolean isArchive, String merchantId);

}
