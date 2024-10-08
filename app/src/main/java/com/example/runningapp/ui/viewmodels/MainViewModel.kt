package com.example.runningapp.ui.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.db.Run
import com.example.runningapp.others.SortType
import com.example.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }

    fun deleteRun(run: Run) = viewModelScope.launch {
        mainRepository.deleteRun(run)
    }

    private val runSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runSortedByCaloriesBurned = mainRepository.getAllRunSortedByCalories()
    private val runSortedByTimesInMillis = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runSortedAvgSpeed = mainRepository.getAllRunSortedByAvgSpeed()


    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runSortedByDate) { result ->
            if(sortType == SortType.DATE){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runSortedAvgSpeed) { result ->
            if(sortType == SortType.AVG_SPEED){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runSortedByDistance) { result ->
            if(sortType == SortType.DISTANCE){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runSortedByCaloriesBurned) { result ->
            if(sortType == SortType.CALORIES_BURNED){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runSortedByTimesInMillis) { result ->
            if(sortType == SortType.RUNNING_TIME){
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType){
        SortType.DATE -> runSortedByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runSortedByTimesInMillis.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runSortedAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runSortedByDistance.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }
}