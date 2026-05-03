package com.uniride.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;
    
    @ManyToOne
    @JoinColumn(name = "reviewed_id")
    private User reviewed;
    
    private Integer rating;
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}