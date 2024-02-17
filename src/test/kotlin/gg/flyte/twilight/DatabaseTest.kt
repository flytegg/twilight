package gg.flyte.twilight

import gg.flyte.twilight.data.sql.QueryBuilder
import gg.flyte.twilight.data.sql.SQLSerializable
import gg.flyte.twilight.data.sql.SQLWrapper;
import gg.flyte.twilight.data.sql.toListOfObjects
import gg.flyte.twilight.environment.Environment


data class Person(val id: Int = 0, val name: String = "", val age: Int = 0) : SQLSerializable

fun main() {
    val person = Person(1, "Test", 13)
    // POJO test
    assert(person.toInsertQuery("people") == "INSERT INTO people (age, id, name) VALUES ('13', '1', 'Test')") { "SQL INSERT Query did not match" }
    //
    val password = Environment.get("sql_password")
    val user = Environment.get("sql_username")
    val url = Environment.get("sql_url")
    /* As a heads-up may need to shade a mysql/postgres driver for this to work!
    such as
    implementation("org.postgresql:postgresql:42.7.1")
    */
    val database = SQLWrapper(url, user, password);

    database.connect()

    assert(database.execute("CREATE TABLE IF NOT EXISTS PEOPLE(id INT PRIMARY KEY, name TEXT, age INT);")) { "Failed to create table!" }

    assert(database.execute(person.toInsertQuery("people"))) { "Failed to insert a person into the table!" }

    // getting POJO's

    val people = database.executeQuery(QueryBuilder().select("*").from("people").where("id=1").buildSelectQuery())?.toListOfObjects<Person>()

    println("People returned from database: $people")
}