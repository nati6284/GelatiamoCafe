package com.cafe.repository;
import com.cafe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findAllByOrderByDisplayOrderAscNameAsc();
  Optional<Category> findByName(String name);
}
