package org.example.backend.f1.driver;

import org.example.backend.f1.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

}
