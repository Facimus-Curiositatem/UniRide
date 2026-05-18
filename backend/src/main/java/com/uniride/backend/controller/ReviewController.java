package com.uniride.backend.controller;

import com.uniride.backend.model.Review;
import com.uniride.backend.model.User;
import com.uniride.backend.repository.ReviewRepository;
import com.uniride.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> payload, Authentication authentication) {
        String email = authentication.getName();
        User reviewer = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Long reviewedId = Long.valueOf(payload.get("reviewedId").toString());
        Integer rating = (Integer) payload.get("rating");
        String comment = (String) payload.get("comment");
        
        User reviewed = userRepository.findById(reviewedId)
            .orElseThrow(() -> new RuntimeException("Usuario a calificar no encontrado"));
        
        Review review = Review.builder()
            .reviewer(reviewer)
            .reviewed(reviewed)
            .rating(rating)
            .comment(comment)
            .build();
        
        reviewRepository.save(review);
        
        // Actualizar rating promedio
        Double avgRating = reviewRepository.getAverageRatingForUser(reviewedId);
        reviewed.setRating(avgRating);
        userRepository.save(reviewed);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Calificación guardada");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
public ResponseEntity<?> getMyReviews(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
    List<Review> reviews = reviewRepository.findByReviewedId(user.getId());
    
    List<Map<String, Object>> response = reviews.stream().map(r -> {
        Map<String, Object> map = new HashMap<>();
        map.put("id", r.getId());
        map.put("rating", r.getRating());
        map.put("comment", r.getComment());
        map.put("reviewerName", r.getReviewer().getFullName());
        map.put("reviewerFaculty", "Javeriana");
        map.put("createdAt", r.getCreatedAt());
        return map;
    }).collect(Collectors.toList());
    
    return ResponseEntity.ok(response);
}
}