package com.skillx.features.onboarding.presentation.availability

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.CefrLevel
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.TimeOfDay
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AvailabilityUiState(
    val availableDays: Set<AvailabilityDay> = emptySet(),
    val timesOfDay: Set<TimeOfDay> = emptySet(),
    val lessonFormats: Set<LessonFormat> = emptySet(),
    val preferredDurationMinutes: Int? = null,
    val languagesText: String = "",
    val isLoading: Boolean = true,
    val saving: Boolean = false,
    val error: String = ""
)

class AvailabilityViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val viewModelScope: CoroutineScope
) {

    private val _uiState = MutableStateFlow(
        AvailabilityUiState()
    )

    val uiState: StateFlow<AvailabilityUiState> =
        _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        _uiState.update {
            it.copy(
                isLoading = true,
                error = ""
            )
        }

        viewModelScope.launch {

            val result =
                onboardingRepository.loadProgress()

            when (result) {

                is AppResult.Success -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,

                            availableDays =
                                result.data.availableDays,

                            timesOfDay =
                                result.data.availableTimesOfDay,

                            lessonFormats =
                                result.data.lessonFormats,

                            preferredDurationMinutes =
                                result.data.preferredDurationMinutes,

                            languagesText =
                                result.data.languages.joinToString("\n") {
                                    "${it.languageCode} - ${it.level.displayLabel}"
                                }
                        )
                    }
                }

                is AppResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun onDayChanged(
        day: AvailabilityDay,
        checked: Boolean
    ) {
        _uiState.update { current ->

            val newDays =
                if (checked) {
                    current.availableDays + day
                } else {
                    current.availableDays - day
                }

            current.copy(
                availableDays = newDays
            )
        }
    }

    fun onTimeOfDayChanged(
        timeOfDay: TimeOfDay,
        checked: Boolean
    ) {
        _uiState.update { current ->

            val newTimes =
                if (checked) {
                    current.timesOfDay + timeOfDay
                } else {
                    current.timesOfDay - timeOfDay
                }

            current.copy(
                timesOfDay = newTimes
            )
        }
    }

    fun onFormatChanged(
        format: LessonFormat,
        checked: Boolean
    ) {
        _uiState.update { current ->

            val newFormats =
                if (checked) {
                    current.lessonFormats + format
                } else {
                    current.lessonFormats - format
                }

            current.copy(
                lessonFormats = newFormats
            )
        }
    }

    fun onDurationChanged(
        minutes: Int
    ) {
        _uiState.update { current ->

            current.copy(
                preferredDurationMinutes =
                    if (
                        current.preferredDurationMinutes == minutes
                    ) {
                        null
                    } else {
                        minutes
                    }
            )
        }
    }

    fun onLanguagesTextChanged(
        text: String
    ) {
        _uiState.update {
            it.copy(
                languagesText = text
            )
        }
    }

    private fun parseLanguages(
        text: String
    ): List<LanguageProficiencyEntry> {

        return text.lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { line ->

                val parts =
                    line.split("-")
                        .map { it.trim() }

                if (parts.size == 2) {

                    LanguageProficiencyEntry(
                        languageCode = parts[0],
                        level =
                            CefrLevel.values()
                                .firstOrNull {
                                    it.displayLabel == parts[1]
                                }
                                ?: CefrLevel.B1
                    )

                } else {

                    LanguageProficiencyEntry(
                        languageCode = parts[0],
                        level = CefrLevel.B1
                    )
                }
            }
    }

    fun onNext(
        onSuccess: () -> Unit
    ) {

        val state = _uiState.value

        if (state.saving) {
            return
        }

        _uiState.update {
            it.copy(
                saving = true,
                error = ""
            )
        }

        viewModelScope.launch {

            val languages =
                parseLanguages(
                    state.languagesText
                )

            val result =
                onboardingRepository.saveAvailability(

                    days =
                        state.availableDays,

                    timesOfDay =
                        state.timesOfDay,

                    formats =
                        state.lessonFormats,

                    durationMinutes =
                        state.preferredDurationMinutes,

                    languages =
                        languages
                )

            when (result) {

                is AppResult.Success -> {

                    _uiState.update {
                        it.copy(
                            saving = false
                        )
                    }

                    onSuccess()
                }

                is AppResult.Error -> {

                    _uiState.update {
                        it.copy(
                            saving = false,
                            error = result.error.message
                        )
                    }
                }
            }
        }
    }
}