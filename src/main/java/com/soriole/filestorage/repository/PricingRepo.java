package com.filestorage.repository;

import com.filestorage.model.db.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public interface PricingRepo extends JpaRepository<Pricing,Long> {
}
