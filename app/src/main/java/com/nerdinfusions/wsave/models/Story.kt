package com.nerdinfusions.wsave.models

import java.io.Serializable

data class Story(
        var type: Int = 0,
        var path: String? = null
): Serializable {
}