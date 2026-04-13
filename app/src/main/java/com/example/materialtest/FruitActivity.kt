package com.example.materialtest

import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class FruitActivity : AppCompatActivity() {
    companion object {
        const val FRUIT_NAME = "fruit_name"
        const val FRUIT_IMAGE_ID = "fruit_image_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fruit)
        val fruitName = intent.getStringExtra(FRUIT_NAME) ?: ""
        val fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID, 0)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val collapsingToolbar = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbar)
        collapsingToolbar.title = fruitName
        val fruitImageView = findViewById<ImageView>(R.id.fruitImageView)
        Glide.with(this).load(fruitImageId).into(fruitImageView)
        val fruitContentText = findViewById<TextView>(R.id.fruitContentText)
        fruitContentText.text = generateFruitContent(fruitName)
        val commentsText = findViewById<TextView>(R.id.fruitCommentsText)
        val addCommentFab = findViewById<FloatingActionButton>(R.id.addCommentFab)
        val comments = loadComments(fruitName)
        renderComments(commentsText, comments)
        addCommentFab.setOnClickListener {
            showAddCommentDialog(fruitName, comments, commentsText)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Keep image edge-to-edge while moving only interactive UI away from system bars.
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            toolbar.setPadding(
                toolbar.paddingLeft,
                systemBars.top,
                toolbar.paddingRight,
                toolbar.paddingBottom
            )
            insets
        }
    }

    private fun showAddCommentDialog(
        fruitName: String,
        comments: MutableList<String>,
        commentsText: TextView
    ) {
        val input = EditText(this).apply {
            hint = "Write your comment..."
            setSingleLine(false)
            minLines = 3
        }
        MaterialAlertDialogBuilder(this)
            .setTitle("Add comment")
            .setView(input)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Post") { _, _ ->
                val newComment = input.text.toString().trim()
                if (newComment.isEmpty()) {
                    Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                comments.add(0, newComment)
                saveComments(fruitName, comments)
                renderComments(commentsText, comments)
                Toast.makeText(this, "Comment added", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun renderComments(commentsText: TextView, comments: List<String>) {
        commentsText.text = if (comments.isEmpty()) {
            "Comments\n\nNo comments yet. Tap the comment button to add one."
        } else {
            val items = comments.joinToString("\n\n") { "- $it" }
            "Comments (${comments.size})\n\n$items"
        }
    }

    private fun loadComments(fruitName: String): MutableList<String> {
        val raw = getSharedPreferences("fruit_comments", MODE_PRIVATE)
            .getString(commentKey(fruitName), "[]")
            ?: "[]"
        return try {
            val jsonArray = JSONArray(raw)
            MutableList(jsonArray.length()) { index -> jsonArray.getString(index) }
        } catch (_: Exception) {
            mutableListOf()
        }
    }

    private fun saveComments(fruitName: String, comments: List<String>) {
        val jsonArray = JSONArray()
        comments.forEach { jsonArray.put(it) }
        getSharedPreferences("fruit_comments", MODE_PRIVATE)
            .edit()
            .putString(commentKey(fruitName), jsonArray.toString())
            .apply()
    }

    private fun commentKey(fruitName: String): String {
        return "comment_$fruitName"
    }

    private fun generateFruitContent(fruitName: String): String {
        return when (fruitName) {
            "Apple" -> "Apple is a crisp and juicy fruit rich in fiber and vitamin C. " +
                    "It is great for snacks, salads, and desserts, and can be eaten fresh or baked."
            "Banana" -> "Banana is a soft and sweet fruit that provides potassium and quick energy. " +
                    "It is perfect for breakfast, smoothies, and healthy snacks."
            "Orange" -> "Orange is a citrus fruit known for its refreshing taste and high vitamin C content. " +
                    "It can be enjoyed as fresh slices or juice."
            "Watermelon" -> "Watermelon is a hydrating summer fruit with a light sweetness and high water content. " +
                    "It is delicious when chilled and shared on hot days."
            "Pear" -> "Pear has a delicate sweetness and smooth texture, with plenty of fiber. " +
                    "It can be eaten fresh, poached, or added to salads."
            "Grape" -> "Grape is a small juicy fruit that comes in green, red, and purple varieties. " +
                    "It is easy to snack on and also used for juice, raisins, and jam."
            "Pineapple" -> "Pineapple is a tropical fruit with a sweet and tangy flavor, rich in vitamin C and manganese. " +
                    "It pairs well with desserts, drinks, and savory dishes."
            "Strawberry" -> "Strawberry is a bright red berry with a sweet aroma and natural freshness. " +
                    "It is commonly used in cakes, yogurt, smoothies, and fruit salads."
            "Cherry" -> "Cherry is a small stone fruit with a rich sweet-tart flavor and deep color. " +
                    "It is tasty fresh and also popular in pies and jams."
            "Mango" -> "Mango is a tropical fruit with a soft texture and rich sweetness, often called the king of fruits. " +
                    "It is excellent for fresh eating, smoothies, and desserts."
            else -> "$fruitName is a delicious fruit with unique flavor and nutrition."
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
