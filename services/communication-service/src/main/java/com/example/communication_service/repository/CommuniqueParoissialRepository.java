package com.example.communication_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.communication_service.entity.CommuniqueParoissial;

public interface CommuniqueParoissialRepository
		extends JpaRepository<CommuniqueParoissial, Long>, JpaSpecificationExecutor<CommuniqueParoissial> {
}
