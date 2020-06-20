package com.example

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.gt
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId


class MongoDataHandler() {
    val database: MongoDatabase
    val spaceshipsCollection: MongoCollection<SpaceShip>

    init {
        val pojoCodecRegistry: CodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
        val codecRegistry: CodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            pojoCodecRegistry
        )

        val clientSettings = MongoClientSettings.builder()
            .codecRegistry(codecRegistry)
            .build()

        val mongoClient = MongoClients.create(clientSettings)
        database = mongoClient.getDatabase("development");
        spaceshipsCollection = database.getCollection(SpaceShip::class.java.name, SpaceShip::class.java)
        initSpaceShips()
    }

    fun initSpaceShips() {
        deleteAllSpaceShips()
        spaceshipsCollection.insertOne(SpaceShip(null, "Milkyway410", 11.3f))
        spaceshipsCollection.insertOne(SpaceShip(null, "Mercury12", 99.9f))
        spaceshipsCollection.insertOne(SpaceShip(null, "Saturn23", 0.1f))
        spaceshipsCollection.insertOne(SpaceShip(null, "Pluto22", 5.2f))
        spaceshipsCollection.insertOne(SpaceShip(null, "Jupiter9", 88.6f))
        spaceshipsCollection.insertOne(SpaceShip(null, "EarthMoon59", 55.5f))
        spaceshipsCollection.insertOne(SpaceShip(null, "test1", 55.5f))
        spaceshipsCollection.insertOne(SpaceShip(null, "test2", 12.3f))
        spaceshipsCollection.insertOne(SpaceShip(null, "test3", 22.3f))
        spaceshipsCollection.insertOne(SpaceShip(null, "test4", 36.3f))
    }

    fun shipsSortedByFuelAndPaged(pageno: Int, pageSize: Int): List<SpaceShip> {
        return spaceshipsCollection
            .find()
            .sort(Document(mapOf(Pair("fuel", -1), Pair("_id", -1))))
            .skip(pageno - 1)
            .limit(pageSize)
            .toList()
    }

    fun shipsWithMoreFuelThan(fuellimit: Float): List<SpaceShip> {
        return spaceshipsCollection.find(gt("fuel", fuellimit)).toList()
    }

    fun fuelUpSpaceShip(hexId: String) {
        spaceshipsCollection.updateOne(eq("_id", ObjectId(hexId)), Document("\$set", Document("fuel", 99.9f)))
    }

    fun replaceSpaceShip(ship: SpaceShip) {
        spaceshipsCollection.replaceOne(eq("_id", ship.id), ship)
    }

    fun deleteAllSpaceShips() {
        spaceshipsCollection.deleteMany(Document())
    }

    fun findOneSpaceShip(hexid: String): SpaceShip? {
        return spaceshipsCollection.find(eq("_id", ObjectId(hexid))).first()
    }

    fun allSpaceShips(): List<SpaceShip> {
        val mongoResult = spaceshipsCollection.find()
        mongoResult.forEach {
            print("ship: $it")
        }
        return mongoResult.toList()
    }
}