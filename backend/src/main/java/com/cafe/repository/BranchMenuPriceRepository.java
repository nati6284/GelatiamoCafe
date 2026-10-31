package com.cafe.repository;
import com.cafe.model.BranchMenuPrice;
import com.cafe.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
public interface BranchMenuPriceRepository extends JpaRepository<BranchMenuPrice,Long> {
 List<BranchMenuPrice> findByBranchAndAvailableTrueOrderByMenuItemCategoryAscMenuItemNameAsc(Branch branch);
 List<BranchMenuPrice> findByBranchAndMenuItemPopularTrueAndAvailableTrue(Branch branch);
 @Transactional void deleteByMenuItem(com.cafe.model.MenuItem menuItem);
 @Transactional void deleteByBranch(com.cafe.model.Branch branch);
}
