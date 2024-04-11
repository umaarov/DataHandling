package uz.umarov.kotlinflowsexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.umarov.kotlinflowsexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "LOG_TAG"
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val logTextView = binding.logTextView
        Logger.setLogTextView(logTextView)
        val logButton = binding.logButton

        lifecycleScope.launch {
            val result = sharedFlowExample()
            result.collect {
                Logger.printToTextView("Result 1 $it")
            }
        }

        lifecycleScope.launch {
            val result = sharedFlowExample()
            delay(10000)
            result.collect {
                Logger.printToTextView("Result 2 $it")
            }
        }

        logButton.setOnClickListener {
            scenario2()
        }

    }

    private fun scenario1() {
        lifecycleScope.launch(Dispatchers.Default) {
            getUserNames().forEach {
                Logger.printToTextView(it)
            }
        }
    }

    private fun scenario2() {
        lifecycleScope.launch(Dispatchers.Default) {
            getUserNamesAsFlows().collect { userName ->
                Logger.printToTextView(userName)
            }
        }
    }

    private fun stateFlowExample(): Flow<Int> {
        val mutableSharedFlow = MutableStateFlow<Int>(8989)
        lifecycleScope.launch {
            val list = listOf<Int>(1, 2, 3, 4, 5)
            list.forEach {
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

    private fun sharedFlowExample(): Flow<Int> {
        val mutableSharedFlow = MutableSharedFlow<Int>()
        lifecycleScope.launch {
            val list = listOf<Int>(1, 2, 3, 4, 5)
            list.forEach {
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

    private suspend fun getUserNames(): List<String> {
        val list = mutableListOf<String>()
        list.add(getUser(1))
        list.add(getUser(2))
        list.add(getUser(3))
        list.add(getUser(4))
        list.add(getUser(5))
        return list
    }

    private suspend fun getUserNamesAsFlows(): Flow<String> {
        Logger.printToTextView("First line getUserNamesAsFlows")
        return listOf(1, 2, 3, 4, 5).asFlow().map { getUser(it) }
    }

    private suspend fun getUser(id: Int): String {
        Logger.printToTextView("getUser $id Current thread: " + Thread.currentThread().name)
        delay(1000)
        return "User $id"
    }

}