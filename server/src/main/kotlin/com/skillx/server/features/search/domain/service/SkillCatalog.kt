package com.skillx.server.features.search.domain.service

import com.skillx.server.features.search.domain.model.PersonSummary
import com.skillx.server.features.search.domain.model.SkillStat

/**
 * Builds the skill catalog from users' skill lists. Skills are matched case-insensitively
 * (trimmed); a user listing the same skill twice counts once. The display name is the
 * first spelling encountered.
 */
object SkillCatalog {

    fun aggregate(people: List<PersonSummary>): List<SkillStat> {
        val names = LinkedHashMap<String, String>()
        val teachers = HashMap<String, MutableSet<String>>()
        val learners = HashMap<String, MutableSet<String>>()

        for (person in people) {
            for (raw in person.teachSkills) {
                val key = normalize(raw) ?: continue
                names.getOrPut(key) { raw.trim() }
                teachers.getOrPut(key) { mutableSetOf() }.add(person.uid)
            }
            for (raw in person.learnSkills) {
                val key = normalize(raw) ?: continue
                names.getOrPut(key) { raw.trim() }
                learners.getOrPut(key) { mutableSetOf() }.add(person.uid)
            }
        }

        return names.map { (key, name) ->
            SkillStat(
                id = key,
                name = name,
                teacherCount = teachers[key]?.size ?: 0,
                learnerCount = learners[key]?.size ?: 0
            )
        }
    }

    /** Most-taught first, then most-wanted, then alphabetical — a stable, explainable order. */
    val byPopularity: Comparator<SkillStat> =
        compareByDescending<SkillStat> { it.teacherCount }
            .thenByDescending { it.learnerCount }
            .thenBy { it.name.lowercase() }

    fun normalize(skill: String): String? = skill.trim().lowercase().takeIf { it.isNotEmpty() }
}
