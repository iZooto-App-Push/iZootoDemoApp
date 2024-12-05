package com.k.deeplinkingtesting.admob

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.izooto.iZooto
import com.k.deeplinkingtesting.R

class GridViewActivity : AppCompatActivity() {

    var recycler_view : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grid_layout)
        recycler_view=findViewById(R.id.recycler_view)
        // Sample data
        val data = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6")
        // Find RecyclerView
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "GridView "
        // Set GridLayoutManager with 2 columns
        recycler_view?.layoutManager = GridLayoutManager(this, 2)
        recycler_view?.addItemDecoration(GridSpacingItemDecoration(2, 16))
        // Set Adapter
        recycler_view?.adapter = GridAdapter(data)
        var scrollView : ScrollView = findViewById(R.id.parent_layout)
        val layout: LinearLayout = findViewById(R.id.main_layout)
        iZooto.enablePulse(this,scrollView, layout, true)

    }
}