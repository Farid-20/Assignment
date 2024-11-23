package com.example.newsapp

import News
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsList: List<News> // Holds all news data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize DrawerLayout and Toggle
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up Navigation View
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            handleNavigation(menuItem)
            true
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        newsList = getDummyData() // Initialize with dummy data
        newsAdapter = NewsAdapter(newsList) { news ->
            onNewsItemClicked(news)
        }
        recyclerView.adapter = newsAdapter

        // Set up SearchView
        val searchView: SearchView = findViewById(R.id.searchView)
        setupSearchView(searchView)

        // Set up Spinner for filtering categories
        val spinner: Spinner = findViewById(R.id.categorySpinner)
        val categories = listOf("All", "Technology", "Sports", "Government", "Health")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set listener for category selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                filterNewsByCategory(selectedCategory)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Optional: Handle if nothing is selected (e.g., show all articles)
            }
        }
    }

    // Handle navigation item clicks
    private fun handleNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.technology -> Toast.makeText(this, "Technology Selected", Toast.LENGTH_SHORT).show()
            R.id.sports -> Toast.makeText(this, "Sports Selected", Toast.LENGTH_SHORT).show()
            R.id.politics -> Toast.makeText(this, "Politics Selected", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    // Set up the SearchView
    private fun setupSearchView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = newsList.filter { article ->
                    article.title.contains(newText ?: "", ignoreCase = true) ||
                            article.description.contains(newText ?: "", ignoreCase = true)
                }
                newsAdapter.filterList(filteredList)
                return true
            }
        })
    }

    // Handle RecyclerView item clicks
    private fun onNewsItemClicked(news: News) {
        // Navigate to a new screen with news details (example)
        val intent = Intent(this, NewsDetailActivity::class.java).apply {
            putExtra("title", news.title)          // Pass the title
            putExtra("description", news.description)  // Pass the description
            putExtra("imageUrl", news.imageResId)    // Pass the image URL
        }
        startActivity(intent)
    }



    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Dummy data for news
    private fun getDummyData(): List<News> {
        return listOf(
            News("Breaking News: Market Crash", "Stock markets plummeted today, affecting global economies...", R.drawable.market_crash, "Government"),
            News("Technology Update: AI Advances", "New breakthroughs in AI technology promise to revolutionize multiple industries...", R.drawable.ai_advances, "Technology"),
            News("Sports: Team Wins Championship", "Team A clinched the trophy after an intense final game...", R.drawable.team_championship, "Sports"),
            News("Weather Alert: Heavy Rainfall Expected", "Prepare for heavy rain in the northern region. The storm is expected to last for 48 hours...", R.drawable.weather_alert, "Government"),
            News("Politics: New Policy Announced", "Government releases a new policy aimed at improving the nation's healthcare system...", R.drawable.policy_announcement, "Politics"),
            News("Health: Tips for a Balanced Diet", "Discover the top 10 tips for a healthy diet that can help improve overall wellness...", R.drawable.balanced_diet, "Health"),
            News("Global: International Trade Talks", "Countries come together for international trade talks to improve economic relations...", R.drawable.trade_talks, "Business"),
            News("Entertainment: Movie Premiere", "The highly anticipated movie premieres today, with celebrities gracing the red carpet...", R.drawable.movie_premiere, "Entertainment"),
            News("Science: New Discovery on Mars", "NASA's rover has discovered signs of ancient life on Mars, marking a major breakthrough...", R.drawable.mars_discovery, "Science"),
            News("Education: Online Learning Boom", "The rise of online learning platforms is transforming the education sector globally...", R.drawable.online_learning, "Education")
        )
    }





    // Filter news by category
    private fun filterNewsByCategory(category: String) {
        val filteredNews = if (category == "All") {
            getDummyData() // Show all news if "All" is selected
        } else {
            getDummyData().filter { it.category.equals(category, ignoreCase = true) }
        }
        newsAdapter.filterList(filteredNews) // Update the RecyclerView with the filtered list
    }
}
