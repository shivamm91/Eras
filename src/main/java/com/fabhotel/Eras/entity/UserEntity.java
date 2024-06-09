package com.fabhotel.Eras.entity;




import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private int yearsOfExperience;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_skill_mapping",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<SkillEntity> skills;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_company_mapping",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "company_details_id"))
    private List<CompanyEntity> companies;
    
    // We can have other fields as well like created_at, updated_at, created_by, updated_by, etc.
}