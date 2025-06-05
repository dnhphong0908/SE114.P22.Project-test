package com.example.mam.data

object Constant {
    const val BASE_URL = "http://192.168.1.45:8080/api/v1/"
    const val STORAGE_URL = BASE_URL + "storage/images?url="
    const val BASE_IMAGE = "https://static.vecteezy.com/system/resources/previews/056/202/171/non_2x/add-image-or-photo-icon-vector.jpg"
    const val BASE_AVT ="https://sbcf.fr/wp-content/uploads/2018/03/sbcf-default-avatar.png"
    enum class metadata(value: String){
        OTP_ACTION("OTP_ACTION"),
        ROLE_NAME("ROLE_NAME"),
        USER_STATUS("USER_STATUS"),
        ORDER_STATUS("ORDER_STATUS"),
        PAYMENT_METHOD("PAYMENT_METHOD"),
    }

}