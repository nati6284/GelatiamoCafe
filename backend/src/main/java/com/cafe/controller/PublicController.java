package com.cafe.controller;
import com.cafe.model.*;
import com.cafe.dto.*;
import com.cafe.repository.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.util.stream.Collectors;
@RestController @RequestMapping("/api")
public class PublicController {
 private final MenuItemRepository menu;
 private final BranchRepository branchRepo;
 private final BranchMenuPriceRepository branchMenuRepo;
 
 public PublicController(MenuItemRepository menu, BranchRepository branchRepo, BranchMenuPriceRepository branchMenuRepo){
   this.menu=menu;
   this.branchRepo=branchRepo;
   this.branchMenuRepo=branchMenuRepo;
 }
 
 @GetMapping("/health") public Map<String,String> health(){return Map.of("status","ok");}
 
 @GetMapping("/branches") public List<Branch> branches(){return branchRepo.findByActiveTrue();}
 
 @GetMapping("/menu") public List<MenuItem> menu(){return menu.findByAvailableTrueOrderByCategoryAscNameAsc();}
 
 @GetMapping("/menu/branch/{branchCode}")
 public ResponseEntity<List<MenuItemDTO>> menuByBranch(@PathVariable String branchCode){
   Optional<Branch> branch = branchRepo.findByCode(branchCode);
   if(branch.isEmpty()) return ResponseEntity.notFound().build();
   
   List<BranchMenuPrice> prices = branchMenuRepo.findByBranchAndAvailableTrueOrderByMenuItemCategoryAscMenuItemNameAsc(branch.get());
   List<MenuItemDTO> items = prices.stream().map(p -> new MenuItemDTO(
     p.getMenuItem().getId(),
     p.getMenuItem().getName(),
     p.getMenuItem().getDescription(),
     p.getMenuItem().getCategory(),
     p.getPrice(),
     p.getMenuItem().getImage(),
     p.isAvailable(),
     p.getMenuItem().isPopular()
   )).collect(Collectors.toList());
   
   return ResponseEntity.ok(items);
 }
 
 @GetMapping("/menu/branch/{branchCode}/popular")
 public ResponseEntity<List<MenuItemDTO>> popularByBranch(@PathVariable String branchCode){
   Optional<Branch> branch = branchRepo.findByCode(branchCode);
   if(branch.isEmpty()) return ResponseEntity.notFound().build();
   
   List<BranchMenuPrice> prices = branchMenuRepo.findByBranchAndMenuItemPopularTrueAndAvailableTrue(branch.get());
   List<MenuItemDTO> items = prices.stream().map(p -> new MenuItemDTO(
     p.getMenuItem().getId(),
     p.getMenuItem().getName(),
     p.getMenuItem().getDescription(),
     p.getMenuItem().getCategory(),
     p.getPrice(),
     p.getMenuItem().getImage(),
     p.isAvailable(),
     p.getMenuItem().isPopular()
   )).collect(Collectors.toList());
   
   return ResponseEntity.ok(items);
 }
 
 @GetMapping("/menu/popular") public List<MenuItem> popular(){return menu.findByPopularTrueAndAvailableTrue();}
 @GetMapping("/menu/categories") public List<String> categories(){return menu.findByAvailableTrueOrderByCategoryAscNameAsc().stream().map(MenuItem::getCategory).distinct().toList();}
}
