CREATE DATABASE ERAS;

USE ERAS;

-- Insert data into CompanyEntity
INSERT INTO company_entity (name) VALUES ('Company A');
INSERT INTO company_entity (name) VALUES ('Company B');
INSERT INTO company_entity (name) VALUES ('Company C');

-- Insert data into SkillEntity
INSERT INTO skill_entity (name, parent_skill_id) VALUES ('Java', NULL);
INSERT INTO skill_entity (name, parent_skill_id) VALUES ('Spring', 1);
INSERT INTO skill_entity (name, parent_skill_id) VALUES ('Hibernate', 2);

-- Insert data into UserEntity
INSERT INTO user_entity (name, years_of_experience) VALUES ('Shivam', 5); 
INSERT INTO user_entity (name, years_of_experience) VALUES ('Vivek', 3);   
INSERT INTO user_entity (name, years_of_experience) VALUES ('Ashutosh', 10); 

-- Insert data into user_skill_mapping
INSERT INTO user_skill_mapping (user_id, skill_id) VALUES (1, 1); -- Shivam knows Java
INSERT INTO user_skill_mapping (user_id, skill_id) VALUES (1, 2); -- Shivam knows Spring
INSERT INTO user_skill_mapping (user_id, skill_id) VALUES (2, 1); -- Vivek knows Java
INSERT INTO user_skill_mapping (user_id, skill_id) VALUES (3, 1); -- Ashutosh knows Java
INSERT INTO user_skill_mapping (user_id, skill_id) VALUES (3, 2); -- Ashutosh knows Spring

INSERT INTO user_skill_mapping (user_id, skill_id) VALUES (2, 2); -- Vivek knows Spring

-- Insert data into user_company_mapping
INSERT INTO user_company_mapping (user_id, company_details_id) VALUES (1, 1); -- Shivam worked at Company A
INSERT INTO user_company_mapping (user_id, company_details_id) VALUES (2, 2); -- Vivek worked at Company B
INSERT INTO user_company_mapping (user_id, company_details_id) VALUES (3, 3); -- Ashutosh worked at Company C
INSERT INTO user_company_mapping (user_id, company_details_id) VALUES (3, 1); -- Ashutosh also worked at Company A


