
# Twilight ✨

Twilight is an API for developers creating plugins for Spigot or Paper based Minecraft servers. It contains a wide range of utilities and QOL improvements, from inventories, to schedulers and databases.

Twilight is built using **Kotlin**, and is recommended for usage with. Many features of Twilight should work with plain Java, though compatability is not guaranteed.

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
  <version>1.0.29</version>
</dependency>
```

Gradle (Groovy DSL)
```groovy
maven {
    url "https://repo.flyte.gg/releases"
}

implementation "gg.flyte:twilight:1.0.29"
```

Gradle (Kotlin DSL)
```kotlin
maven("https://repo.flyte.gg/releases")

implementation("gg.flyte:twilight:1.0.29")
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

### Additional Events

Twilight provides additional events which are not found in Spigot or Paper. These are:
- PlayerMainHandInteractEvent
- PlayerOffHandInteractEvent
- PlayerOpChangeEvent
- PlayerOpEvent
- PlayerDeopEvent

You can opt out of Twilight calling these events. For example:

```kotlin
disableCustomEventListeners(OpEventListener, InteractEventListener)
```


### Scheduler
Bukkit's build in scheduler is tedious at best, so Twilight takes advantage of beautiful Kotlin syntax to make it easier to write, as well as adding a custom TimeUnit to save you calculating ticks.

How to schedule a single task to run on Bukkit's main thread either sync or async:

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

### Databases
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
And use the standard features of the Mongo Java Driver with your `MongoCollection`.

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

Currently the only way to configure your MongoDB "cache" for UUIDs and names, is to have an Environment variable called `NAME_CACHE_COLLECTION` with the value being what you want to call the collection.

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
