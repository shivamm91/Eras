package com.fabhotel.Eras.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class EndorsementEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	private String reviewerId;

	private String revieweeId;

	@ManyToOne
	@JoinColumn(name = "skill_id")
	private SkillEntity skill;

	private int userScore;

	private int adjustedScore;

	private String adjustmentReason;

	// We can have other fields as well like created_at, updated_at, created_by,
	// updated_by, etc.
}