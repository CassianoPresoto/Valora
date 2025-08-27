package org.example.project.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.*

enum class BottomTab(val labelRes: StringResource?, val icon: ImageVector) {
    Friends(Res.string.tab_friends, Icons.Default.Person),
    Groups(Res.string.tab_groups, Icons.Default.Group),
    Add(null, Icons.Default.AddBox),
    Activities(Res.string.tab_activities, Icons.Default.AreaChart),
    Account(Res.string.tab_account, Icons.Default.AccountCircle)
}
