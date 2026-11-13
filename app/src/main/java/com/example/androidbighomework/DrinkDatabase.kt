package com.example.androidbighomework

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

// Only user-added drinks go into this table; preset drinks live in code.
@Entity(tableName = "custom_drinks")
data class CustomDrink(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Double,
    val sugar: Double,
    val caffeine: Double,
    val description: String,
    val steps: String
)

// Ratings are stored separately so preset drinks can be rated too.
@Entity(tableName = "drink_ratings")
data class DrinkRating(
    @PrimaryKey val drinkKey: String,
    val stars: Int
)

@Dao
interface DrinkDao {
    @Query("SELECT * FROM custom_drinks ORDER BY id DESC")
    fun getAllCustomDrinks(): List<CustomDrink>

    @Insert
    fun insertDrink(drink: CustomDrink)

    @Update
    fun updateDrink(drink: CustomDrink)

    @Delete
    fun deleteDrink(drink: CustomDrink)

    @Query("SELECT * FROM drink_ratings WHERE drinkKey = :key LIMIT 1")
    fun getRating(key: String): DrinkRating?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveRating(rating: DrinkRating)

    @Query("DELETE FROM drink_ratings WHERE drinkKey = :key")
    fun deleteRating(key: String)
}

@Database(entities = [CustomDrink::class, DrinkRating::class], version = 1, exportSchema = false)
abstract class DrinkDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao

    companion object {
        private var instance: DrinkDatabase? = null

        fun getDatabase(context: Context): DrinkDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrinkDatabase::class.java,
                    "drink_database"
                )
                    // This small classroom project has tiny data, so this is the easiest to read.
                    // A real project should move database reads/writes to a background thread.
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}
