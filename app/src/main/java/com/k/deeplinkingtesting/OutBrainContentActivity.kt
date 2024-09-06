package com.k.deeplinkingtesting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.k.deeplinkingtesting.Drawer.SlidingDrawerActivity
import com.k.deeplinkingtesting.SFWebView.SFWebViewRecyclerViewActivity
import com.k.deeplinkingtesting.SFWebView.SFWebViewScrollViewActivity
import com.k.deeplinkingtesting.SmartFeed.SmartFeedActivity
import com.k.deeplinkingtesting.SmartFeed.SmartFeedMidFeedActivity
import com.k.deeplinkingtesting.VideoWidget.VideoWidgetActivity
import com.k.deeplinkingtesting.gridlayout.GridWithImagesActivity
import com.k.deeplinkingtesting.insteam.SwipableActivity

class OutBrainContentActivity : AppCompatActivity() {
    private var smart_feed: Button? = null
    private var smart_middle_feed: Button? = null
    private var grid: Button? = null
    private var sf_web_feed: Button? = null
    private var sf_web_scroll: Button? = null
    private var sf_video:Button? = null
    private var sliding_drawer : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_out_brain_content)
        smart_feed = findViewById(R.id.smart_feed)
        smart_middle_feed = findViewById(R.id.smart_middle_feed)
        grid = findViewById(R.id.grid)
        sf_web_feed = findViewById(R.id.sf_web_feed)
        sf_web_scroll = findViewById(R.id.sf_web_scroll)
        sf_video = findViewById(R.id.sf_video)
        sliding_drawer=findViewById(R.id.sliding_drawer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "OutBrain Feed"
        smart_feed?.setOnClickListener { view ->
            (view as? Button)?.let {
                val intent = Intent(this@OutBrainContentActivity, SmartFeedActivity::class.java)
                startActivity(intent)
            }
        }
        smart_middle_feed?.setOnClickListener { view ->
            (view as? Button)?.let {
                val intent = Intent(this@OutBrainContentActivity, SmartFeedMidFeedActivity::class.java)
                startActivity(intent)
            }
        }
        grid?.setOnClickListener { view ->
            (view as? Button)?.let {
                val intent = Intent(this@OutBrainContentActivity, GridWithImagesActivity::class.java)
                startActivity(intent)

            }
        }
        sf_web_feed?.setOnClickListener { view ->
            (view as? Button)?.let {
                val intent = Intent(this@OutBrainContentActivity, SFWebViewRecyclerViewActivity::class.java)
                startActivity(intent)
            }
        }
        sf_web_scroll?.setOnClickListener { view ->
            (view as? Button)?.let {
                val intent = Intent(this@OutBrainContentActivity, SFWebViewScrollViewActivity::class.java)
                startActivity(intent)
            }
        }
        sf_video?.setOnClickListener { view ->
            (view as? Button)?.let {
                val intent = Intent(this@OutBrainContentActivity, SwipableActivity::class.java)
                startActivity(intent)
            }
        }
        sliding_drawer?.setOnClickListener { view ->
            (view as? Button)?.let {
                val intent = Intent(this@OutBrainContentActivity, SlidingDrawerActivity::class.java)
                startActivity(intent)
            }
        }

    }
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                // Perform your custom action here
                // For example, show a dialog or navigate to a specific activity
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}