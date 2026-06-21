package com.campus.lostfound.repository;

import com.campus.lostfound.model.LostItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    List<LostItem> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCaseOrLocationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(
            String title, String category, String location, String description);

    List<LostItem> findAllByOrderByCreatedAtDesc();
}
