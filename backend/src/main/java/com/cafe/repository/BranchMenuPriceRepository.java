package com.cafe.repository;
import com.cafe.model.BranchMenuPrice;
import com.cafe.model.Branch;
import com.cafe.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface BranchMenuPriceRepository extends JpaRepository<BranchMenuPrice, Long> {
  List<BranchMenuPrice> findByBranchAndAvailableTrueOrderByMenuItemCategoryAscMenuItemNameAsc(Branch branch);
  List<BranchMenuPrice> findByBranchOrderByMenuItemCategoryAscMenuItemNameAsc(Branch branch);
  List<BranchMenuPrice> findByBranchAndMenuItemPopularTrueAndAvailableTrue(Branch branch);
  Optional<BranchMenuPrice> findByBranchAndMenuItem(Branch branch, MenuItem menuItem);
  @Transactional void deleteByMenuItem(MenuItem menuItem);
  @Transactional void deleteByBranch(Branch branch);
}
