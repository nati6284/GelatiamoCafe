package com.cafe.controller;
import com.cafe.model.*;
import com.cafe.dto.*;
import com.cafe.repository.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController @RequestMapping("/api")
public class PublicController {
  private final MenuItemRepository menu;
  private final BranchRepository branchRepo;
  private final BranchMenuPriceRepository branchMenuRepo;
  private final CategoryRepository categoryRepo;

  public PublicController(MenuItemRepository menu, BranchRepository branchRepo,
      BranchMenuPriceRepository branchMenuRepo, CategoryRepository categoryRepo) {
    this.menu = menu; this.branchRepo = branchRepo;
    this.branchMenuRepo = branchMenuRepo; this.categoryRepo = categoryRepo;
  }

  @GetMapping("/health") public Map<String,String> health(){return Map.of("status","ok");}

  @GetMapping("/branches") public List<Branch> branches(){return branchRepo.findByActiveTrue();}

  // Returns all categories in display order
  @GetMapping("/categories") public List<Category> categories(){
    return categoryRepo.findAllByOrderByDisplayOrderAscNameAsc();
  }

  @GetMapping("/menu") public List<MenuItem> menu(){
    return menu.findByAvailableTrueOrderByCategoryAscNameAsc();
  }

  @GetMapping("/menu/branch/{branchCode}")
  public ResponseEntity<List<MenuItemDTO>> menuByBranch(@PathVariable String branchCode){
    Optional<Branch> branch = branchRepo.findByCode(branchCode);
    if(branch.isEmpty()) return ResponseEntity.notFound().build();

    List<BranchMenuPrice> prices = branchMenuRepo
        .findByBranchAndAvailableTrueOrderByMenuItemCategoryAscMenuItemNameAsc(branch.get());
    List<MenuItemDTO> items = prices.stream().map(p -> new MenuItemDTO(
      p.getMenuItem().getId(), p.getMenuItem().getName(),
      p.getMenuItem().getDescription(), p.getMenuItem().getCategory(),
      p.getPrice(), p.getMenuItem().getImage(), p.getBranchImage(),
      p.isAvailable(), p.getMenuItem().isPopular()
    )).collect(Collectors.toList());

    return ResponseEntity.ok(items);
  }

  @GetMapping("/menu/branch/{branchCode}/popular")
  public ResponseEntity<List<MenuItemDTO>> popularByBranch(@PathVariable String branchCode){
    Optional<Branch> branch = branchRepo.findByCode(branchCode);
    if(branch.isEmpty()) return ResponseEntity.notFound().build();

    List<BranchMenuPrice> prices = branchMenuRepo
        .findByBranchAndMenuItemPopularTrueAndAvailableTrue(branch.get());
    List<MenuItemDTO> items = prices.stream().map(p -> new MenuItemDTO(
      p.getMenuItem().getId(), p.getMenuItem().getName(),
      p.getMenuItem().getDescription(), p.getMenuItem().getCategory(),
      p.getPrice(), p.getMenuItem().getImage(), p.getBranchImage(),
      p.isAvailable(), p.getMenuItem().isPopular()
    )).collect(Collectors.toList());

    return ResponseEntity.ok(items);
  }

  @GetMapping("/menu/popular") public List<MenuItem> popular(){return menu.findByPopularTrueAndAvailableTrue();}

  /**
   * Proxies an external image URL through the backend so the browser can load it
   * without CORS restrictions (used for print view).
   * Usage: GET /api/image-proxy?url=https://...
   */
  @GetMapping("/image-proxy")
  public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
    try {
      HttpClient client = HttpClient.newBuilder()
          .followRedirects(HttpClient.Redirect.NORMAL)
          .build();
      HttpRequest req = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("User-Agent", "Mozilla/5.0")
          .GET()
          .build();
      HttpResponse<byte[]> resp = client.send(req, HttpResponse.BodyHandlers.ofByteArray());
      String contentType = resp.headers().firstValue("Content-Type").orElse("image/jpeg");
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType(contentType));
      return new ResponseEntity<>(resp.body(), headers, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
  }
}
