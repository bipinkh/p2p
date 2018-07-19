package com.soriole.filestorage.repository;

import com.soriole.filestorage.model.db.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author bipin khatiwada
 * github.com/bipinkh
 */
public interface PricingRepo extends JpaRepository<Pricing,Long> {
}
