package com.syb.syblibrary.service.fingerpush

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FingerPushMessageData(
    /**
     * 메세지 번호
     */
    @Expose
    @SerializedName("msgTag")
    var msgTag: String = "",
    /**
     * CD:1;IM:0;PT:DEFT
     * (CD:커스텀데이터 여부(0없음, 1있음), IM:이미지여부(0없음, 1있음) ,PT:메세지타입 (DEFT:일반, LNGT:롱푸시, STOS:타겟푸시)
     */
    @Expose
    @SerializedName("code")
    var code: String = "",
    /**
     * 보낸시간
     */
    @Expose
    @SerializedName("time")
    var time: Long = 0,
    /**
     * 핑거푸시 앱이름
     */
    @Expose
    @SerializedName("appTitle")
    var appTitle: String = "",
    /**
     * 뱃지
     */
    @Expose
    @SerializedName("badge")
    var badge: Int = 0,
    /**
     * 사운드
     */
    @Expose
    @SerializedName("sound")
    var sound: String = "",
    /**
     * 메세지 제목
     */
    @Expose
    @SerializedName("title")
    var title: String = "",
    /**
     * 메세지 내용
     */
    @Expose
    @SerializedName("message")
    var message: String = "",
    /**
     * 웹링크 url
     */
    @Expose
    @SerializedName("weblink")
    var weblink: String = "",
    /**
     * 라벨코드
     */
    @Expose
    @SerializedName("labelCode")
    var labelCode: String = "",
    /**
     * 이미지 여부
     * (0:없음;1:있음)
     */
    @Expose
    @SerializedName("img")
    var img: Int = -1,
    /**
     * 이미지 url
     */
    @Expose
    @SerializedName("imgUrl")
    var imgUrl: String = "",
    /**
     * 푸시 웹뷰 인앱 여부
     */
    @Expose
    @SerializedName("inApp")
    var inApp: String = "Y"
): Serializable