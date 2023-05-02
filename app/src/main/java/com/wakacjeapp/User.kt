package com.wakacjeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    var name: String?,
    var email: String?,
    var uid: String?
    ) : Parcelable

{
    constructor() : this(
        "",
        "",
        ""
    )
}
