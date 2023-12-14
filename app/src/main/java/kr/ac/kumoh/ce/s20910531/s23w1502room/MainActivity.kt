package kr.ac.kumoh.ce.s20910531.s23w1502room

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.startActivity
import kr.ac.kumoh.ce.s20910531.s23w1502room.ui.theme.S23W1502RoomTheme

class MainActivity : ComponentActivity() {
    private val viewModel: SongViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: SongViewModel) {
    val songList by viewModel.songList.observeAsState(emptyList())

    val showDialog =  remember { mutableStateOf(false) }

    S23W1502RoomTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            showDialog.value = true
                        }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "추가 버튼"
                        )
                    }
                }
            ) {
                SongList(songList, it)
                if (showDialog.value) {
                    SongInputDialog(
                        closeDialog = { showDialog.value = false },
                        addSong = { title -> viewModel.add(title) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongList(songList: List<Song>, padding: PaddingValues) {
    LazyVerticalStaggeredGrid(
        StaggeredGridCells.Fixed(2),
        contentPadding = padding,
    ) {
        items(songList) {
            SongItem(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongInputDialog(
    closeDialog: () -> Unit,
    addSong: (String) -> Unit
) {
    val songTitle = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { closeDialog() }) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    value = songTitle.value,
                    onValueChange = {
                        songTitle.value = it
                    },
                    label = { Text("노래 제목") },
                    placeholder = {
                        Text("제목 입력")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (songTitle.value.isEmpty()) {
                            return@Button
                        }
                        addSong(songTitle.value)
                        closeDialog()
                    }
                ) {
                    Text("추가")
                }
            }
        }
    }
}

@Composable
fun SongItem(song: Song) {
    val context = LocalContext.current
    Card (
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query=${song.title}")
                )
                startActivity(context, intent, null)
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Text(
            text = song.title,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = 30.sp,
            lineHeight = 35.sp,
            textAlign = TextAlign.Center
        )
    }
}