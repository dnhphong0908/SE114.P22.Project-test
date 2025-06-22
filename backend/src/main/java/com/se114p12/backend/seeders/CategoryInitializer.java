package com.se114p12.backend.seeders;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.entities.product.Product;
import com.se114p12.backend.entities.product.ProductCategory;
import com.se114p12.backend.repositories.product.ProductCategoryRepository;
import com.se114p12.backend.services.general.StorageService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryInitializer {
  private final StorageService storageService;
  private final ProductCategoryRepository productCategoryRepository;
  private final JsonLoader jsonLoader;

  @Transactional
  public void initializeCategories() {
    List<ProductCategory> categories = jsonLoader.loadCategoriesFromJson();
    System.out.println("Loaded categories: " + categories.size());
    System.out.println("-- Initializing categories --");
    for (ProductCategory category : categories) {
      System.out.println("Processing category: " + category.getName());
      for (Product product : category.getProducts()) {
        String path = saveImageFromUrl(product.getImageUrl(), AppConstant.PRODUCT_FOLDER);
        product.setImageUrl(path);
        product.setIsAvailable(true);
      }
      String path = saveImageFromUrl(category.getImageUrl(), AppConstant.CATEGORY_FOLDER);
      category.setImageUrl(path);
    }
    productCategoryRepository.saveAll(categories);
    System.out.println("-- Categories initialized successfully --");
  }

  // Util method to save a image file from a URL
  private String saveImageFromUrl(String imageUrl, String folderName) {
    try {
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(imageUrl)).GET().build();
      try {
        HttpResponse<InputStream> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
          throw new RuntimeException("Failed to fetch image from URL: " + imageUrl);
        }
        try (InputStream inputStream = response.body()) {
          return storageService.storeByInputStream(inputStream, folderName);
        } catch (IOException e) {
          throw new RuntimeException("Failed to save image from URL: " + imageUrl, e);
        }
      } catch (MalformedURLException e) {
        throw new RuntimeException("Invalid image URL: " + imageUrl, e);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error fetching image from URL: " + imageUrl, e);
    }
  }
}
