package com.example.navigationapp

sealed class BottomNavigationItems(var route: String, var icon: Int, var title: String ){
    object Home : BottomNavigationItems("home", R.drawable.house_real_icon, "Home")
    object About : BottomNavigationItems("about", R.drawable.myabout, "About")
    object Profile : BottomNavigationItems("profile", R.drawable.profile_icon, "Profile")
    object SideNav : BottomNavigationItems("sidenav", R.drawable.navigation_sidebar_icon, "Side Nav")

}
