package com.cafe.controller;
import com.cafe.config.*;
import com.cafe.model.*;
import com.cafe.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController @RequestMapping("/api/admin")
public class AdminController {
  private final MenuItemRepository menu;
  private final BranchRepository branchRepo;
  private final BranchMenuPriceRepository branchMenuRepo;
  private final AdminTokenService tokens;
  private final AdminGuard guard;
  @Value("${app.admin.username}") private String adminUsername;
  @Value("${app.admin.password}") private String adminPassword;

  public AdminController(MenuItemRepository menu, BranchRepository branchRepo,
      BranchMenuPriceRepository branchMenuRepo, AdminTokenService tokens, AdminGuard guard) {
    this.menu = menu; this.branchRepo = branchRepo; this.branchMenuRepo = branchMenuRepo;
    this.tokens = tokens; this.guard = guard;
  }

  @PostMapping("/login")
  public Map<String,String> login(@RequestBody Map<String,String> body) {
    if (!Objects.equals(adminUsername, body.get("username")) || !Objects.equals(adminPassword, body.get("password")))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    return Map.of("token", tokens.create());
  }

  @PostMapping("/logout")
  public Map<String,String> logout(HttpServletRequest request) {
    guard.check(request);
    String auth = request.getHeader("Authorization");
    tokens.revoke(auth.substring(7));
    return Map.of("message", "Signed out");
  }

  // ── Menu ──────────────────────────────────────────────────────────────────

  @PostMapping("/menu") @ResponseStatus(HttpStatus.CREATED)
  public MenuItem createMenu(@Valid @RequestBody MenuItem item, HttpServletRequest r) {
    guard.check(r);
    item.setId(null);
    MenuItem saved = menu.save(item);
    // auto-create a BranchMenuPrice for every existing branch
    for (Branch branch : branchRepo.findAll()) {
      branchMenuRepo.save(new BranchMenuPrice(branch, saved, saved.getPrice()));
    }
    return saved;
  }

  @PutMapping("/menu/{id}")
  public MenuItem updateMenu(@PathVariable Long id, @Valid @RequestBody MenuItem item, HttpServletRequest r) {
    guard.check(r);
    MenuItem existing = menu.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    existing.setName(item.getName()); existing.setDescription(item.getDescription());
    existing.setCategory(item.getCategory()); existing.setPrice(item.getPrice());
    existing.setImage(item.getImage()); existing.setAvailable(item.isAvailable());
    return menu.save(existing);
  }

  @DeleteMapping("/menu/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteMenu(@PathVariable Long id, HttpServletRequest r) {
    guard.check(r);
    menu.deleteById(id);
  }

  // ── Branches ──────────────────────────────────────────────────────────────

  @GetMapping("/branches")
  public List<Branch> getAllBranches(HttpServletRequest r) { guard.check(r); return branchRepo.findAll(); }

  @PostMapping("/branches") @ResponseStatus(HttpStatus.CREATED)
  public Branch createBranch(@Valid @RequestBody Branch branch, HttpServletRequest r) {
    guard.check(r);
    branch.setId(null);
    Branch saved = branchRepo.save(branch);
    // auto-create price rows for every existing menu item
    for (MenuItem mi : menu.findAll()) {
      branchMenuRepo.save(new BranchMenuPrice(saved, mi, mi.getPrice()));
    }
    return saved;
  }

  @PutMapping("/branches/{id}")
  public Branch updateBranch(@PathVariable Long id, @Valid @RequestBody Branch branch, HttpServletRequest r) {
    guard.check(r);
    Branch existing = branchRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    existing.setName(branch.getName()); existing.setAddress(branch.getAddress());
    existing.setPhone(branch.getPhone()); existing.setCode(branch.getCode());
    existing.setMapUrl(branch.getMapUrl());
    existing.setLatitude(branch.getLatitude()); existing.setLongitude(branch.getLongitude());
    existing.setActive(branch.isActive());
    return branchRepo.save(existing);
  }

  @DeleteMapping("/branches/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBranch(@PathVariable Long id, HttpServletRequest r) {
    guard.check(r);
    branchRepo.deleteById(id);
  }

  // ── Branch pricing ────────────────────────────────────────────────────────

  @GetMapping("/branches/{branchId}/prices")
  public List<BranchMenuPrice> getBranchPrices(@PathVariable Long branchId, HttpServletRequest r) {
    guard.check(r);
    Branch branch = branchRepo.findById(branchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    // fetch existing price rows
    List<BranchMenuPrice> existing = new ArrayList<>(
      branchMenuRepo.findByBranchAndAvailableTrueOrderByMenuItemCategoryAscMenuItemNameAsc(branch));
    Set<Long> existingIds = new HashSet<>();
    existing.forEach(p -> existingIds.add(p.getMenuItem().getId()));
    // create missing rows for any new menu items
    for (MenuItem mi : menu.findAll()) {
      if (!existingIds.contains(mi.getId())) {
        existing.add(branchMenuRepo.save(new BranchMenuPrice(branch, mi, mi.getPrice())));
      }
    }
    existing.sort(Comparator.comparing((BranchMenuPrice p) -> p.getMenuItem().getCategory())
      .thenComparing(p -> p.getMenuItem().getName()));
    return existing;
  }

  @PutMapping("/branches/{branchId}/prices/{menuItemId}")
  public BranchMenuPrice updateBranchPrice(@PathVariable Long branchId, @PathVariable Long menuItemId,
      @RequestBody Map<String,Double> body, HttpServletRequest r) {
    guard.check(r);
    Branch branch = branchRepo.findById(branchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    MenuItem item = menu.findById(menuItemId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    List<BranchMenuPrice> prices = branchMenuRepo.findByBranchAndAvailableTrueOrderByMenuItemCategoryAscMenuItemNameAsc(branch);
    BranchMenuPrice price = prices.stream().filter(p -> p.getMenuItem().getId().equals(menuItemId))
      .findFirst().orElse(new BranchMenuPrice(branch, item, body.get("price")));
    price.setPrice(body.get("price"));
    return branchMenuRepo.save(price);
  }
}
