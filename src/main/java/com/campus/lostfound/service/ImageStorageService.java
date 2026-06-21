package com.campus.lostfound.service;

import com.campus.lostfound.model.LostItem;
import java.io.IOException;
import java.util.Base64;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");

    public void store(LostItem item, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return;
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPG, PNG, GIF, and WEBP images are allowed.");
        }

        item.setImageContentType(file.getContentType());
        item.setImageData(Base64.getEncoder().encodeToString(file.getBytes()));
    }
}
