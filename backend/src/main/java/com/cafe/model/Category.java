package com.cafe.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity @Table(name="categories")
public class Category {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank @Size(max=80) @Column(unique=true) private String name;
  private int displayOrder = 0;

  public Category() {}
  public Category(String name, int displayOrder) { this.name = name; this.displayOrder = displayOrder; }

  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public int getDisplayOrder(){return displayOrder;} public void setDisplayOrder(int displayOrder){this.displayOrder=displayOrder;}
}
