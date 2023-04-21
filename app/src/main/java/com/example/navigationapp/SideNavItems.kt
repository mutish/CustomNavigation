package com.example.navigationapp

sealed class SideNavItems(var route: String, var icon: Int, var title: String){
    object Home: SideNavItems("home", R.drawable.house_real_icon,"Home")
    object Dashboard: SideNavItems("dashboard", R.drawable.dashboard_icon,"Dashboard")
    object About: SideNavItems("about", R.drawable.information_icon,"About")
    object Profile: SideNavItems("profile", R.drawable.profile_group_icon,"Profile")
    object Music: SideNavItems("music", R.drawable.spotify_icon,"music")
    object BottomNav: SideNavItems("sidenav", R.drawable.navigation_sidebar_icon,"Bottom Nav")

}
