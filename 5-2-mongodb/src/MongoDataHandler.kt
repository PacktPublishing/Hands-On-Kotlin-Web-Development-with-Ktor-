package com.example

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider


class MongoDataHandler() {
    val database: MongoDatabase
    val spaceshipsCollection: MongoCollection<SpaceShip>
    init{
        val pojoCodecRegistry: CodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build())
        val codecRegistry: CodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry)

        val clientSettings = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .build()

        val mongoClient = MongoClients.create(clientSettings)
        database = mongoClient.getDatabase("development");
        spaceshipsCollection = database.getCollection(SpaceShip::class.java.name, SpaceShip::class.java)
        initSpaceShips()
    }

    fun initSpaceShips(){
        spaceshipsCollection.insertOne(SpaceShip(null, "test1", 62.3f ))
        spaceshipsCollection.insertOne(SpaceShip(null, "test2", 12.3f ))
        spaceshipsCollection.insertOne(SpaceShip(null, "test3", 22.3f ))
        spaceshipsCollection.insertOne(SpaceShip(null, "test4", 36.3f ))
    }

    fun allSpaceShips(): List<SpaceShip>{
        val mongoResult = spaceshipsCollection.find()
        mongoResult.forEach{
            print("ship: $it")
        }
        return mongoResult.toList()
    }
}