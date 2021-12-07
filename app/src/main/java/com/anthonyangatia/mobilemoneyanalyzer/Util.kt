package com.anthonyangatia.mobilemoneyanalyzer

import androidx.room.ColumnInfo
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import java.text.SimpleDateFormat


data class AmountTransacted(
    @ColumnInfo(name = "totalSent")var amountSentTotal: Double?,
    @ColumnInfo(name = "totalReceived")var amountReceivedTotal:Double?
)

const val SMS_RECEIVE_ACTION = "android.provider.Telephony.SMS_RECEIVED"
//const val SENT_MONEY_REGEX_STRING = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) (paid|sent) to (?<recipient>.+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
//const val RECEIVED_MONEY_REGEX_STRING = """(?<code>\w+) Confirmed\.*\s*You have received Ksh(?<amountReceived>[\d\.\,]+) from (?<sender>.*) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.*\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\..*"""
//const val ACCOUNT_BALANCE = """(?<code>\w+)\.*\s*Confirmed\.*\s*Your account balance was: M-PESA Account : Ksh(?<mpesaBalance>[\d\.\,]+) Business Account : Ksh(?<businessBalance>[\d\.\,]+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val CREATEFROMPDUFORMAT = "3gpp"

const val SENT_TO_NUMBER = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) sent to (?<recipientName>[A-Z,a-z,\s]+)\s*(?<recipientNo>[0-9]+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val SENT_TO_PAYBILL = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) sent to (?<paybillNumber>[A-Z,a-z,\s]+)for account (?<accountNo>.+|)on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val SENT_TO_MSHWARI = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) transferred to M-Shwari account on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*M-PESA balance is Ksh(?<mpesaBalance>[\d\.\,\s]+)\.\s*New M-Shwari saving account balance is Ksh(?<mshwariBalance>[\d\.\,\s]+)\.\s*Transaction cost Ksh\.(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val  SENT_TO_BUY_GOODS = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) (paid|sent) to (?<recipient>.+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val RECEIVED_FROM_MSHWARI = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) transferred from M-Shwari account on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*M-Shwari balance is Ksh(?<mshwariBalance>[\d\.\,\s]+)\.\s*M-PESA balance is Ksh(?<mpesaBalance>[\d\.\,\s]+)\.\s*Transaction cost Ksh\.(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val ACCOUNT_BALANCE = """(?<code>\w+)\.*\s*Confirmed\.*\s*Your account balance was: M-PESA Account : Ksh(?<mpesaBalance>[\d\.\,]+) Business Account : Ksh(?<businessBalance>[\d\.\,]+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val SENT_TO_BUY_AIRTIME = """(?<code>\w+) Confirmed\.*\s*You bought Ksh(?<amountSent>[\d\.\,]+) of airtime on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.*\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\..*"""
const val RECEIVED_MONEY = """(?<code>\w+) Confirmed\.*\s*You have received Ksh(?<amountReceived>[\d\.\,]+) from (?<sender>.*) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.*\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\..*"""

fun buildReceiptFromSms(message:String): Receipt {
    val sentToNumber = SENT_TO_NUMBER.toRegex()
    val sentToPayBill = SENT_TO_PAYBILL.toRegex()
    val sentToMshwari = SENT_TO_MSHWARI.toRegex()
    val sentToBuyGoods = SENT_TO_BUY_GOODS.toRegex()
    val sentToBuyAirTime = SENT_TO_BUY_AIRTIME.toRegex()
    val receivedMoney = RECEIVED_MONEY.toRegex()
    val receivedMoneyMshwari = RECEIVED_FROM_MSHWARI.toRegex()
    val accountBalanceRegex = ACCOUNT_BALANCE.toRegex()
    when(true){
        sentToNumber.matches(message)->{
            val matchResult = sentToNumber.matchEntire(message)
            var (code, amountSent,recipientName, recipientNumber, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val receipt =  Receipt(0L,message, code, recipientName = recipientName, recipientNumber= recipientNumber.toLong(),
                null,null, transactionType ="sentToNumber",
                date = convertDateToLong(date+" "+time), time = time, balance =convertToDouble(balance),
                amountSent = convertToDouble(amountSent), null,transactionCost = convertToDouble(transactionCost))

            return receipt
        }
        sentToPayBill.matches(message)->{
            val matchResult = sentToPayBill.matchEntire(message)
            var (code, amountSent, paybill,  account, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val receipt =  Receipt(0L,message, code, recipientName = paybill, null,
                account = account, sender =null, transactionType = "sentToPayBill",
                date = convertDateToLong(date+" "+time), time = time, balance = convertToDouble(balance),
                amountSent = convertToDouble(amountSent), amountReceived = null,transactionCost = convertToDouble(transactionCost))
            return receipt
        }
        sentToMshwari.matches(message)->{
            val matchResult = sentToMshwari.matchEntire(message)
            var (code, amountSent, date, time, mpesaBalance, mshwariBalance,  transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val receipt =  Receipt(0L,message, code, recipientName = "MSHWARI", null,
                account = null, sender =null, transactionType = "sentToMshwari",
                date = convertDateToLong(date+" "+time), time = time, balance = convertToDouble(mpesaBalance),
                amountSent = convertToDouble(amountSent), amountReceived = null,transactionCost = convertToDouble(transactionCost))
            return receipt
        }
        sentToBuyGoods.matches(message)->{
            val matchResult = sentToBuyGoods.matchEntire(message)
            var (code, amountSent, recipientName, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val receipt =  Receipt(0L,message, code, recipientName = recipientName, null,
                account = null, sender =null, transactionType = "sentBuyGoods",
                date = convertDateToLong(date+" "+time), time = time, balance = convertToDouble(balance),
                amountSent = convertToDouble(amountSent), amountReceived = null,transactionCost = convertToDouble(transactionCost))
            return receipt
        }
        receivedMoney.matches(message)->{
            val matchResult = receivedMoney.matchEntire(message)
            var (code, amountReceived, sender, date, time, balance) = matchResult!!.destructured
            time = formatTime(time)
            val receipt =  Receipt(0L,message, code, recipientName = null, null,
                account = null, sender =sender, transactionType = "sentBuyGoods",
                date = convertDateToLong(date+" "+time), time = time, balance = convertToDouble(balance),
                amountSent = null, amountReceived = convertToDouble(amountReceived))
            return receipt
        }
        receivedMoneyMshwari.matches(message)->{
            val matchResult = receivedMoneyMshwari.matchEntire(message)
            var (code, amountReceived, date, time, balance) = matchResult!!.destructured
            time = formatTime(time)
            val receipt =  Receipt(0L,message, code, recipientName = null, null,
                account = null, sender ="MSHWARI", transactionType = "sentBuyGoods",
                date = convertDateToLong(date+" "+time), time = time, balance = convertToDouble(balance),
                amountSent = null, amountReceived = convertToDouble(amountReceived))
            return receipt
        }
        accountBalanceRegex.matches(message)->{
            val matchResult = accountBalanceRegex.matchEntire(message)
            var (code, mpesaBalance, businessBalance, date, time, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val receipt = Receipt(0L,message, code,
                recipientName = null, null,
                balance = convertToDouble(mpesaBalance), transactionCost = convertToDouble(transactionCost))
            return receipt
        }
        sentToBuyAirTime.matches(message)->{
            val matchResult = sentToBuyAirTime.matchEntire(message)
            var (code, amountSent, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val receipt =  Receipt(0L,message, code, recipientName = "AIRTIME", null,
                account = null, sender =null, transactionType = "sentBuyGoods",
                date = convertDateToLong(date+" "+time), time = time, balance = convertToDouble(balance),
                amountSent = convertToDouble(amountSent), amountReceived = null,transactionCost = convertToDouble(transactionCost))
            return receipt
        }

    }
    return Receipt()
}

private fun formatTime(date: String): String {
    var newstring = date.replace("AM", "a.m.")
    if(newstring.equals(date)){
        var newstring = date.replace("PM", "p.m.")
        return newstring
    }
    return newstring
}

fun convertToDouble(value: String):Double{
    return value.replace(",", "").toDouble()
}


fun convertDateToLong(dateString: String): Long {
    val dateFormat = SimpleDateFormat("dd/MM/yy hh:mm aa")
    val date = dateFormat.parse(dateString)
    return date.time
}