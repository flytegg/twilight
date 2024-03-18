package gg.flyte.twilight

import gg.flyte.twilight.redis.Redis

fun addingListenersTest(){

    val listener = Redis.addListener("cool-channel"){
        println("The following message was received: '$message' on channel '$channel'")
        this.listener.unregister()
    }



}