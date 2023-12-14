package kr.ac.kumoh.ce.s20910531.s23w1502room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "song")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String
)

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun select(): LiveData<List<Song>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: Song)
}

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}

class SongRepository(private val dao: SongDao) {
    val songList: LiveData<List<Song>> = dao.select()

    suspend fun insert(song: Song) {
        dao.insert(song)
    }
}