package com.example.model

import com.example.ui.login.Session
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.*
import org.bson.BsonDocument
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

enum class DataManagerMongoDB {
    INSTANCE;
    val log = LoggerFactory.getLogger(DataManagerMongoDB::class.java)
    val database: MongoDatabase
    val bookCollection: MongoCollection<Book>
    val cartCollection: MongoCollection<Cart>

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
        bookCollection = database.getCollection(Book::class.java.simpleName, Book::class.java)
        cartCollection = database.getCollection(Cart::class.java.simpleName, Cart::class.java)
        initBooks()
    }

    fun initBooks(){
        bookCollection.deleteMany(BsonDocument())
        cartCollection.deleteMany(BsonDocument())
        bookCollection.insertOne(
            Book(
                null,
                "How to grow apples",
                "Mr. Appleton",
                100.0f
            )
        )
        bookCollection.insertOne(
            Book(
                null,
                "How to grow oranges",
                "Mr. Orangeton",
                90.0f
            )
        )
        bookCollection.insertOne(
            Book(
                null,
                "How to grow lemons",
                "Mr. Lemon",
                110.0f
            )
        )
        bookCollection.insertOne(
            Book(
                null,
                "How to grow pineapples",
                "Mr. Pineapple",
                100.0f
            )
        )
        bookCollection.insertOne(
            Book(
                null,
                "How to grow pears",
                "Mr. Pears",
                120.0f
            )
        )
        bookCollection.insertOne(
            Book(
                null,
                "How to grow coconuts",
                "Mr. Coconut",
                130.0f
            )
        )
        bookCollection.insertOne(
            Book(
                null,
                "How to grow bananas",
                "Mr. Appleton",
                120.0f
            )
        )
    }

    fun newBook(book: Book): Book {
        bookCollection.insertOne(book)
        return book
    }

    fun updateBook(book: Book): Book {
        val bookfound = bookCollection.find(Document("_id", book.id)).first()
        bookfound?.title = book.title
        bookfound?.author = book.author
        bookfound?.price = book.price
        return bookfound!!
    }

    fun deleteBook(bookid: String): Book {
        val bookfound = bookCollection.find(eq("_id", bookid)).first()
        bookCollection.deleteOne(eq("_id", ObjectId(bookid)))
        return bookfound!!
    }

    fun allBooks(): List<Book>{
        return bookCollection.find().toList()
    }

    fun sortedBooks(sortby: String, asc: Boolean): List<Book> {
        val pageno = 1
        val pageSize = 1000
        val ascint: Int = if(asc) 1 else -1
        return bookCollection
            .find()
            .sort(Document(mapOf(Pair(sortby, ascint), Pair("_id", -1))))
            .skip(pageno - 1)
            .limit(pageSize)
            .toList()
    }

    fun searchBooks(str: String): List<Book> {
        return bookCollection
            .find(
                or(
                regex("title", ".*$str.*"),
                regex("author", ".*$str.*")
                )
                )
            .sort(Document(mapOf(Pair("title", 1), Pair("_id", -1))))
            .toList()
    }

    fun updateCart(cart:Cart){
        val replaceOne = cartCollection.replaceOne(eq("username", cart.username), cart)
        log.info("Update result: $replaceOne")
    }

    fun addBook(session: Session?, book: Book){
        val cartForUser = cartForUser(session)
        cartForUser.addBook(book)
        updateCart(cartForUser)
    }

    fun cartForUser(session: Session?): Cart{
        if (session == null)
            throw IllegalArgumentException("Session is null")
        val find = cartCollection.find(eq("username", session.username))


        if (find.count() == 0){
            val cart = Cart(username=session.username)
            cartCollection.insertOne(cart)
            return cart
        }
        else
            return find.first()
    }

    fun getBookWithId(bookid: String): Book {
        log.info("Get book with id: $bookid")
        return bookCollection.find(eq("_id", ObjectId(bookid))).first()
    }

    fun removeBook(session: Session?, book: Book) {
        val cartForUser = cartForUser(session)
        cartForUser.removeBook(book)
        updateCart(cartForUser)
    }
}