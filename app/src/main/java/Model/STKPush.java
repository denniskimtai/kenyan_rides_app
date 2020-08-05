package Model;

import com.google.gson.annotations.SerializedName;

public class STKPush {

    @SerializedName("BusinessShortCode")
    public String businessShortCode;

    @SerializedName("Password")
    public String password;

    @SerializedName("Timestamp")

    public String timestamp;
    @SerializedName("TransactionType")

    public String transactionType;
    @SerializedName("Amount")

    public String amount;
    @SerializedName("PartyA")

    public String partyA;
    @SerializedName("PartyB")

    public String partyB;
    @SerializedName("PhoneNumber")

    public String phoneNumber;
    @SerializedName("CallBackURL")

    public String callBackURL;
    @SerializedName("AccountReference")

    public String accountReference;
    @SerializedName("TransactionDesc")

    public String transactionDesc;


public STKPush(String businessShortCode, String password, String timestamp, String transactionType, String amount,
               String partyA, String partyB, String phoneNumber, String callBackURL, String accountReference, String transactionDesc) {

        this.businessShortCode = businessShortCode;
        this.password = password;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.amount = amount;
        this.partyA = partyA;
        this.partyB = partyB;
        this.phoneNumber = phoneNumber;
        this.callBackURL = callBackURL;
        this.accountReference = accountReference;
        this.transactionDesc = transactionDesc;
    }

}
