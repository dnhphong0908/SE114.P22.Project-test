package com.se114p12.backend.controllers.storage;

import com.se114p12.backend.constants.AppConstant;
import com.se114p12.backend.services.general.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(AppConstant.API_BASE_PATH + "/storage")
@RequiredArgsConstructor
@RestController
public class StorageController {

  private final StorageService storageService;

  @GetMapping(value = "/images", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<Resource> getImageStoragePath(@RequestParam("url") String url) {
    return ResponseEntity.status(HttpStatus.OK).body(storageService.loadAsResource(url));
  }
}
