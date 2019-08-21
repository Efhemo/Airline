
package com.efhems.airlines.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.efhems.airlines.database.getDatabase
import com.efhems.airlines.domain.Airport
import com.efhems.airlines.domain.Schedule
import com.efhems.airlines.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class ViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val repository = Repository(database)

    val airport: LiveData<List<Airport>> get() = repository.getAirports()

    private val _schedules: MutableLiveData<List<Schedule>> = MutableLiveData()
    val schedules: LiveData<List<Schedule>> get() = _schedules


    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        airport()
    }

    fun airport() {
        viewModelScope.launch {
            repository.airports()
        }
    }

    fun schedules(origin: String, destination: String, date: String) {
        viewModelScope.launch {
            _schedules.value = repository.schedules(origin, destination, date)
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}
