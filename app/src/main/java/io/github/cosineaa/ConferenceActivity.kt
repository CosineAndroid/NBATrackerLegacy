package io.github.cosineaa

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import io.github.cosineaa.ConferenceActivity.Companion.conferenceInstance
import io.github.cosineaa.MainActivity.Companion.allTeamList
import io.github.cosineaa.tracker.data.TeamInfo
import io.github.cosineaa.util.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ConferenceActivity : ComponentActivity() {

    companion object {
        lateinit var conferenceInstance: ConferenceActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        conferenceInstance = this

        setContent {
            ConferenceView()
        }
    }
}

@Composable
fun ConferenceTopAppBar() {
    TopAppBar(
        title = { Text(text = "컨퍼런스", color = Color.White, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Bold) },
        backgroundColor = Color.Black
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ConferenceView() {
    val pageState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column {
        ConferenceTopAppBar()
        SelectConference(pageState, scope)
        ConferenceStatTypeGuide()
        ScrollConferenceTeam(pageState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SelectConference(pageState: PagerState, scope: CoroutineScope) {
    TabRow(
        selectedTabIndex = pageState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pageState, tabPositions)) },
        backgroundColor = Color.Black,
        contentColor = Color.Yellow
    ) {
        MainActivity.conferencePages.forEachIndexed { index, title ->
            Tab(
                text = { Text(text = title, color = Color.White, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium) },
                selected = pageState.currentPage == index,
                onClick = {
                    scope.launch {
                        pageState.scrollToPage(index)
                    }
                }
            )
        }
    }
}

@Composable
fun ConferenceStatTypeGuide() {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(30.size()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Text(
            modifier = Modifier.fillMaxWidth(0.07f),
            text = "순위", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
        Text(modifier = Modifier.fillMaxWidth(0.05f),
            text = "   ", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
        Text(modifier = Modifier.fillMaxWidth(0.25f),
            text = "팀", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
        Text(modifier = Modifier.fillMaxWidth(0.11f),
            text = "경기", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
        Text(modifier = Modifier.fillMaxWidth(0.07f),
            text = "승", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
        Text(modifier = Modifier.fillMaxWidth(0.08f),
            text = "패", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
        Text(modifier = Modifier.fillMaxWidth(0.18f),
            text = "승률", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
        Text(modifier = Modifier.fillMaxWidth(0.2f),
            text = "연속", fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Medium)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScrollConferenceTeam(pageState: PagerState) {
    HorizontalPager(count = MainActivity.conferencePages.size, state = pageState) { page ->
        LazyColumn {
            when (page) {
                0 -> itemsIndexed(MainActivity.allTeamList) { index, item -> ConferenceTeam(index, item) }
                1 -> itemsIndexed(MainActivity.eastTeamList) { index, item -> ConferenceTeam(index, item) }
                2 -> itemsIndexed(MainActivity.westTeamList) { index, item -> ConferenceTeam(index, item) }
            }
        }
    }
}

@Composable
fun ConferenceTeam(order: Int, info: TeamInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.size())
            .padding(2.size())
            .clickable {
                val intent = Intent(conferenceInstance, TeamActivity::class.java)
                intent.putExtra("TeamInfo", info)
                conferenceInstance.startActivity(intent)
            },
        backgroundColor = Color.White,
        contentColor = Color.Black)
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(modifier = Modifier
                .fillMaxWidth(0.08f)
                .padding(start = 10.size()),
                text = String.format("%02d", order + 1), fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Light)

            AsyncImage(modifier = Modifier
                .fillMaxWidth(0.14f)
                .size(40.size(), 40.size()),
                model = info.teamImage, contentDescription = info.teamKoreanShortName)
            // LoadConferenceTeamImage(teamShortName = info.teamShortName)

            Text(modifier = Modifier.fillMaxWidth(0.36f),
                text = info.teamKoreanShortName, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Light)

            Text(modifier = Modifier.fillMaxWidth(0.2f),
                text = info.gameAll, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Light)

            Text(modifier = Modifier.fillMaxWidth(0.2f),
                text = info.gameWin, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Light)

            Text(modifier = Modifier.fillMaxWidth(0.2f),
                text = info.gameLose, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Light)

            Text(modifier = Modifier.fillMaxWidth(0.6f),
                text = info.gameRate, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Light)

            Text(modifier = Modifier.fillMaxWidth(0.65f),
                text = info.gameContinuity, fontFamily = MainActivity.esamanru, fontWeight = FontWeight.Light)
        }
    }
}
