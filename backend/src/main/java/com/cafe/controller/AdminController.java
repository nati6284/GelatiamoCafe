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
  private final CategoryRepository categoryRepo;
  private final AdminTokenService tokens;
  private final AdminGuard guard;

  @Value("${app.admin.username}") private String adminUsername;
  @Value("${app.admin.password}") private String adminPassword;

  public AdminController(MenuItemRepository menu, BranchRepository branchRepo,
      BranchMenuPriceRepository branchMenuRepo, CategoryRepository categoryRepo,
      AdminTokenService tokens, AdminGuard guard) {
    this.menu = menu; this.branchRepo = branchRepo;
    this.branchMenuRepo = branchMenuRepo; this.categoryRepo = categoryRepo;
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

  // ── Categories ──────────────────────────────────────────────────────────────

  @GetMapping("/categories")
  public List<Category> getCategories(HttpServletRequest r) {
    guard.check(r);
    return categoryRepo.findAllByOrderByDisplayOrderAscNameAsc();
  }

  @PostMapping("/categories") @ResponseStatus(HttpStatus.CREATED)
  public Category createCategory(@Valid @RequestBody Category category, HttpServletRequest r) {
    guard.check(r);
    category.setId(null);
    return categoryRepo.save(category);
  }

  @PutMapping("/categories/{id}")
  public Category updateCategory(@PathVariable Long id, @Valid @RequestBody Category category, HttpServletRequest r) {
    guard.check(r);
    Category existing = categoryRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    existing.setName(category.getName());
    existing.setDisplayOrder(category.getDisplayOrder());
    return categoryRepo.save(existing);
  }

  @DeleteMapping("/categories/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable Long id, HttpServletRequest r) {
    guard.check(r);
    categoryRepo.deleteById(id);
  }

  // ── Menu Item Catalog (global) ─────────────────────────────────────────────
  // These manage the catalog only. Branch assignment is done separately.

  @GetMapping("/menu")
  public List<MenuItem> getAllMenuItems(HttpServletRequest r) {
    guard.check(r);
    return menu.findAllByOrderByCategoryAscNameAsc();
  }

  @PostMapping("/menu") @ResponseStatus(HttpStatus.CREATED)
  public MenuItem createMenu(@Valid @RequestBody MenuItem item, HttpServletRequest r) {
    guard.check(r);
    item.setId(null);
    // Save to catalog only — no automatic branch assignment
    return menu.save(item);
  }

  @PutMapping("/menu/{id}")
  public MenuItem updateMenu(@PathVariable Long id, @Valid @RequestBody MenuItem item, HttpServletRequest r) {
    guard.check(r);
    MenuItem existing = menu.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    existing.setName(item.getName()); existing.setDescription(item.getDescription());
    existing.setCategory(item.getCategory()); existing.setPrice(item.getPrice());
    existing.setImage(item.getImage()); existing.setAvailable(item.isAvailable());
    existing.setPopular(item.isPopular());
    return menu.save(existing);
  }

  @DeleteMapping("/menu/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteMenu(@PathVariable Long id, HttpServletRequest r) {
    guard.check(r);
    MenuItem item = menu.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    branchMenuRepo.deleteByMenuItem(item);
    menu.deleteById(id);
  }

  // ── Branches ──────────────────────────────────────────────────────────────

  @GetMapping("/branches")
  public List<Branch> getAllBranches(HttpServletRequest r) { guard.check(r); return branchRepo.findAll(); }

  @PostMapping("/branches") @ResponseStatus(HttpStatus.CREATED)
  public Branch createBranch(@Valid @RequestBody Branch branch, HttpServletRequest r) {
    guard.check(r);
    branch.setId(null);
    // Save branch only — no automatic menu item assignment
    return branchRepo.save(branch);
  }

  @PutMapping("/branches/{id}")
  public Branch updateBranch(@PathVariable Long id, @Valid @RequestBody Branch branch, HttpServletRequest r) {
    guard.check(r);
    Branch existing = branchRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
    Branch branch = branchRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    branchMenuRepo.deleteByBranch(branch);
    branchRepo.deleteById(id);
  }

  // ── Branch menu: list, add, remove, update ────────────────────────────────

  /**
   * Returns only items explicitly assigned to this branch (no auto-fill).
   */
  @GetMapping("/branches/{branchId}/menu")
  public List<BranchMenuPrice> getBranchMenu(@PathVariable Long branchId, HttpServletRequest r) {
    guard.check(r);
    Branch branch = branchRepo.findById(branchId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return branchMenuRepo.findByBranchOrderByMenuItemCategoryAscMenuItemNameAsc(branch);
  }

  /**
   * Add a menu item to a branch (with optional price / image override).
   */
  @PostMapping("/branches/{branchId}/menu/{menuItemId}") @ResponseStatus(HttpStatus.CREATED)
  public BranchMenuPrice addItemToBranch(@PathVariable Long branchId,
      @PathVariable Long menuItemId,
      @RequestBody(required = false) Map<String,Object> body,
      HttpServletRequest r) {
    guard.check(r);
    Branch branch = branchRepo.findById(branchId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    MenuItem item = menu.findById(menuItemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (branchMenuRepo.findByBranchAndMenuItem(branch, item).isPresent())
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Item already in branch menu");

    BranchMenuPrice bmp = new BranchMenuPrice(branch, item, item.getPrice());
    if (body != null) {
      if (body.containsKey("price") && body.get("price") != null)
        bmp.setPrice(((Number) body.get("price")).doubleValue());
      if (body.containsKey("available"))
        bmp.setAvailable(Boolean.TRUE.equals(body.get("available")));
      if (body.containsKey("branchImage"))
        bmp.setBranchImage((String) body.get("branchImage"));
    }
    return branchMenuRepo.save(bmp);
  }

  /**
   * Remove a menu item from a branch.
   */
  @DeleteMapping("/branches/{branchId}/menu/{menuItemId}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeItemFromBranch(@PathVariable Long branchId,
      @PathVariable Long menuItemId, HttpServletRequest r) {
    guard.check(r);
    Branch branch = branchRepo.findById(branchId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    MenuItem item = menu.findById(menuItemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    BranchMenuPrice bmp = branchMenuRepo.findByBranchAndMenuItem(branch, item)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    branchMenuRepo.delete(bmp);
  }

  /**
   * Update price / availability / branch image for an item already in a branch menu.
   */
  @PutMapping("/branches/{branchId}/menu/{menuItemId}")
  public BranchMenuPrice updateBranchMenuItem(@PathVariable Long branchId,
      @PathVariable Long menuItemId, @RequestBody Map<String,Object> body, HttpServletRequest r) {
    guard.check(r);
    Branch branch = branchRepo.findById(branchId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    MenuItem item = menu.findById(menuItemId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    BranchMenuPrice bmp = branchMenuRepo.findByBranchAndMenuItem(branch, item)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not in branch menu"));

    if (body.containsKey("price") && body.get("price") != null)
      bmp.setPrice(((Number) body.get("price")).doubleValue());
    if (body.containsKey("available"))
      bmp.setAvailable(Boolean.TRUE.equals(body.get("available")));
    if (body.containsKey("branchImage"))
      bmp.setBranchImage((String) body.get("branchImage"));

    return branchMenuRepo.save(bmp);
  }

  // ── Legacy endpoint kept for backward compat (redirects to new) ───────────
  @GetMapping("/branches/{branchId}/prices")
  public List<BranchMenuPrice> getBranchPrices(@PathVariable Long branchId, HttpServletRequest r) {
    return getBranchMenu(branchId, r);
  }

  @PutMapping("/branches/{branchId}/prices/{menuItemId}")
  public BranchMenuPrice updateBranchPrice(@PathVariable Long branchId,
      @PathVariable Long menuItemId, @RequestBody Map<String,Object> body, HttpServletRequest r) {
    return updateBranchMenuItem(branchId, menuItemId, body, r);
  }
}
