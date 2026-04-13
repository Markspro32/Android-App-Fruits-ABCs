package com.example.materialtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val fruits = mutableListOf(Fruit("Apple", R.drawable.apple), Fruit("Banana", R.drawable.banana),
        Fruit("Orange", R.drawable.orange), Fruit("Watermelon", R.drawable.watermelon),
        Fruit("Pear", R.drawable.pear), Fruit("Grape", R.drawable.grape),
        Fruit("Pineapple", R.drawable.pineapple), Fruit("Strawberry", R.drawable.strawberry),
        Fruit("Cherry", R.drawable.cherry), Fruit("Mango", R.drawable.mango))

    val fruitList = ArrayList<Fruit>()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: FruitAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initFruits()
        val layoutManager = GridLayoutManager(this, 2)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        adapter = FruitAdapter(this, fruitList)
        recyclerView.adapter = adapter


        val mainView = findViewById<androidx.coordinatorlayout.widget.CoordinatorLayout>(R.id.main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshFruits()
        }

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            toolbar.setPadding(0, systemBars.top, 0, 0)
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.closeDrawers()
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
            it.title = "Fruits"
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
        
        val navView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.navView)
        navView.setCheckedItem(R.id.navCall)
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            true
        }
        
        val fab = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Data Deleted", Snackbar.LENGTH_SHORT)
                .setAction("UNDO") {
                    Toast.makeText(this, "Data restored", Toast.LENGTH_SHORT).show()
                }
                .setAnchorView(fab)
                .show()
        }
    }
    private fun refreshFruits(){
        thread{
            runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }
    private fun initFruits(){
        fruitList.clear()
        repeat(50){
            val index = (0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backup ->
                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show()
            R.id.delete ->
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show()
            R.id.settings ->
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show()
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }
}

class Fruit(val name: String,val imageId:Int)
class FruitAdapter(val context: Context, val fruitList:List<Fruit>):
        RecyclerView.Adapter<FruitAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val fruitImage: ImageView =view.findViewById(R.id.fruitImage)
        val fruitName: TextView =view.findViewById(R.id.fruitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType:Int): ViewHolder{
        val view= LayoutInflater.from(context).inflate(R.layout.fruit_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position == RecyclerView.NO_POSITION) return@setOnClickListener
            val fruit = fruitList[position]
            val intent = Intent(context, FruitActivity::class.java).apply{
                putExtra(FruitActivity.FRUIT_NAME,fruit.name)
                putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.imageId)
            }
            context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder,position:Int){
        val fruit=fruitList[position]
        holder.fruitName.text=fruit.name
        Glide.with(context).load(fruit.imageId).into(holder.fruitImage)
        }
    override fun getItemCount()=fruitList.size
}
