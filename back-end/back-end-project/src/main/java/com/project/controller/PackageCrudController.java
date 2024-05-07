package com.project.controller;

import com.project.controller.path.ProjectPath;
import com.project.model.Package;
import com.project.service.packages.PackageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ProjectPath.PACKAGE)
@Tag(name = "PackageCrudController", description = "Package CRUD Service API")
public class PackageCrudController {

  private final PackageService packageService;

  @PostMapping(ProjectPath.CREATE)
  public Package savePackage(@RequestParam String name, @RequestParam Integer price) {
    return this.packageService.savePackage(name, price);
  }

  @PostMapping(ProjectPath.UPDATE)
  public Package updatePackage(@RequestParam Integer id, @RequestParam String name,
      @RequestParam Integer price) {
    return this.packageService.updatePackage(id, name, price);
  }

  @DeleteMapping(ProjectPath.DELETE)
  public boolean deletePackage(Integer id) throws Exception {
    return packageService.deletePackage(id);
  }

  @GetMapping(ProjectPath.FIND_BY_ID)
  public Package findById(Integer id) {
    return packageService.findById(id);
  }
}