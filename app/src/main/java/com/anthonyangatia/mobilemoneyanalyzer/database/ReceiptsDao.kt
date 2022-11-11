package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anthonyangatia.mobilemoneyanalyzer.util.AmountTransacted
import com.anthonyangatia.mobilemoneyanalyzer.util.CategoryAmount
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptsDao{
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(receipt: Receipt):Long
//
//    @Query("SELECT * from transaction_receipt_table WHERE receipt_code = :code ")
//    suspend fun getReceipt(code:String): Receipt?
//
//    @Query("SELECT * from transaction_receipt_table WHERE receiptId = :id ")
//    suspend fun getReceipt(id:Long): Receipt?

    @Query("SELECT * from transaction_receipt_table")
    fun getAllReceipts(): LiveData<List<Receipt>?>

    @Query("SELECT * from transaction_receipt_table WHERE date BETWEEN :beginningDate AND :endDate")
    suspend fun getReceiptWhereDate(beginningDate:Long , endDate:Long ): List<Receipt>?

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived from transaction_receipt_table WHERE date BETWEEN :beginningDate AND :endDate")
    suspend fun getAmountTransactedList(beginningDate:Long , endDate:Long ): AmountTransacted?

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived FROM transaction_receipt_table WHERE date BETWEEN :beginningDate AND :endDate AND transaction_type = :transactionType")
    suspend fun getSumOfTransactionType(beginningDate:Long , endDate:Long, transactionType:String):AmountTransacted?

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived FROM transaction_receipt_table WHERE name = :name")
    suspend fun amountTransactedByPerson(name: String):List<AmountTransacted>?

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived FROM transaction_receipt_table WHERE name = :name AND date BETWEEN :beginningDate AND :endDate")
    suspend fun amountTransactedByPersonBetweenTime(name: String, beginningDate: Long, endDate: Long): List<AmountTransacted>?

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived FROM transaction_receipt_table WHERE name = :name AND date BETWEEN :beginningDate AND :endDate")
    suspend fun highestAmountSent(name: String, beginningDate: Long, endDate: Long):List<AmountTransacted>?//Not sure of this query

    @Query("SELECT * FROM transaction_receipt_table LIMIT 1")
    suspend fun getLastReceipt():Receipt?

    @Query("DELETE FROM transaction_receipt_table")
    suspend fun clear()
//TODO:(cleared) Figure out why coroutines is not working:https://developer.android.com/codelabs/kotlin-android-training-coroutines-and-room#5

    //Part 2
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(receipt: Receipt):Long

    @Query("SELECT * from transaction_receipt_table WHERE receipt_code = :code ")
    suspend fun getReceipt(code:String): Receipt?

    @Query("SELECT * from transaction_receipt_table WHERE receiptId = :id ")
    fun getReceipt(id:Long): LiveData<Receipt>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPerson(personAndBusiness: PersonAndBusiness) //You can return lo if you want to get the id
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE) //Deprecated
//    suspend fun insertBusiness(business: Business)

    @Query("SELECT * from transaction_receipt_table WHERE receipt_message LIKE :searchQuery")
    fun searchReceipt(searchQuery: String): Flow<List<Receipt>>

    @Query("SELECT * from personandbusiness WHERE name LIKE :searchQuery OR phoneNumber LIKE :searchQuery")
    fun searchPerson(searchQuery: String): Flow<List<PersonAndBusiness>>

    @Query("SELECT * from personandbusiness WHERE name = :name ")
    suspend fun getPerson(name: String):PersonAndBusiness

    @Query("SELECT * from personandbusiness")
    fun getPeopleAndBusiness():LiveData<List<PersonAndBusiness>>

//    @Query("SELECT * from business") //Deprecated
//    fun getBusiness():LiveData<List<Business>>

    @Query("DELETE FROM personandbusiness")
    suspend fun clearPersonAndBusiness()

//    @Query("DELETE FROM business") // deprecated
//    suspend fun clearBusiness()

    @Query("SELECT * from transaction_receipt_table WHERE name = :name")
    fun getReceiptsWherePerson(name: String): LiveData<List<Receipt>>

    @Query("SELECT SUM(amount_sent) as totalSent, SUM(amount_received) as totalReceived FROM transaction_receipt_table WHERE name = :name")
    suspend fun getSumOfTransactionOfPerson(name: String):AmountTransacted?

    @Query("UPDATE personandbusiness SET target_expenditure = :target WHERE name = :name")
    suspend fun updateExpense(name: String, target: Double)

    @Query("SELECT * from target WHERE name = :name")
    fun getTargetOfPerson(name: String): LiveData<List<Target>>

    @Query("SELECT * from target")
    fun getAllTargets(): LiveData<List<Target>>
    @Query("SELECT account_balance from transaction_receipt_table LIMIT 1")//The query should change and get where the balance is not null
    suspend fun getBalance():Double?

    @Query("UPDATE transaction_receipt_table SET  category= :category WHERE receiptId = :id")
    suspend fun classify(category:String, id:Long)

    @Query("SELECT SUM(amount_sent) as totalSent FROM transaction_receipt_table WHERE transaction_type = :transactionType")
    suspend fun getTotalAmountByTransactionType(transactionType: String):Double?

    @Query("SELECT DISTINCT category FROM transaction_receipt_table")
    suspend fun getAllCategories():List<String?>

    @Query("SELECT SUM(amount_sent) as amount FROM transaction_receipt_table WHERE category = :category")
    suspend fun getCategoryAndAmount(category:String?):Double

// LIKE "SELECT * FROM table '%' || :searchQuery || '%' "

}