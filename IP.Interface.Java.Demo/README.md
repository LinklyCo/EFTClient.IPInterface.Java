# Introduction - IP Interface Java Demo POS

![TestPos](README_PICS\testpos.PNG)

The IP Interface Java Demo POS is a simple test POS that uses the functionality of the IP.Interface.Java library to perform
transactions over a TCP/IP connection (SSL and Non-SSL).

##AsyncMainWindow

The Async Main window class uses the AsyncSocket & AsyncSSLSocket Controls.

The AsyncMainWindow POS is set up to run the following requests:
- Logon
- Transaction
- Status
- Settlement
- Get Last Transaction
- Reprint Receipt
- Set Dialog
- Display Request
- Generic POS Command
- Display Control Panel
- Query Card
- Get Client List
- Cheque Auth
- Configure Merchant
- Cloud Pair
- Cloud Logon

##MainWindow
<b>*No Longer Supporting*</b>

The Main window class uses the old socket control (PCEFTPOSControl). This control is unable to perform SSL Connections and
has been dropped in favour of AsyncSocketControl & AsyncSSLSocketControl.

The MainWindow POS is set up to run the following requests:
- Logon
- Transaction
- Status
- Settlement
- Get Last Transaction
- Reprint Receipt
- Set Dialog
# Contact Us 

<b>Send an email to devsupport@pceftpos.com.au for:</b>
- Enquiries into developing with the Java Interface
- Reporting any bugs/issues found when using the interface

# Release Notes
