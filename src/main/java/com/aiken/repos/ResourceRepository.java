package com.aiken.repos;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aiken.beans.Skill;
import com.aiken.beans.SkillResource;

@Repository
public interface ResourceRepository extends JpaRepository<SkillResource, Integer>
{
	Set<SkillResource> findBySkill(Skill skill);
}