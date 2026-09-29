package com.skillx.features.onboarding.presentation.review

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.features.onboarding.presentation.OnboardingStep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReviewUiState(
    val isLoading: Boolean = true,
    val saving: Boolean = false,
    val error: String = "",
    val reviewItems: List<ReviewItem> = emptyList()
) {
    data class ReviewItem(
        val title: String,
        val value: String,
        val step: OnboardingStep
    )
}

class ReviewViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val viewModelScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = onboardingRepository.loadProgress()
            when (result) {
                is AppResult.Success -> {
                    val progress = result.data
                    val items = buildReviewItems(progress)
                    _uiState.update { it.copy(isLoading = false, reviewItems = items) }
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.error.message) }
            }
        }
    }

    private fun buildReviewItems(progress: OnboardingProgress): List<ReviewUiState.ReviewItem> {
        return listOf(
            ReviewUiState.ReviewItem(
                title = "Teach Skills",
                value = if (progress.teachSkills.isEmpty()) "None" else progress.teachSkills.map { "${it.skillId.value} (${it.proficiency?.displayName ?: "No level"})" }.joinToString(", "),
                step = OnboardingStep.TEACH_SKILLS
            ),
            ReviewUiState.ReviewItem(
                title = "Learn Skills",
                value = if (progress.learnSkills.isEmpty()) "None" else progress.learnSkills.map { "${it.skillId.value} (${it.targetProficiency?.displayName ?: "Any level"})" }.joinToString(", "),
                step = OnboardingStep.LEARN_SKILLS
            ),
            ReviewUiState.ReviewItem(
                title = "Proficiency Levels",
                value = if (progress.teachSkills.isEmpty()) "N/A" else progress.teachSkills.map { "${it.skillId.value}: ${it.proficiency?.displayName ?: "Not set"}" }.joinToString(", "),
                step = OnboardingStep.PROFICIENCY
            ),
            ReviewUiState.ReviewItem(
                title = "Goals",
                value = buildGoalsString(progress.learningGoals, progress.teachingGoals),
                step = OnboardingStep.GOALS
            ),
            ReviewUiState.ReviewItem(
                title = "Availability",
                value = buildAvailabilityString(progress.availableDays, progress.lessonFormats, progress.preferredDurationMinutes),
                step = OnboardingStep.AVAILABILITY
            ),
            ReviewUiState.ReviewItem(
                title = "Languages",
                value = if (progress.languages.isEmpty()) "None" else progress.languages.map { "${it.languageCode} - ${it.level.displayLabel}" }.joinToString(", "),
                step = OnboardingStep.AVAILABILITY
            )
        )
    }

    private fun buildGoalsString(learning: Set<com.skillx.features.onboarding.domain.model.LearningGoal>, teaching: Set<com.skillx.features.onboarding.domain.model.TeachingGoal>): String {
        val parts = mutableListOf<String>()
        if (learning.isNotEmpty()) parts.add("Learning: ${learning.joinToString(", ") { it.name }}")
        if (teaching.isNotEmpty()) parts.add("Teaching: ${teaching.joinToString(", ") { it.name }}")
        return if (parts.isEmpty()) "None" else parts.joinToString("; ")
    }

    private fun buildAvailabilityString(
        days: Set<AvailabilityDay>,
        formats: Set<com.skillx.features.onboarding.domain.model.LessonFormat>,
        duration: Int?
    ): String {
        val parts = mutableListOf<String>()
        if (days.isNotEmpty()) parts.add(days.joinToString(", ") { it.name.substring(0, 3) })
        if (formats.isNotEmpty()) parts.add(formats.joinToString(", ") { it.name })
        if (duration != null) parts.add("${duration} min")
        return if (parts.isEmpty()) "Not set" else parts.joinToString("; ")
    }

    fun onFinish(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.saving) return
        _uiState.update { it.copy(saving = true, error = "") }
        viewModelScope.launch {
            val result = onboardingRepository.complete()
            when (result) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(saving = false) }
                    onSuccess()
                }
                is AppResult.Error -> _uiState.update { it.copy(saving = false, error = result.error.message) }
            }
        }
    }
}