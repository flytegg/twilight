
# Twilight ✨

Twilight is an API for developers creating plugins for Spigot or Paper based Minecraft servers. It contains a wide range of utilities and QOL improvements, from inventories, to schedulers and databases.

Twilight is built using **Kotlin**, and is recommended for usage with. Many features of Twilight should work with plain Java, though compatibility is not guaranteed.

For support, questions or to chat with the team, come join the Discord:

[![Discord Banner](https://discordapp.com/api/guilds/835561528299880518/widget.png?style=banner2)](https://discord.gg/flyte)

## Setup
Twilight should be bundled within your plugin. Add the following repository and dependency to your build tool:

Maven
```xml
<repository>
    <id>flyte-repository-releases</id>
    <name>Flyte Repository</name>
    <url>https://repo.flyte.gg/releases</url>
</repository>

<dependency>
    <groupId>gg.flyte</groupId>
    <artifactId>twilight</artifactId>
    <version>1.1.10</version>
</dependency>
```

Gradle (Groovy DSL)
```groovy
maven {
    url "https://repo.flyte.gg/releases"
}

implementation "gg.flyte:twilight:1.1.10"
```

Gradle (Kotlin DSL)
```kotlin
maven("https://repo.flyte.gg/releases")

implementation("gg.flyte:twilight:1.1.10")
```

Certain features of Twilight require configuration, which can be done via the Twilight class. To setup a Twilight class instance, you can use the `twilight` function as shown below:
```kotlin
val twilight = twilight(this)
```

If you want to make use of environment variables (.env files), you can configure your usage of these here, like so:
```kotlin
val twilight = twilight(this) {
    env {
        useDifferentEnvironments = true
        devEnvFileName = ".env.dev"
        prodEnvFileName = ".env.prod"
    }
}
```
The above are the default env configuration settings.

If you `useDifferentEnvironments`, you'll need a `.env` file which contains the following:
```env
ENVIRONMENT=DEV # or PROD
```
This file determines whether to use your dev .env or your prod .env.

If you do not use this different environments feature, then it will just use the .env (or whatever you specify the name as with `prodEnvFileName`)

Throughout your project you can use `Environment.get("VARIABLE")` to retrieve a value from your environment variables.

Other features that can be configured in the Twilight class builder will have their own sections later in the README.

## Features

### Extension Functions

Twilight takes advantage of Kotlin's extension functions to add additional functions to various classes used within the API. Many of these are convenience functions, and some add complete new functionality. To see all of the functions added, view them [inside the code](https://github.com/flytegg/twilight/tree/master/src/main/kotlin/gg/flyte/twilight/extension).

### Events
We have a neat way to handle code for events, which register by themselves so you don't have to!

You can make use of it like so:
```kt
event<PlayerJoinEvent> {
    player.sendMessage("Welcome to the server!")
}
```
If you need to change the `eventPriority` or the `ignoreCancelled`, you can pass it to the function call like:
```kt
event<PlayerJoinEvent>(EventPriority.HIGHEST, true) {
    player.sendMessage("Welcome to the server!")
}
```

If you ever need an instance of the Listener class that the event gets put in to, it's returned by the function. Specifically, it returns `TwilightListener`. This class has a convenience function in for unregistering the listener, it can be used like so:
```kt
val listener = event<PlayerJoinEvent> {
    player.sendMessage("Welcome to the server!")
}

listener.unregister()
```

### Making your own events with Twilight
Instead of having to extend the org.bukkit.event.Event yourself, you can extend TwilightEvent!

Here's an example:
```kt
class MyCustomEvent : TwilightEvent() {

}
```

This is much easier than the standard Bukkit Event, as you don't have to worry about defining handles, etc.

The TwilightEvent also includes a timestamp which provides an Instant for when the event was ran:

```kt
// with the above event example
event<MyCustomEvent> {
    println("time event was executed: $timestamp")
}
```

You can also declare that it should be asyncronous by passing a `true` value to the TwilightEvent constructor:
```kt
class MyCustomEvent : TwilightEvent(true) {

}

class MyCustomEvent(async: Boolean) : TwilightEvent(async) {

}
```

### Additional Events

Twilight provides additional events which are not found in Spigot or Paper. These are:
- PlayerMainHandInteractEvent
- PlayerOffHandInteractEvent
- PlayerOpChangeEvent
- PlayerOpEvent
- PlayerDeopEvent
- ChatClickEvent [(see below)](#custom-chatclickevent)

You can opt out of Twilight calling these events. For example:

```kotlin
disableCustomEventListeners(OpEventListener, InteractEventListener)
```
### Custom ChatClickEvent

Due to limitations imposed by the Minecraft server software, when interacting with a clickable message in chat or in a book the only response options are `RUN_COMMAND`, `SUGGEST_COMMAND`, `CHANGE_PAGE`, `COPY_TO_CLIPBOARD`, `OPEN_FILE` and `OPEN_URL`. None of these match the most common use case: running custom code. Twilight utilizes the `RUN_COMMAND` response to call a custom `ChatClickEvent` which can be listened to like a regular event.

To use this feature, where you would normally build your clickable message, use the Twilight extension functions to add a custom click event. Twilight will then redirect any data which you  put in the parameters to be accessable as a variable from within the `ChatClickEvent` when the player clicks the message.

For Paper/Adventure (recommended):
```kotlin
import net.kyori.adventure.text.Component

Component.text("Click here")
    .customClickEvent("openGUI", "warps")
```

Or for Spigot/BungeeCord:
```kotlin
import net.md_5.bungee.api.chat.TextComponent

TextComponent("Click here")
    .customClickEvent("openGUI", "warps")
```

From there, simply listen to the event as normal, and access the data attached to the message the player has clicked. In this basic example, information to open a "warps" GUI has been passed through as the custom data, and so the correct action can be taken:

```kotlin
event<ChatClickEvent> {
    if (data.size != 2) return@event
    if (data[0] != "openGUI") return@event
    when (data[1]) {
        "warps" -> GUIManager.openWarps(player)
            ..
    }
}

```

### Scheduler
Bukkit's built in scheduler is tedious at best, so Twilight takes advantage of beautiful Kotlin syntax to make it easier to write, as well as adding a custom TimeUnit to save you calculating ticks.

How to schedule a single task to run on Bukkit's main thread either sync or async:
`
```kotlin
sync {
    println("I am a sync BukkitRunnable")
}

async {
    println("I am an async BukkitRunnable")
}
```

How to schedule a delayed task, with optional custom time unit and async parameters:

```kotlin
// SYNC
delay {
    println("I am a sync BukkitRunnable delayed by 1 tick")
}

delay(10) {
    println("I am a sync BukkitRunnable delayed by 10 ticks")
}

delay(1, TimeUnit.SECONDS) {
    println("I am a sync BukkitRunnable delayed by 1 second")
}

// ASYNC
delay(10, true) {
    println("I am an async BukkitRunnable delayed by 10 ticks")
}

delay(1, TimeUnit.SECONDS, true) {
    println("I am an async BukkitRunnable delayed by 1 second")
}
```

How to schedule a repeating task, with optional custom time unit and async parameters:
```kotlin
// SYNC
repeat(10) {
    println("I am a sync BukkitRunnable running every 10 ticks")
}

repeat(10, TimeUnit.SECONDS) {
    println("I am a sync BukkitRunnable running every 10 seconds")
}

repeat(5, 10) {
    println("I am a sync BukkitRunnable running every 10 ticks, waiting 5 before starting")
}

repeat(5, 10, TimeUnit.SECONDS) {
    println("I am a sync BukkitRunnable running every 10 seconds, waiting 5 before starting")
}

// ASYNC
repeat(10, true) {
    println("I am an async BukkitRunnable running every 10 ticks")
}

repeat(10, TimeUnit.SECONDS, true) {
    println("I am an async BukkitRunnable running every 10 seconds")
}

repeat(5, 10, true) {
    println("I am an async BukkitRunnable running every 10 ticks, waiting 5 before starting")
}

repeat(5, 10, TimeUnit.SECONDS, true) {
    println("I am an async BukkitRunnable running every 10 seconds, waiting 5 before starting")
}
```

> Twilight `repeat` conflicting with Kotlin's `repeat`? As an alternative, you can use `repeatingTask`. 

## Databases
### MongoDB
Currently we have support for MongoDB. To configure it, you can take one of two routes:

#### Environment variables
You can use the following Environment variables for your MongoDB:
```env
MONGO_URI="your URI string"
MONGO_DATABASE="your database name"
```

#### Builder
When building your Twilight instance, you can specify your URI and database like so:
```kotlin
val twilight = twilight(plugin) {
    mongo {
        uri = "your URI string"
        database = "your database name"
    }
}
```

From here you can use the following function to get a collection from your database:
```kotlin
MongoDB.collection("my-collection")
```
And use the standard features of the Mongo Sync Driver with your `MongoCollection`.

**OR** you can use some of our custom features, making communicating with a Mongo database infinitely easier. Here's how you do it:

```kt
class Profile(
    @field:Id val id: UUID,
    val name: String
) : MongoSerializable
```

What's happening here? We're declaring what should be used as the key identifier for our class in the database, we can do so by annotating a field with `@field:Id`.

We also implement an interface `MongoSerializable`. This gives us access to a bunch of methods which make our lives really easy when it comes to moving between our class instance and our database.

For example, I could do the following:
```kt
val profile = Profile(UUID.randomUUID(), "Name")

profile.save()
// this returns a CompletableFuture of the UpdateResult
// or we could do profile.saveSync() if we need it to be sync, does not return wrapped by a CompletableFuture

profile.delete()
// this returns a CompletableFuture of the DeleteResult
// same as save, can do profile.deleteSync() for sync, does not return wrapped by a CompletableFuture
```

If we ever want to find and load and instance of our class from the database, we can use some functions from the TwilightMongoCollection:

```kt
val collection = MongoDB.collection<Profile>() // by default this assumes the name of the collection is the plural camel case of the type, f.x. Profile -> profiles, SomeExampleThing -> someExampleThings
// you can specify the name of the collection if you wish it to be different like so
val collection = MongoDB.collection<Profile>("myCollection")
collection.find() // returns a CompletableFuture<MongoIterable<Profile>>
collection.find(BsonFilter) // returns a CompletableFuture<MongoIterable<Profile>>
collection.findById(id) // id must be the same type as the field marked as the id on the class, returns a CompletableFuture<MongoIterable<Profile>>
collection.delete(BsonFilter) // returns a CompletableFuture<DeleteResult>
collection.deleteById(id) // id must be the same type as the field marked as the id on the class, returns a CompletableFuture<DeleteResult>
// all of these have sync versions which follow the same pattern, f.x. collection.findSync(), where the return value is the same as the async version, just not wrapped by a CompletableFuture
```

If we need something that isn't already wrapped by the TwilightMongoCollection, it exposes us the MongoCollection of Documents, which we can get with `collection.documents`.

### SQL (MySQL, Postgres)

#### Getting Started
To get started you need to create an instance of the SQL Wrapper like so
```kotlin
val db = SQLWrapper(url = "your db url", user="user", password="password")
db.connect()
```

#### QueryBuilder
The QueryBuilder class will help you in creating everything from simple queries like SELECT's to even complex JOIN's.
All you need to start is an instance of the query builder, here's an example usage
```kotlin
val queryBuilder = QueryBuilder()

// Example SELECT query
val selectQuery = queryBuilder.select("id", "name").from("person").where("age" gt 18).build()

// Example INSERT query
val insertQuery = queryBuilder.insertInto("person", "id", "name").values(1, "John").build()

// Example UPDATE query
val updateQuery = queryBuilder.update().table("person").set("name", "John Doe").where("id" eq 1).build()

// Example DELETE query
val deleteQuery = queryBuilder.delete().table("person").where("id = 1").build()
```
#### Using objects in your database
If you would like to retrieve and store data as objects within your database there a some methods provided for this


1 - Your object must implement SQLSerializable 
2 - You must have a table that fits the structure of your object, you can create by calling `convertToSQLTable()` on your object, then execute the statement like so
```kotlin
// NOTE: convertToSQLTable() takes a optional dialect parameter, at this time the only additional dialect is postgres
val createTable = yourObjectInstace.convertToSQLTable()

if(db.execute(createTable)) {
    // successfully executed
}
```
3 - To insert your object call `toInsertQuery()` like so
```kotlin
val insertToTable = yourObjectInstace.toInsertQuery()

if(db.execute(insertToTable)) {
    // successfully executed
}
```

4 - To retrieve objects from your database you call a select statement like normal but call `toListOfObjects<Type>()` on the returned `Results` class

#### Running queries
Once you have your query using either the QueryBuilder or your own you can run it like so
```kotlin
val result = result.executeQuery(selectQuery)
```
once you have run the query it will return a `Results` class, it can be used like so
```kotlin
result?.let { res ->
  println("MyColumn Value: " + res["my_column"])
}
```
The results class contains a list of all the rows and columns returned by the database.
You can access any of the columns values the same way you would with a map.

### Ternary Operator
There is a basic ternary operator implementation added which can be used like so:
```kotlin
val test = false
println(test then "yes" or "no")
```
This doesn't yet work for evaluating functions either side of the ternary though, we plan to figure this out in the near future.

### UUID ⟷ Name
Twilight can do the heavy lifting and query the Mojang API to find the UUID from name or name from UUID of a player, particularly useful for networks. Twilight will cache responses in an attempt to not break the rate limit imposed by Mojang.

If you have a UUID and you want to get a name, you can call `nameFromUUID`:
```kotlin
NameCacheService.nameFromUUID(UUID.fromString("a008c892-e7e1-48e1-8235-8aa389318b7a"))
```
This will look up your cache to see if we already know the name, otherwise we will check the MongoDB "cache" of key, value pairs, and finally, we'll query Mojang if we still don't know it.

After each step the key, value pair will be stored so the next call is just on the cache.

Similarly, if you have a name and want to get the UUID, you can call `uuidFromName`:
```kotlin
NameCacheService.uuidFromName("stxphen")
```

Currently the only way to configure your MongoDB "cache" for UUIDs and names, is to have an Environment variable called `NAME_CACHE_COLLECTION` with the value being what you want to call the collection. Don't want to use the Mongo cache? Disable `useMongoCache` in the settings. 

# Redis
Twilight has a Redis system that lets you set/get/delete string key value pairs, additionally, you are able to publish messages and listen to incoming messages on any channel you'd like.

#### Environment variables
You can use the following Environment variables for your Redis Server:
```env
REDIS_HOST="your redis server host"
REDIS_PORT="your redis server port"
REDIS_TIMEOUT="your redis connection timeout"
REDIS_AUTHENTICATION="NONE"
```
Alternatively, if your Redis server requires a Username + Password in order to access, you can use the following:
```env
REDIS_HOST="your redis server host"
REDIS_PORT="your redis server port"
REDIS_TIMEOUT="your redis connection timeout"
REDIS_AUTHENTICATION="USERNAME_PASSWORD"
REDIS_USERNAME:"coolUsername"
REDIS_PASSWORD:"coolPassword"
```
Alternatively, if your Redis server requires a URL in order to access, you can use the following:
```env
REDIS_HOST="your redis server host"
REDIS_PORT="your redis server port"
REDIS_TIMEOUT="your redis connection timeout"
REDIS_AUTHENTICATION="URL"
REDIS_URL="coolURL"
```

#### Builder
When building your Twilight instance, you can specify your host and port like so:
```kotlin
val twilight = twilight(plugin) {
    redis {
        authentication = Authentication.NONE
        host = "your redis server host"
        port = 6379 // Default Redis Port
        timeout = 500 // 500 Milliseconds Timeout
    }
}
```
Alternatively, if your Redis server requires a Username + Password in order to access, you can use the following:
```kotlin
val twilight = twilight(plugin) {
    redis {
        authentication = Authentication.USERNAME_PASSWORD
        host = "your redis server host"
        port = 6379 // Default Redis Port
        timeout = 500 // 500 Milliseconds Timeout
        username = "coolUsername"
        password = "coolPassword"
    }
}
```
Alternatively, if your Redis server requires a URL in order to access, you can use the following:
```kotlin
val twilight = twilight(plugin) {
    redis {
        authentication = Authentication.URL
        host = "your redis server host"
        port = 6379 // Default Redis Port
        timeout = 500 // 500 Milliseconds Timeout
        url = "coolURL"
    }
}
```
#### String Key-Value Pairs
You can Set/Get/Delete String Key-Value pairs on your Redis server like so: (All of those functions are Async and return a CompleteableFuture)
```kotlin
Redis.set("cool-key", "super-secret-value")

val future = Redis.get("cool-key") // Returns a Completable Future

future.thenApplyAsync {
    value -> println("The value is: $value") // Prints: "The value is: super-secret-value"
}.exceptionally {
    e -> println("An exception occurred: ${e.message}") // Handle the Exception
}

Thread.sleep(1000)

Redis.delete("cool-key")
```
#### Publishing Messages
You can publish messages like so:

```kotlin
Redis.publish("channel", "message") // Async Publishing
```
#### Redis Listeners (PubSub)
##### Listen to incoming messages
You are able to listen to incoming message on specific channels, using the 'TwilightRedisListener' Class:
```kotlin
// Extend the 'TwilightRedisListener' class and override the 'onMessage' function.
class PlayerConnectionRedisListener(): TwilightRedisListener("player-connection") {
    override fun onMessage(message: String) {
        // do stuff
    }
}
```
You can add add/register the listener like this: (which also returns the listener which lets you unregister it if you'd like)
```kotlin
val listener = Redis.addListener(PlayerConnectionRedisListener())
listener.unregister() // unregistering the listener.
```
Alternativley, instead of extending the listener class, you can add a listener using a block of code, which returns the 'RedisMessage' data class, which contains the channel, the message, and the listener:
```kotlin
val listener = Redis.addListener("cool-channel"){
    println("The following message was received: '$message' on channel '$channel'")
    this.listener.unregister() // unregistering the listener after we recieved the message.
}
```

### Files Extensions

#### File.hash()
You can easily get the hash of a file using this method. The parameter `algorithm` is used to define which should be used for the hash, the default is SHA-256.

If you need to hold a reference to a file at a remote location you can use the `RemoteFile` class provided here. This allows you to easily get the hash of a remote file with `RemoteFile#hash`.

This is particularly useful when used in parallel with our other open-source library, [resource-pack-deploy](https://github.com/flytegg/resource-pack-deploy), so you can get the hash of the latest resource pack file, and use it to reset a client's cached version of your resource pack if there are any differences.

### Symbols
Twilight offers a collection of widely used symbols within the `Symbols` object.

These include, but are not limited to:
- • (BULLET)
- ❤ (HEART)
- ★ (STAR)
- » (SMALL_RIGHT)
- █ (SQUARE)
- ⬤ (CIRCLE)
- ⛏ (PICKAXE)
- ⛀ (COIN)
- ⛁ (COINS)

### Libraries
Twilight is bundled with some useful libraries, some include:
- Bson
- Dotenv
- Mongo Sync Driver
- GSON

#### GSON
We're aiming to provide some standard GSON Type Adapters for ease of use. Currently, we have adapters for the following:
- Location

We have a useful exclusion strategy, allowing you to exclude marked fields of a class from being in serialized JSON. All you need to do is annotate the class field with `@Exclude`!

Make sure to use our `GSON` import rather than making your own/own builder, as the Gson instance has to declare the ExclusionStrategy.
