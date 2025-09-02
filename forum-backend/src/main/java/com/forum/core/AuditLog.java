package com.forum.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @Column(name = "actor_id")
    private Long actorId;
}


