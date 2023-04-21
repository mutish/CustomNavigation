package com.example.navigationapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navigationapp.BottomNavigationItems.About.route

class BottomNavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
            Navigation()
        }
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    Scaffold (
        topBar = { TopBar()},
        bottomBar = { BottomNavigationBar(navController)},
        content = {
            Box(modifier = Modifier.padding(it)) {
                Navigation(navController = navController)
            }
        },
        backgroundColor = colorResource(id = R.color.yellow)
    )

}
@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController, startDestination = BottomNavigationItems.Home.route){
        composable(BottomNavigationItems.Home.route){
            HomeScreen()
        }
        composable(BottomNavigationItems.About.route){
            AboutScreen()
        }
        composable(BottomNavigationItems.Profile.route){
            ProfileScreen()
        }
        composable(BottomNavigationItems.SideNav.route){
            val context = LocalContext.current
            val intent = Intent(context, SideNavigationActivity::class.java)
            context.startActivity(intent)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    MainScreen()
}

@Composable
fun TopBar (){
    TopAppBar (
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        backgroundColor = colorResource(id = R.color.yellow),
        contentColor = Color.Black
    )
}
@Preview(showBackground = true)
@Composable
fun TopbarPreview(){
    TopBar()
}

@Composable
fun BottomNavigationBar(navController: NavController){
    val items = listOf(
        BottomNavigationItems.Home,
        BottomNavigationItems.About,
        BottomNavigationItems.Profile,
        BottomNavigationItems.SideNav
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.bleu_de_france),
        contentColor = Color.White
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
// tag active page
        val currentRoute = navBackStackEntry?.destination?.route
//        looping..
        items.forEach {
            BottomNavigationItem(
                selected = false,
                icon = { Icon(painterResource(id = it.icon), contentDescription = it.title)},label = {Text(it.title)}, alwaysShowLabel = true,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                onClick ={
                    //  navigation
                    navController.navigate(it.route){
                        navController.graph.startDestinationRoute.let {
                            popUpTo(route){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
//adding search bar for countries
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController = navController)
        }
        composable(
            "details/{countryName}",
            arguments = listOf(navArgument("search") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("search")?.let { countryName ->
                DetailsScreen(countryName = countryName)
            }
        }
    }
}

@Composable
fun TopSearchBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = colorResource(id = R.color.black),
        contentColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopSearchBar()
}
@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = androidx.core.R.color.notification_icon_bg_color),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    SearchView(textState)
}
@Composable
fun MainScreen(navController: NavController) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        SearchView(textState)

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController = navController)
}
@Composable
fun DetailsScreen(countryName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bleu_de_france))
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = countryName,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 22.sp
        )
    }
}




