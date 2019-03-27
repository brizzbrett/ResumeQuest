package com.aiken.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiken.beans.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer>
{
}
