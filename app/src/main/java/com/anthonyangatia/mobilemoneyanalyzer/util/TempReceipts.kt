package com.anthonyangatia.mobilemoneyanalyzer.util

fun getTempReceipts(): MutableList<String> {
    val receipts: MutableList<String> = mutableListOf("PJV2ZC9GDC Confirmed. Ksh140.00 sent to PETER  NJAU 0769767204 on 31/10/21 at 2:38 PM. New M-PESA balance is Ksh26,797.68. Transaction cost, Ksh6.00. Amount you can transact within the day is 293,155.00. Send KES100 & below to POCHI LA BIASHARA for FREE! To reverse, foward this message to 456.",
    "PKC7JN273D Confirmed. Ksh220.00 sent to MARTIN  MAKHULO 0721775558 on 12/11/21 at 10:51 AM. New M-PESA balance is Ksh4,730.68. Transaction cost, Ksh6.00. Amount you can transact within the day is 299,780.00. Send KES100 & below to POCHI LA BIASHARA for FREE! To reverse, foward this message to 456.",
    "PKL8Y2CRUS Confirmed. Ksh70.00 sent to SERIL  ALUBALA 0790338891 on 21/11/21 at 8:07 AM. New M-PESA balance is Ksh2,154.68. Transaction cost, Ksh0.00. Amount you can transact within the day is 299,930.00. Send KES100 & below to POCHI LA BIASHARA for FREE! To reverse, foward this message to 456.",
    "PJQ5ROOMRV Confirmed. Ksh250.00 sent to Patrick  Juma 0790591668 on 26/10/21 at 7:41 PM. New M-PESA balance is Ksh29,026.68. Transaction cost, Ksh6.00. Amount you can transact within the day is 299,460.00. Send KES100 & below to POCHI LA BIASHARA for FREE! To reverse, foward this message to 456.",
    "PJV7Z8ZDD3 Confirmed. Ksh6,165.00 sent to TINGG CELLULANT  for account Glovo - iv_alFUFenfvJj4 on 31/10/21 at 1:39 PM New M-PESA balance is Ksh26,983.68. Transaction cost, Ksh85.00. Amount you can transact within the day is 293,335.00.",
    "PK3668EV0M Confirmed. Ksh110.00 sent to Safaricom Offers  for account Tunukiwa on 3/11/21 at 11:07 PM New M-PESA balance is Ksh56,337.68. Transaction cost, Ksh0.00. Amount you can transact within the day is 299,730.00.",
    "PJP9Q754FH Confirmed. Ksh20.00 sent to Safaricom Limited for account on 25/10/21 at 8:31 PM. New M-PESA balance is Ksh29,572.68. Transaction cost, Ksh0.00.",
    "PK41692KXV Confirmed. Ksh20.00 sent to Safaricom Limited for account on 4/11/21 at 12:03 AM. New M-PESA balance is Ksh56,317.68. Transaction cost, Ksh0.00.",
    "PK53824S1Z Confirmed. Ksh25,000.00 sent to KCB Paybill AC  for account 1179734017 on 5/11/21 at 7:56 AM New M-PESA balance is Ksh31,167.68. Transaction cost, Ksh0.00. Amount you can transact within the day is 275,000.00.",
    "PK5282C41U Confirmed. Ksh250.00 sent to NCBA Bank Ltd Corporate  for account Mbagathi on 5/11/21 at 8:01 AM New M-PESA balance is Ksh30,894.68. Transaction cost, Ksh23.00. Amount you can transact within the day is 274,750.00.",
    "PK5382JP6D Confirmed. Ksh250.00 sent to NCBA Bank Ltd Corporate  for account Mbagathi Chairs on 5/11/21 at 8:06 AM New M-PESA balance is Ksh30,621.68. Transaction cost, Ksh23.00. Amount you can transact within the day is 274,500.00.",
    "PL96UWFB1C Confirmed. Ksh120.00 paid to ROZZYNE RESTAURANT. on 9/12/21 at 3:17 PM.New M-PESA balance is Ksh5,195.68. Transaction cost, Ksh0.00. Amount you can transact within the day is 299,880.00.You can now access M-PESA via *334#",
    "PKG4Q8PRLM Confirmed.Ksh1,000.00 transferred from M-Shwari account on 16/11/21 at 2:40 PM. M-Shwari balance is Ksh23,565.25 .M-PESA balance is Ksh1,074.68 .Transaction cost Ksh.0.00",
    "PK92G385UU Confirmed.Ksh24,500.00 transferred to M-Shwari account on 9/11/21 at 10:25 PM. M-PESA balance is Ksh5,336.68 .New M-Shwari saving account balance is Ksh24,560.47. Transaction cost Ksh.0.00",
    "PK98G2WRHO Confirmed. Your account balance was: M-PESA Account : Ksh29,836.68 Business Account : Ksh0.00 on 9/11/21 at 10:14 PM. Transaction cost, Ksh0.00. Dial*334# NOW to get your M-PESA mini or full statements.",
    "PK98G39GTA Confirmed. Your account balance was: M-PESA Account : Ksh5,336.68 Business Account : Ksh0.00 on 9/11/21 at 10:27 PM. Transaction cost, Ksh0.00. Dial*334# NOW to get your M-PESA mini or full statements.",
    "PKB2IGBF1G confirmed.You bought Ksh100.00 of airtime on 11/11/21 at 3:14 PM.New M-PESA balance is Ksh4,956.68. Transaction cost, Ksh0.00. Amount you can transact within the day is 299,780.00. Dial *106# to check numbers registered under your ID.",
    "PGG2E89RLI confirmed.You bought Ksh1000.00 of airtime on 16/7/21 at 4:21 AM.New M-PESA balance is Ksh4,620.33. Transaction cost, Ksh100.00. Amount you can transact within the day is 299,900.00. Download M-PESA app on http://bit.ly/mpesappsm & get 500MB FREE data . To reverse, forward this message to 456.",
    "PK283UGE6Y Confirmed.You have received Ksh30,000.00 from ALOICE  OLOO 0719249474 on 2/11/21 at 4:51 PM  New M-PESA balance is Ksh56,657.68.Download M-PESA app on http://bit.ly/mpesappsm & get 500MB.",
    "PK283UGE6Y Confirmed.You have received Ksh300.00 from ALOICE  OLOO 0719249474 on 2/11/21 at 4:51 PM  New M-PESA balance is Ksh56,657.68.Download M-PESA app on http://bit.ly/mpesappsm & get 500MB.",
    "PJ63V4R74Z Confirmed. You have received Ksh25,500.00 from Consolidated Bank LTD on 6/10/21 at 5:19 PM. New M-PESA balance is Ksh31,245.68. Separate personal and business funds through Pochi la Biashara on *334#.",
    "PGT8XFXGBK Confirmed.You have received Ksh5,287.00 from DTB Touch 24 7 Account 516601 on 29/7/21 at 3:59 PM New M-PESA balance is Ksh5,959.33.  Separate personal and business funds through Pochi la Biashara on *334#.",
    "PK83DGZMY3 Confirmed. Ksh140.00 paid to MAURICE OUMA. on 8/11/21 at 1:00 PM.New M-PESA balance is Ksh29,896.68. Transaction cost, Ksh10.00. Amount you can transact within the day is 299,830.00.You can now access M-PESA via *334#",
    "PK85E5R2FR Confirmed. Ksh60.00 paid to KIRETON 4. on 8/11/21 at 7:15 PM.New M-PESA balance is Ksh29,836.68. Transaction cost, Ksh0.00. Amount you can transact within the day is 299,770.00.You can now access M-PESA via *334#")
    return receipts
}