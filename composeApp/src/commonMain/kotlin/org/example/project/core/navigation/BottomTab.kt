package org.example.project.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.*

enum class BottomTab(val labelRes: StringResource?, val icon: ImageVector) {
    Friends(Res.string.tab_friends, Icons.Default.People),
    Groups(Res.string.tab_groups, Icons.Default.Group),
    Add(null, Icons.Default.Add),
    Activities(Res.string.tab_activities, Icons.Default.List),
    Account(Res.string.tab_account, Icons.Default.Person)
}
