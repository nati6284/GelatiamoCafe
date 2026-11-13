package com.cafe.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name="branches")
public class Branch {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank @Size(max=120) private String name;
  @NotBlank @Size(max=500) private String address;
  @Size(max=20) private String phone;
  @NotBlank @Size(max=100) @Column(unique=true) private String code; // unique identifier for QR codes
  @Size(max=500) private String mapUrl; // Google Maps link
  @Size(max=200) private String openingHours; // e.g. "Every day · 8:00 AM – 10:00 PM"
  private Double latitude;
  private Double longitude;
  private boolean active = true;
  
  public Branch() {}
  public Branch(String name, String address, String phone, String code) {
    this.name = name;
    this.address = address;
    this.phone = phone;
    this.code = code;
  }
  public Branch(String name, String address, String phone, String code, String mapUrl) {
    this.name = name;
    this.address = address;
    this.phone = phone;
    this.code = code;
    this.mapUrl = mapUrl;
  }
  
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public String getAddress(){return address;} public void setAddress(String address){this.address=address;}
  public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
  public String getCode(){return code;} public void setCode(String code){this.code=code;}
  public String getMapUrl(){return mapUrl;} public void setMapUrl(String mapUrl){this.mapUrl=mapUrl;}
  public String getOpeningHours(){return openingHours;} public void setOpeningHours(String openingHours){this.openingHours=openingHours;}
  public Double getLatitude(){return latitude;} public void setLatitude(Double latitude){this.latitude=latitude;}
  public Double getLongitude(){return longitude;} public void setLongitude(Double longitude){this.longitude=longitude;}
  public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
}
