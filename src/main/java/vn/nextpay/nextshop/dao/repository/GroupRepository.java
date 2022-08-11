/*
 *  Created by: HieuPV
 *  Mail: hieupv@mpos.vn
 */

package vn.nextpay.nextshop.dao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import vn.nextpay.nextshop.dao.entity.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> findGroupByIdAndIsArchiveAndType(String id, boolean isArchive, String type);
    Optional<Group> findGroupByIdAndIsArchiveAndTypeOrNameAndIsArchiveAndType(String id, boolean isArchive, String type, String name, boolean isArchive2, String type2);

    @Query(value = "{$and: [{ $or: [{'_id' : ?0}, {'name' : ?0 }] }, {'isArchive':false},{'type':?1} ]}")
    Optional<Group> findGroupByIdOrNameWithIsArchiveAndType(String query,String type);
    Optional<Group> findGroupByIdAndIsArchive(String id, boolean isArchive);
    Optional<Group> findGroupByNameAndIsArchiveAndTypeAndMerchantId(String name, boolean isArchive, String type, String merchantId);
    List<Group> findGroupByIsArchiveAndType(boolean isArchive,  String type);
    List<Group> findGroupByIsArchiveAndTypeAndMerchantId(boolean isArchive,  String type, String merchantId);
    List<Group> findGroupByIsArchiveAndMerchantId(boolean isArchive,  String merchantId);

    Long countByIsDefaultGroupAndIsArchiveAndType(boolean isDefault, boolean isArchive, String type);

}
