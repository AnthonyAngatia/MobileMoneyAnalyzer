package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.*
import java.sql.Date

@Entity(tableName = "transaction_receipt_table")
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    var receiptId: Long = 0L,

    @ColumnInfo(name = "receipt_message")
    var message: String,

    @ColumnInfo(name = "receipt_code")
    var code: String? = null,

    @ColumnInfo(name = "transaction_type")
    var transactionType: String? = null,

    var date: Long? = null,//TODO: Look for valid type for dates and time

    @ColumnInfo(name = "account_balance")
    var balance: Double? = null,

    @ColumnInfo(name = "amount_sent")
    var amountSent: Double? = null,

    @ColumnInfo(name = "amount_received")
    var amountReceived: Double? = null,

    @ColumnInfo(name="transaction_cost")
    var transactionCost: Double? = null,
    var personPhoneNumber: String? = null,
    var businessName: String? = null
)


data class Person(
    @PrimaryKey()
    var phoneNumber: String,
    @ColumnInfo(name="name")
    var name: String,
    @ColumnInfo(name="target_expenditure")
    var targetExpense:Double? = null
)

data class Business(
    @PrimaryKey()
    var businessName: String, //For pabill, its the paybill concatenated with the account no.
    @ColumnInfo(name="target_expenditure")
    var targetExpense:Double? = null
)

@Entity(

    foreignKeys = [ForeignKey(
        entity = Person::class,
        parentColumns = arrayOf("phoneNumber"),
        childColumns = arrayOf("personPhoneNumber"),
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Business::class,
            parentColumns = arrayOf("businessName"),
            childColumns = arrayOf("businessName"),
            onDelete = ForeignKey.CASCADE
        )]
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




