package com.anthonyangatia.mobilemoneyanalyzer.database

import androidx.room.*

@Entity(tableName = "transaction_receipt_table")
data class Receipt(
    @ColumnInfo(name = "receipt_message")
    var message: String,

    @ColumnInfo(name = "receipt_code")
    var code: String? = null,

    @ColumnInfo(name = "transaction_type")
    var transactionType: String? = null, //sentToNumber, sentBuyGoods, sentToPayBill, sentToMshwari, receivedMoney, accountBalance

    var category: String? = null,

    var date: Long? = null,//TODO: Look for valid type for dates and time

    @ColumnInfo(name = "account_balance")
    var balance: Double? = null,

    @ColumnInfo(name = "amount_sent")
    var amountSent: Double? = null,

    @ColumnInfo(name = "amount_received")
    var amountReceived: Double? = null,

    @ColumnInfo(name="transaction_cost")
    var transactionCost: Double? = null,
    var name: String? = null
){
    @PrimaryKey(autoGenerate = true)
    var receiptId: Long = 0L
}

@Entity(foreignKeys = [ForeignKey(
        entity = PersonAndBusiness::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("name"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class PersonAndBusiness(
    @PrimaryKey()
    var name: String, //person name or buy goods name or paybill name
    var phoneNumber: String? = null, //for person
    @ColumnInfo(name="target_expenditure")
    var targetExpense:Double? = null
)

@Entity()
data class Target(
    val name: String,//Identifier of person or business
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
    @Embedded val user: PersonAndBusiness,
    @Relation(
        parentColumn = "name",
        entityColumn = "name"
    )
    val targets: List<Target>
)

data class PersonWithReceipts(
    @Embedded val user: PersonAndBusiness,
    @Relation(
        parentColumn = "name",
        entityColumn = "name"
    )
    val receipts: List<Receipt>
)

data class PersonReceipt(val personAndBusiness: PersonAndBusiness, val receipt: Receipt)

data class TransactionSummary(val transactionType:String, val amount: Double)




