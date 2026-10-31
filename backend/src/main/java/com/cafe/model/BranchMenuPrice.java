package com.cafe.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="branch_menu_prices", uniqueConstraints = @UniqueConstraint(columnNames = {"branch_id", "menu_item_id"}))
public class BranchMenuPrice {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  
  @ManyToOne @JoinColumn(name="branch_id", nullable=false, foreignKey=@ForeignKey(name="fk_bmp_branch")) private Branch branch;
  @ManyToOne @JoinColumn(name="menu_item_id", nullable=false, foreignKey=@ForeignKey(name="fk_bmp_menuitem")) private MenuItem menuItem;
  
  @NotNull @DecimalMin("0.0") private Double price;
  private boolean available = true;
  
  public BranchMenuPrice() {}
  public BranchMenuPrice(Branch branch, MenuItem menuItem, Double price) {
    this.branch = branch;
    this.menuItem = menuItem;
    this.price = price;
  }
  
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public Branch getBranch(){return branch;} public void setBranch(Branch branch){this.branch=branch;}
  public MenuItem getMenuItem(){return menuItem;} public void setMenuItem(MenuItem menuItem){this.menuItem=menuItem;}
  public Double getPrice(){return price;} public void setPrice(Double price){this.price=price;}
  public boolean isAvailable(){return available;} public void setAvailable(boolean available){this.available=available;}
}
