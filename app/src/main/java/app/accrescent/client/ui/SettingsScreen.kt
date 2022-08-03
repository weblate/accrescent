package app.accrescent.client.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.accrescent.client.R
import app.accrescent.client.util.isPrivileged
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val dynamicColor by viewModel.dynamicColor.collectAsState(false)
    val requireUserAction by viewModel.requireUserAction.collectAsState(true)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.width(228.dp)) {
                Text(stringResource(R.string.dynamic_color), fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.dynamic_color_desc), fontSize = 14.sp)
            }
            Switch(
                checked = dynamicColor,
                onCheckedChange = { coroutineScope.launch { viewModel.setDynamicColor(it) } },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.width(228.dp)) {
                Text(stringResource(R.string.require_user_action), fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.require_user_action_desc), fontSize = 14.sp)
            }
            Switch(
                checked = requireUserAction,
                onCheckedChange = { coroutineScope.launch { viewModel.setDynamicColor(it) } },
                enabled = context.isPrivileged(),
            )
        }
    }
}
