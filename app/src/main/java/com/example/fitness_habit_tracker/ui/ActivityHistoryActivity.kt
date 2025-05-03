package com.example.fitness_habit_tracker.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_habit_tracker.R
import com.example.fitness_habit_tracker.adapters.ActivityAdapter
import com.example.fitness_habit_tracker.database.WorkoutDatabase
import kotlinx.coroutines.launch

class ActivityHistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var adapter: ActivityAdapter
    private lateinit var database: WorkoutDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        database = WorkoutDatabase.getDatabase(this)

//        recyclerView = findViewById(R.id.recyclerView)
//        emptyView = findViewById(R.id.emptyStateText)

        setupRecyclerView()
        loadActivities()
    }

    private fun setupRecyclerView() {
        adapter = ActivityAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ActivityHistoryActivity)
            adapter = this@ActivityHistoryActivity.adapter
        }
    }

    private fun loadActivities() {
        lifecycleScope.launch {
            val activities = database.activityDao().getAllActivities()
            if (activities.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
                adapter.submitList(activities)
            }
        }
    }
} 