package com.example.payflow.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.payflow.exceptions.MediaUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaService {

    private final Cloudinary cloudinary;


    public UploadResult uploadImage(MultipartFile file, String folder) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File too large (max 5 MB)");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image uploads are allowed");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", "payflow/" + folder,
                    "resource_type", "image",
                    "transformation", "c_limit,w_500,h_500,q_auto,f_auto"
                )
            );

            return new UploadResult(
                (String) result.get("secure_url"),
                (String) result.get("public_id")
            );
        } catch (IOException e) {
            log.error("Cloudinary upload failed", e);
            throw new MediaUploadException("Failed to upload image");
        }
    }

    @Async("taskExecutor")
    public void deleteImage(String publicId) {
        if (publicId == null) return;
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.warn("Failed to delete image {}", publicId, e);
            // don't fail the request — deleting an old avatar is not critical
        }
    }

    public record UploadResult(String url, String publicId) {}
}