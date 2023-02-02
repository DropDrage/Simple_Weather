package com.dropdrage.common.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toPx(density: Density) = with(density) { toPx() }

@Composable
fun Dp.toPxInt(density: Density) = with(density) { toPx().toInt() }
