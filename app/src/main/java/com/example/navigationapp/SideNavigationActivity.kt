package com.example.navigationapp

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SideNavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            views
            NavScreen()
        }
    }
    override fun onBackPressed() {
        val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun NavScreen(){
    // scaffold state : is the drawer open or not
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    // CoroutineScope : ability to toggle the drawer state
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    // if you want the drawer to open for the right side
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { SideTopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerBackgroundColor = colorResource(id = R.color.white),
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        },
        content = {Box(modifier = Modifier.padding(it)) {
            SideNavigation(navController = navController)
        }
        },
        backgroundColor = colorResource(id = R.color.bleu_de_france),
    )
}
@Composable
fun SideTopBar(scope: CoroutineScope, scaffoldState: ScaffoldState){
    TopAppBar(
        title = { Text(text = "Side Navigation") },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = colorResource(id = R.color.white),
        contentColor = Color.Black
    )
}

@Preview(showBackground = true)
@Composable
fun BarScreenPreview(){
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    SideTopBar(scope = scope, scaffoldState = scaffoldState)
}

@Preview(showBackground = true)
@Composable
fun NavScreenPreview(){
    NavScreen()
}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController){
//    list of my nav items
    val nav_items = listOf(SideNavItems.Home,SideNavItems.About,SideNavItems.Profile,SideNavItems.Dashboard,SideNavItems.Music,
        SideNavItems.BottomNav)
    // create the view
    Column(modifier =  Modifier.background(colorResource(id = R.color.white))) {
//        navigation header
        Image(
            painterResource(id = R.drawable.android), contentDescription = "nav header",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(10.dp) )
//        space in between
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(5.dp))
//        List of items
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        nav_items.forEach{ item ->
            DrawerItem(item = item, selected = currentRoute == item.route,
                onItemClick = {
                    navController.navigate(item.route){
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(route = it){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    //close the drawer
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }

                })
        }
//        footer
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "I finally saw the light at the End of the Tunnel.", color = Color.Black, textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic ,modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally))
    }

}

@Composable
fun SideNavigation(navController: NavHostController){
    NavHost(navController, startDestination = SideNavItems.Home.route ){
        composable(SideNavItems.Home.route){
            HomeScreen()
        }
        composable(SideNavItems.About.route){
            AboutScreen()
        }
        composable(SideNavItems.Profile.route){
            ProfileScreen()
        }
        composable(SideNavItems.Dashboard.route){
            DashboardScreen()
        }
        composable(SideNavItems.Music.route){
            MusicScreen()
        }
        composable(SideNavItems.BottomNav.route){
            val context = LocalContext.current
            val intent = Intent(context, BottomNavigationActivity::class.java)
            context.startActivity(intent)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DrawerPreview(){
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val navController = rememberNavController()
    Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
}


@Composable
fun DrawerItem(item: SideNavItems, selected: Boolean, onItemClick: (SideNavItems) -> Unit) {
    val background = if (selected) R.color.yellow else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(colorResource(id = background))
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}

@Preview(showBackground = false)
@Composable
fun DrawerItemPreview() {
    DrawerItem(item = SideNavItems.Home, selected = false, onItemClick = {})
}


