package com.my.list.bean

class User(
    var id: Long? = null,
    var name: String? = null,
    var pass: String? = null,
    var email: String? = null,
    var status: String? = null
) {
    override fun toString(): String {
        return "User[$id,$name,$pass,$email,$status]"
    }
}
