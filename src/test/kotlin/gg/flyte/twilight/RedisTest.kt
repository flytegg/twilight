package gg.flyte.twilight

import gg.flyte.twilight.redis.Redis

fun addingListenersTest(){

    val listener = Redis.addListener("cool-channel"){
        println("The following message was received: '$message' on channel '$channel'")
        this.listener.unregister()
    }


    Redis.set("cool-key", "super-secret-value")

    val future = Redis.get("cool-key") // Returns a Completable Future

    future.thenApplyAsync {
        value -> println("The value is: $value") // Prints: "The value is: super-secret-value"
    }.exceptionally {
            e -> println("An exception occurred: ${e.message}") // Handle the Exception
    }

    Thread.sleep(1000)

    Redis.delete("cool-key")




}