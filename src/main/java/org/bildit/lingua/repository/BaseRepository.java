package org.bildit.lingua.repository;

import java.io.Serializable;

import org.bildit.lingua.common.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository <T extends BaseEntity, K extends Serializable> extends JpaRepository <T, Long> {
	
	T save(T entity);
	  
}
