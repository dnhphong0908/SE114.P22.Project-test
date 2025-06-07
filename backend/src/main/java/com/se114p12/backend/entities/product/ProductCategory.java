package com.se114p12.backend.entities.product;

import com.se114p12.backend.entities.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "product_categories")
@Data
public class ProductCategory extends BaseEntity {

  @Column(unique = true)
  private String name;

  private String description;

  @OneToMany(mappedBy = "category")
  private List<Product> product;

  @Column(length = 500)
  private String imageUrl;
}
