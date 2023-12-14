package kr.ac.kumoh.ce.s20910531.s23w1502room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch

class SongViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: SongDao
    private val repository: SongRepository
    val songList: LiveData<List<Song>>

    init {
        val context = getApplication<Application>().applicationContext
        val db = Room.databaseBuilder(
            context,
            SongDatabase::class.java,
            "song.db"
        ).build()
        dao = db.songDao()
        repository = SongRepository(dao)
//        viewModelScope.launch {
//            repository.insert(Song(0, "노래 테스트"))
//        }
        songList = repository.songList
    }

    fun add(title: String) {
        viewModelScope.launch {
            repository.insert(Song(0, title))
        }
    }
}