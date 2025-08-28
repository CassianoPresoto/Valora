package org.example.project.features.friends.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.features.friends.presentation.Friend
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import valora.composeapp.generated.resources.Res
import valora.composeapp.generated.resources.*

@Composable
fun FriendCard(
    friend: Friend,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (friend.profileImageUrl != null) {
                    // TODO: Load actual image when image loading is implemented
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(Res.string.profile_photo_content_description),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(Res.string.profile_photo_content_description),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Friend name
            Text(
                text = friend.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            // Balance information
            if (friend.balance != 0.0) {
                Text(
                    text = if (friend.balance > 0) {
                        stringResource(Res.string.owes_you, friend.balance.toString())
                    } else {
                        stringResource(Res.string.you_owe_them, kotlin.math.abs(friend.balance).toString())
                    },
                    fontSize = 14.sp,
                    color = if (friend.balance > 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview
@Composable
private fun FriendCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Friend who owes you
            FriendCard(
                friend = Friend(
                    id = "1",
                    name = "João Silva",
                    balance = 25.50
                ),
                onClick = { }
            )
            
            // Friend you owe
            FriendCard(
                friend = Friend(
                    id = "2",
                    name = "Maria Santos",
                    balance = -75.00
                ),
                onClick = { }
            )
            
            // Friend with no balance
            FriendCard(
                friend = Friend(
                    id = "3",
                    name = "Carlos Lima",
                    balance = 0.0
                ),
                onClick = { }
            )
        }
    }
}
