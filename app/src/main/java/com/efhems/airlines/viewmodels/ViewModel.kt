
package com.efhems.airlines.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.efhems.airlines.database.getDatabase
import com.efhems.airlines.domain.Airport
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

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}
