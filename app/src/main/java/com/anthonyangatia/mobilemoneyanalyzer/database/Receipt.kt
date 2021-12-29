package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.*
import java.sql.Date

@Entity(tableName = "transaction_receipt_table")
data class Receipt(
    @ColumnInfo(name = "receipt_message")
    var message: String,

    @ColumnInfo(name = "receipt_code")
    var code: String? = null,

    @ColumnInfo(name = "transaction_type")
    var transactionType: String? = null, //sentToNumber, sentBuyGoods, sentToPayBill, sentToMshwari, receivedMoney

    var date: Long? = null,//TODO: Look for valid type for dates and time

    @ColumnInfo(name = "account_balance")
    var balance: Double? = null,

    @ColumnInfo(name = "amount_sent")
    var amountSent: Double? = null,

    @ColumnInfo(name = "amount_received")
    var amountReceived: Double? = null,

    @ColumnInfo(name="transaction_cost")
    var transactionCost: Double? = null,
    var phoneNumber: String? = null,
    var businessName: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var receiptId: Long = 0L
}

@Entity(foreignKeys = [ForeignKey(
        entity = Person::class,
        parentColumns = arrayOf("phoneNumber"),
        childColumns = arrayOf("phoneNumber"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Person(
    @PrimaryKey()
    var phoneNumber: String,
    @ColumnInfo(name="name")
    var name: String,
    @ColumnInfo(name="target_expenditure")
    var targetExpense:Double? = null
)

@Entity(foreignKeys =[ForeignKey(
    entity = Business::class,
    parentColumns = arrayOf("businessName"),
    childColumns = arrayOf("businessName"),
    onDelete = ForeignKey.CASCADE
)])
data class Business(
    @PrimaryKey()
    var businessName: String, //For pabill, its the paybill concatenated with the account no.
    @ColumnInfo(name="target_expenditure")
    var targetExpense:Double? = null
)

@Entity()
data class Target(
    val phoneNumber: String,
    val targetExpense: Double,
    val currentExpenditure:Double,
    val status:Boolean,//If true then it is active else, false
    val dateSet:Long,
    val durationStart: Long, //time in milliseconds
    val durationEnd: Long//time in milliseconds

){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}

data class PersonWithTargets(
    @Embedded val user: Person,
    @Relation(
        parentColumn = "phoneNumber",
        entityColumn = "phoneNumber"
    )
    val targets: List<Target>
)

data class PersonWithReceipts(
    @Embedded val user: Person,
    @Relation(
        parentColumn = "phoneNumber",
        entityColumn = "personPhoneNumber"
    )
    val receipts: List<Receipt>
)

data class BusinessWithReceipts(
    @Embedded val business:Business,
    @Relation(
        parentColumn = "businessName",
        entityColumn = "businessName"
    )
    val receipts: List<Receipt>
)
data class PersonReceipt(val person: Person, val receipt: Receipt)
data class BusinessReceipt(val business:Business, val receipt: Receipt)




