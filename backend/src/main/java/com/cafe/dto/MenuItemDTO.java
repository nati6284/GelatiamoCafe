package com.cafe.dto;
public class MenuItemDTO {
  private Long id;
  private String name;
  private String description;
  private String category;
  private Double price;
  private String image;        // effective image (branchImage if set, else MenuItem.image)
  private String branchImage;  // branch-specific override (null if not set)
  private boolean available;
  private boolean popular;

  public MenuItemDTO(Long id, String name, String description, String category,
      Double price, String image, String branchImage, boolean available, boolean popular) {
    this.id = id; this.name = name; this.description = description;
    this.category = category; this.price = price;
    // resolve effective image: branch override takes priority
    this.branchImage = branchImage;
    this.image = (branchImage != null && !branchImage.isBlank()) ? branchImage : image;
    this.available = available; this.popular = popular;
  }

  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public String getDescription(){return description;} public void setDescription(String description){this.description=description;}
  public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
  public Double getPrice(){return price;} public void setPrice(Double price){this.price=price;}
  public String getImage(){return image;} public void setImage(String image){this.image=image;}
  public String getBranchImage(){return branchImage;} public void setBranchImage(String branchImage){this.branchImage=branchImage;}
  public boolean isAvailable(){return available;} public void setAvailable(boolean available){this.available=available;}
  public boolean isPopular(){return popular;} public void setPopular(boolean popular){this.popular=popular;}
}
