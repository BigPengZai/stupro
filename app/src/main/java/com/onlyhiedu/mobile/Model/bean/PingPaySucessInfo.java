package com.onlyhiedu.mobile.Model.bean;

/**
 * Created by pengpeng on 2017/7/27.
 */

public class PingPaySucessInfo {


    /**
     * id : ch_aPmfb54Gy90KCKeXTK4Cm9KO
     * object : charge
     * created : 1500983220
     * livemode : false
     * paid : false
     * refunded : false
     * reversed : false
     * app : app_qPmjfP5mT0y5rDy5
     * channel : wx
     * orderNo : 20170725194442100001
     * clientIp : 192.168.1.182
     * amount : 1408000
     * amountSettle : 1408000
     * currency : cny
     * subject : 小学80课时
     * body : 小学80课时
     * timePaid : null
     * timeExpire : 1500990420
     * timeSettle : null
     * transactionNo : null
     * refunds : {"object":"list","url":"/v1/charges/ch_aPmfb54Gy90KCKeXTK4Cm9KO/refunds","hasMore":false,"data":[]}
     * amountRefunded : 0
     * failureCode : null
     * failureMsg : null
     * metadata : {}
     * credential : {"object":"credential","wx":{"appId":"wxcomtedkijhg89e1w","partnerId":"1278859101","prepayId":"11010000001707251sernlm18u9kdezd","nonceStr":"137e181af0c50fdd8ed48451d08294a2","timeStamp":"1500983220","packageValue":"Sign=WXPay","sign":"ad3f18331440b36ed7cad9be36bbabe6f7dba9ed"}}
     * extra : {"limit_pay":"no_credit"}
     * description : null
     */

    private String id;
    private String object;
    private int created;
    private boolean livemode;
    private boolean paid;
    private boolean refunded;
    private boolean reversed;
    private String app;
    private String channel;
    private String orderNo;
    private String clientIp;
    private int amount;
    private int amountSettle;
    private String currency;
    private String subject;
    private String body;
    private Object timePaid;
    private int timeExpire;
    private Object timeSettle;
    private Object transactionNo;
    private RefundsBean refunds;
    private int amountRefunded;
    private Object failureCode;
    private Object failureMsg;
    private MetadataBean metadata;
    private CredentialBean credential;
    private ExtraBean extra;
    private Object description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountSettle() {
        return amountSettle;
    }

    public void setAmountSettle(int amountSettle) {
        this.amountSettle = amountSettle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getTimePaid() {
        return timePaid;
    }

    public void setTimePaid(Object timePaid) {
        this.timePaid = timePaid;
    }

    public int getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(int timeExpire) {
        this.timeExpire = timeExpire;
    }

    public Object getTimeSettle() {
        return timeSettle;
    }

    public void setTimeSettle(Object timeSettle) {
        this.timeSettle = timeSettle;
    }

    public Object getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(Object transactionNo) {
        this.transactionNo = transactionNo;
    }

    public RefundsBean getRefunds() {
        return refunds;
    }

    public void setRefunds(RefundsBean refunds) {
        this.refunds = refunds;
    }

    public int getAmountRefunded() {
        return amountRefunded;
    }

    public void setAmountRefunded(int amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public Object getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(Object failureCode) {
        this.failureCode = failureCode;
    }

    public Object getFailureMsg() {
        return failureMsg;
    }

    public void setFailureMsg(Object failureMsg) {
        this.failureMsg = failureMsg;
    }

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public CredentialBean getCredential() {
        return credential;
    }

    public void setCredential(CredentialBean credential) {
        this.credential = credential;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public static class RefundsBean {
    }

    public static class MetadataBean {
    }

    public static class CredentialBean {
        /**
         * object : credential
         * wx : {"appId":"wxcomtedkijhg89e1w","partnerId":"1278859101","prepayId":"11010000001707251sernlm18u9kdezd","nonceStr":"137e181af0c50fdd8ed48451d08294a2","timeStamp":"1500983220","packageValue":"Sign=WXPay","sign":"ad3f18331440b36ed7cad9be36bbabe6f7dba9ed"}
         */

        private String object;
        private WxBean wx;

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public WxBean getWx() {
            return wx;
        }

        public void setWx(WxBean wx) {
            this.wx = wx;
        }

        public static class WxBean {
            /**
             * appId : wxcomtedkijhg89e1w
             * partnerId : 1278859101
             * prepayId : 11010000001707251sernlm18u9kdezd
             * nonceStr : 137e181af0c50fdd8ed48451d08294a2
             * timeStamp : 1500983220
             * packageValue : Sign=WXPay
             * sign : ad3f18331440b36ed7cad9be36bbabe6f7dba9ed
             */

            private String appId;
            private String partnerId;
            private String prepayId;
            private String nonceStr;
            private String timeStamp;
            private String packageValue;
            private String sign;

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getPartnerId() {
                return partnerId;
            }

            public void setPartnerId(String partnerId) {
                this.partnerId = partnerId;
            }

            public String getPrepayId() {
                return prepayId;
            }

            public void setPrepayId(String prepayId) {
                this.prepayId = prepayId;
            }

            public String getNonceStr() {
                return nonceStr;
            }

            public void setNonceStr(String nonceStr) {
                this.nonceStr = nonceStr;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public String getPackageValue() {
                return packageValue;
            }

            public void setPackageValue(String packageValue) {
                this.packageValue = packageValue;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
        }
    }

    public static class ExtraBean {
        /**
         * limit_pay : no_credit
         */

        private String limit_pay;

        public String getLimit_pay() {
            return limit_pay;
        }

        public void setLimit_pay(String limit_pay) {
            this.limit_pay = limit_pay;
        }
    }

}
