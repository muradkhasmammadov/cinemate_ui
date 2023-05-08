package com.esiproject2023.emailservice.repository;

import com.esiproject2023.emailservice.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
