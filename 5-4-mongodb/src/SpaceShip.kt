package com.example

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

class SpaceShip(id: ObjectId?, name: String, fuel: Float) {
    @BsonId
    var id: ObjectId?
    var fuel: Float
    @BsonProperty(value = "shipname")
    var name: String

    constructor() : this(null, "not_set", 5.0f){}
    init{
        this.id = id
        this.fuel = fuel
        this.name = name
    }

    fun getIdAsHex(): String{
        return id!!.toHexString()
    }
}