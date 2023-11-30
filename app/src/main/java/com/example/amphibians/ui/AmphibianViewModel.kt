/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.amphibians.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

enum class AmphibianApiStatus { LOADING, ERROR, DONE }

data class AmphibiansViewState(
    val amphibians: List<Amphibian>,
    val status: AmphibianApiStatus = AmphibianApiStatus.LOADING
)

data class AmphibianViewState(
    val amphibian: Amphibian,
    val status: AmphibianApiStatus = AmphibianApiStatus.LOADING
)

class AmphibianViewModel : ViewModel() {

    private val _amphibiansViewState = MutableLiveData<AmphibiansViewState>()
    val amphibiansViewState: LiveData<AmphibiansViewState>
        get() = _amphibiansViewState


    private val _amphibianViewState = MutableLiveData<AmphibianViewState>()
    val amphibianViewState: LiveData<AmphibianViewState>
        get() = _amphibianViewState

    private fun getAmphibians() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _amphibiansViewState.postValue(
                    AmphibiansViewState(
                        AmphibianApi.retrofitService.getAmphibians(),
                        AmphibianApiStatus.DONE
                    )
                )
            } catch (ioException: IOException) {
                _amphibiansViewState.postValue(
                    AmphibiansViewState(
                        listOf(),
                        AmphibianApiStatus.ERROR
                    )
                )
            }
        }
    }

    fun onAmphibianClicked(amphibian: Amphibian) {
        // TODO: Set the amphibian object
        _amphibianViewState.value = AmphibianViewState(amphibian, AmphibianApiStatus.DONE)
    }

    init {
        getAmphibians()
    }
}
