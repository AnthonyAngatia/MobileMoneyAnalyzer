package com.anthonyangatia.mobilemoneyanalyzer.util

import androidx.room.ColumnInfo
import com.anthonyangatia.mobilemoneyanalyzer.database.PersonAndBusiness
import com.anthonyangatia.mobilemoneyanalyzer.database.Receipt
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


data class AmountTransacted(
    @ColumnInfo(name = "totalSent")var amountSentTotal: Double?,
    @ColumnInfo(name = "totalReceived")var amountReceivedTotal:Double?
)

data class PersonAmountTransacted(var personAndBusiness: PersonAndBusiness, var amountTransacted: AmountTransacted)
val transactionTypeArray = arrayOf<String>("sentToNumber", "sentToBuyGoods","sentToPayBill","sentToMshwari", "receivedMoney", "receivedMoneyMshwari", "sentToBuyAirTime")

data class CategoryAmount(var category :String, var amount: Double)
// Key for the string that's delivered in the action's intent.
val KEY_TEXT_REPLY = "key_text_reply"

fun getTodaysDate(): Int {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val date = Date()
    println(formatter.format(date))
    val dateRegex = """(\d{1,3})\/(\d{1,3})\/(\d{1,4})\s\d+:\d+:\d+""".toRegex()
//        val matchResult = dateRegex.matchEntire("10/12/2021 23:11:32")//For debug purposes
    val matchResult = dateRegex.matchEntire(formatter.format(date))
    val (dateR) = matchResult!!.destructured
    return dateR.toInt()
}

fun getMinMaxTimeDay(day:Int):Pair<Long, Long>{
    val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH) + 1
    //For debugging purposes
//        val month = 12
//        val year = 2021
    val year = calendar.get(Calendar.YEAR)
    val lastTimeInADay = "23:59:59"
    val firstTimeInADay = "00:00:00"
    var minimumTime = "$day/$month/$year $firstTimeInADay"
    var maximumTime = "$day/$month/$year $lastTimeInADay"
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val minTimeMilli = convertDateToLong(minimumTime, dateFormat )
    val maxTimeMilli = convertDateToLong(maximumTime, dateFormat)

    return Pair(minTimeMilli, maxTimeMilli)

}

fun getMinMaxTimeWeek(day:Int, firstDateOfWeek:Int):Pair<Long, Long>{
    val calendar = Calendar.getInstance()
    val month = calendar.get(Calendar.MONTH) + 1
    //For debugging purposes
//        val month = 12
//        val year = 2021
    val year = calendar.get(Calendar.YEAR)
    val lastTimeInADay = "23:59:59"
    val firstTimeInADay = "00:00:00"
    var minimumTime = "$firstDateOfWeek/$month/$year $firstTimeInADay"
    var maximumTime = "$day/$month/$year $lastTimeInADay"
    Timber.i("Min Time String"+minimumTime)
    Timber.i("Max Time String"+maximumTime)

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val minTimeMilli = convertDateToLong(minimumTime, dateFormat )
    val maxTimeMilli = convertDateToLong(maximumTime, dateFormat)

    return Pair(minTimeMilli, maxTimeMilli)

}



fun getFirstDayOfMonth(calendar: Calendar): Long{
    calendar.set(
        Calendar.DAY_OF_MONTH,
        calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.timeInMillis

}

fun getLastDayOfMonth(calendar: Calendar):Long{
    // set day to maximum
    calendar.set(
        Calendar.DAY_OF_MONTH,
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.timeInMillis

}


val daysOfWeek = arrayOf<String>("SUN", "MON","TUE", "WED", "THU", "FRI", "SAT")
val months = arrayOf<String>("January", "February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December")


const val SMS_RECEIVE_ACTION = "android.provider.Telephony.SMS_RECEIVED"
//const val SENT_MONEY_REGEX_STRING = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) (paid|sent) to (?<recipient>.+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
//const val RECEIVED_MONEY_REGEX_STRING = """(?<code>\w+) Confirmed\.*\s*You have received Ksh(?<amountReceived>[\d\.\,]+) from (?<sender>.*) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.*\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\..*"""
//const val ACCOUNT_BALANCE = """(?<code>\w+)\.*\s*Confirmed\.*\s*Your account balance was: M-PESA Account : Ksh(?<mpesaBalance>[\d\.\,]+) Business Account : Ksh(?<businessBalance>[\d\.\,]+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val CREATEFROMPDUFORMAT = "3gpp"

const val SENT_TO_NUMBER = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) sent to (?<recipientName>[A-Z,a-z,\s]+)\s*(?<recipientNo>[0-9]+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val SENT_TO_PAYBILL = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) sent to (?<paybillNumber>[A-Z,a-z,\s]+)for account (?<accountNo>.+|)on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val SENT_TO_MSHWARI = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) transferred to M-Shwari account on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*M-PESA balance is Ksh(?<mpesaBalance>[\d\.\,\s]+)\.\s*New M-Shwari saving account balance is Ksh(?<mshwariBalance>[\d\.\,\s]+)\.\s*Transaction cost Ksh\.(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val  SENT_TO_BUY_GOODS = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) paid to (?<recipient>.+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val RECEIVED_FROM_MSHWARI = """(?<code>\w+)\.*\s*Confirmed\.*\s*Ksh(?<amountSent>[\d\.\,]+) transferred from M-Shwari account on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2}).\s*M-Shwari balance is Ksh(?<mshwariBalance>[\d\.\,\s]+)\.\s*M-PESA balance is Ksh(?<mpesaBalance>[\d\.\,\s]+)\.\s*Transaction cost Ksh\.(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val ACCOUNT_BALANCE = """(?<code>\w+)\.*\s*Confirmed\.*\s*Your account balance was: M-PESA Account : Ksh(?<mpesaBalance>[\d\.\,]+) Business Account : Ksh(?<businessBalance>[\d\.\,]+) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\.\s*.*"""
const val SENT_TO_BUY_AIRTIME = """(?<code>\w+) Confirmed\.*\s*You bought Ksh(?<amountSent>[\d\.\,]+) of airtime on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.*\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\.\s*Transaction cost, Ksh(?<transactionCost>[\d\.\,]+)\..*"""
const val RECEIVED_MONEY = """(?<code>\w+) Confirmed\.*\s*You have received Ksh(?<amountReceived>[\d\.\,]+) from (?<sender>.*) on (?<date>\d{1,2}\/\d{1,2}\/\d{2}) at (?<time>\d{1,2}:\d{2} \w{2})\.*\s*New M-PESA balance is Ksh(?<balance>[\d\.\,]+)\..*"""

fun buildReceiptFromSms(message:String): Pair<Receipt?, PersonAndBusiness?> {
    val sentToNumber = SENT_TO_NUMBER.toRegex()
    val sentToPayBill = SENT_TO_PAYBILL.toRegex()
    val sentToMshwari = SENT_TO_MSHWARI.toRegex()
    val sentToBuyGoods = SENT_TO_BUY_GOODS.toRegex()
    val sentToBuyAirTime = SENT_TO_BUY_AIRTIME.toRegex()
    val receivedMoney = RECEIVED_MONEY.toRegex()
    val receivedMoneyMshwari = RECEIVED_FROM_MSHWARI.toRegex()
    val accountBalance = ACCOUNT_BALANCE.toRegex()
    when(true){
//        sentToPayBill always has to be befor sentToNumber
        sentToPayBill.matches(message)->{
            val matchResult = sentToPayBill.matchEntire(message)
            var (code, amountSent, paybill,  account, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val businessName = paybill
//            val businessName = paybill + "*" + account
            val person = PersonAndBusiness(businessName) //Insert person
            val receipt =  Receipt(message, code, transactionType = "sentToPayBill",
                date = convertDateToLong(date+" "+time), balance = convertToDouble(balance),
                amountSent = convertToDouble(amountSent),transactionCost = convertToDouble(transactionCost), name = person.name)
            return Pair(receipt, person )
        }
        sentToNumber.matches(message)->{
            val matchResult = sentToNumber.matchEntire(message)
            var (code, amountSent,recipientName, recipientNumber, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val person = PersonAndBusiness(recipientName,recipientNumber) //Insert person
            val receipt =  Receipt(message, code, transactionType ="sentToNumber",
                date = convertDateToLong(date+" "+time), balance =convertToDouble(balance),
                amountSent = convertToDouble(amountSent),transactionCost = convertToDouble(transactionCost), name= person.name)


            return Pair(receipt, person)
        }
        sentToBuyGoods.matches(message)->{
            val matchResult = sentToBuyGoods.matchEntire(message)
            var (code, amountSent, recipientName, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val person = PersonAndBusiness(recipientName) //Insert person
            val receipt =  Receipt(message, code, transactionType = "sentToBuyGoods", date = convertDateToLong(date+" "+time), balance = convertToDouble(balance),
                amountSent = convertToDouble(amountSent),transactionCost = convertToDouble(transactionCost),name = person.name)
            return Pair(receipt,person)
        }
        sentToMshwari.matches(message)->{
            val matchResult = sentToMshwari.matchEntire(message)
            var (code, amountSent, date, time, mpesaBalance, mshwariBalance,  transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val person = PersonAndBusiness("MSHWARI")
            val receipt =  Receipt(message, code, transactionType = "sentToMshwari",
                date = convertDateToLong(date+" "+time),balance = convertToDouble(mpesaBalance),
                amountSent = convertToDouble(amountSent), transactionCost = convertToDouble(transactionCost), name = person.name)
            return Pair(receipt,person)
        }
        receivedMoney.matches(message)->{
            val matchResult = receivedMoney.matchEntire(message)
            var (code, amountReceived, sender, date, time, balance) = matchResult!!.destructured
            time = formatTime(time)
            val person = PersonAndBusiness(sender) // e.g ALOICE  OLOO 0719249474, CITIBANK N.A KENYA 100200
            val receipt =  Receipt(message, code,transactionType = "receivedMoney",
            date = convertDateToLong(date+" "+time), balance = convertToDouble(balance),
                amountReceived = convertToDouble(amountReceived), name = person.name)
            return Pair(receipt, person)
        }
        receivedMoneyMshwari.matches(message)->{
            val matchResult = receivedMoneyMshwari.matchEntire(message)
            var (code, amountReceived, date, time, balance) = matchResult!!.destructured
            time = formatTime(time)
            val person = PersonAndBusiness("MSHWARI")
            val receipt =  Receipt(message, code,transactionType = "receivedMoneyMshwari",
                date = convertDateToLong(date+" "+time), balance = convertToDouble(balance),
                amountReceived = convertToDouble(amountReceived), name = person.name)
            return Pair(receipt, person)
        }
        accountBalance.matches(message)->{
            val matchResult = accountBalance.matchEntire(message)
            var (code, mpesaBalance, businessBalance, date, time, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val receipt = Receipt(message, code, transactionType = "accountBalance",
                balance = convertToDouble(mpesaBalance), transactionCost = convertToDouble(transactionCost),
                date = convertDateToLong(date+" "+time))
            return Pair(receipt,null)
        }
        sentToBuyAirTime.matches(message)->{
            val matchResult = sentToBuyAirTime.matchEntire(message)
            var (code, amountSent, date, time, balance, transactionCost) = matchResult!!.destructured
            time = formatTime(time)
            val person = PersonAndBusiness("AIRTIME")
            val receipt =  Receipt(message, code, transactionType = "sentToBuyAirTime",
                date = convertDateToLong(date+" "+time),balance = convertToDouble(balance),
                amountSent = convertToDouble(amountSent),transactionCost = convertToDouble(transactionCost), name = person.name)
            return Pair(receipt, person)
        }

    }
    return Pair(null, null)
}

private fun formatTime(date: String): String {
    var newstring = date.replace("AM", "a.m.")
    if(newstring.equals(date)){
        var newstring = date.replace("PM", "p.m.")
//        return newstring
    }
    return date //Rreturn type for android 11
}

fun convertToDouble(value: String):Double{
    return value.replace(",", "").toDouble()
}


fun convertDateToLong(dateString: String): Long {
    val dateFormat = SimpleDateFormat("dd/MM/yy hh:mm aa")
    val date = dateFormat.parse(dateString)
    return date.time
}
fun convertDateToLong(dateString: String, dateFormat: SimpleDateFormat): Long {
    val date = dateFormat.parse(dateString)
    return date.time
}