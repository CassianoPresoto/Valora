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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.features.friends.domain.model.Friend
import org.example.project.ui.theme.ValoraTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (friend.avatarUrl != null) {
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
            Text(
                text = friend.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                val isPositive = friend.totalBalance > 0
                val labelColor = if (isPositive) {
                    ValoraTheme.colors.positiveSoft
                } else {
                    ValoraTheme.colors.negativeSoft
                }

                if (friend.totalBalance != 0.0) {
                    Text(
                        text = if (isPositive) {
                            stringResource(Res.string.small_they_owe_you_label)
                        } else {
                            stringResource(Res.string.small_you_owe_label)
                        },
                        fontSize = 12.sp,
                        lineHeight = 11.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip,
                        color = labelColor,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "R$ " + kotlin.math.abs(friend.totalBalance).toString(),
                        fontSize = 18.sp,
                        lineHeight = 17.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip,
                        color = labelColor,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.no_pending_debts),
                        fontSize = 12.sp,
                        lineHeight = 11.sp,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Clip,
                        color = ValoraTheme.colors.positive,
                        fontWeight = FontWeight.Normal
                    )
                }
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
            FriendCard(
                friend = Friend(
                    userId = "1",
                    name = "João Silva",
                    totalBalance = 25.50
                ),
                onClick = { }
            )


            FriendCard(
                friend = Friend(
                    userId = "2",
                    name = "Maria Santos",
                    totalBalance = -75.00
                ),
                onClick = { }
            )


            FriendCard(
                friend = Friend(
                    userId = "3",
                    name = "Carlos Lima",
                    totalBalance = 0.0
                ),
                onClick = { }
            )
        }
    }
}
