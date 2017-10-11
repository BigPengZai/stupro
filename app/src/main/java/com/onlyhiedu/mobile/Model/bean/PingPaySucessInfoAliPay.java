package com.onlyhiedu.mobile.Model.bean;

import java.util.List;

/**
 * Created by pengpeng on 2017/7/27.
 * 支付宝
 */

public class PingPaySucessInfoAliPay {


    /**
     * amount : 720000
     * amountRefunded : 0
     * amountSettle : 720000
     * app : app_jznfLKLenTePbXT8
     * body : 小学40课时
     * channel : alipay
     * clientIp : 116.226.32.140
     * created : 1507702216
     * credential : {"alipay":{"orderInfo":"app_id=2017071007707270&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA&timestamp=2017-10-11%2014%3A10%3A16&version=1.0&biz_content=%7B%22body%22%3A%22%E5%B0%8F%E5%AD%A640%E8%AF%BE%E6%97%B6%22%2C%22subject%22%3A%22%E5%B0%8F%E5%AD%A640%E8%AF%BE%E6%97%B6%22%2C%22out_trade_no%22%3A%2220171011141016100040%22%2C%22total_amount%22%3A7200%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%221440m%22%7D&notify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_SeHCaLizvH84uH0eHOGG44mH&sign=Aui9TOjIdda3iLV7NjEmGruIwqmqoWn6zFnc1eOpMuo80tMD4CPxKG3ooIa2CqxKe2UFTeSg%2Fh%2Fq%2FKxkh4%2BkdxPGDXlCDuWwi6nHQH%2BPSZ5OhcAvOTGQK69T4Q5JDSZ0RX2%2B6A8SvJeMuJXR9V5OVBrj03oTDN0HF%2FJ8Ab6I%2BiD%2BfnK%2BuyPUQRst3A3dwZ2ccqx6iIG0ZRB8kD6Ib2NQu2jJFp2tcNYoBkcHaID645R3vUqlj%2B1mR6bwla31CeWO3ZbV2ib9Xgird%2BW67pS6q2PqF2upCNa%2BXgJkg67DJ4Hnr1sF6Zbqt6B2kULkx78kzoO8IN%2FfL7auYSlEsJUwKg%3D%3D"},"object":"credential"}
     * currency : cny
     * extra : {"rn_check":"T"}
     * id : ch_SeHCaLizvH84uH0eHOGG44mH
     * livemode : true
     * metadata : {}
     * object : charge
     * orderNo : 20171011141016100040
     * paid : false
     * refunded : false
     * refunds : {"data":[],"hasMore":false,"object":"list","url":"/v1/charges/ch_SeHCaLizvH84uH0eHOGG44mH/refunds"}
     * reversed : false
     * subject : 小学40课时
     * timeExpire : 1507788616
     */

    private int amount;
    private int amountRefunded;
    private int amountSettle;
    private String app;
    private String body;
    private String channel;
    private String clientIp;
    private int created;
    private CredentialBean credential;
    private String currency;
    private ExtraBean extra;
    private String id;
    private boolean livemode;
    private MetadataBean metadata;
    private String object;
    private String orderNo;
    private boolean paid;
    private boolean refunded;
    private RefundsBean refunds;
    private boolean reversed;
    private String subject;
    private int timeExpire;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountRefunded() {
        return amountRefunded;
    }

    public void setAmountRefunded(int amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public int getAmountSettle() {
        return amountSettle;
    }

    public void setAmountSettle(int amountSettle) {
        this.amountSettle = amountSettle;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public CredentialBean getCredential() {
        return credential;
    }

    public void setCredential(CredentialBean credential) {
        this.credential = credential;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public RefundsBean getRefunds() {
        return refunds;
    }

    public void setRefunds(RefundsBean refunds) {
        this.refunds = refunds;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(int timeExpire) {
        this.timeExpire = timeExpire;
    }

    public static class CredentialBean {
        /**
         * alipay : {"orderInfo":"app_id=2017071007707270&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA&timestamp=2017-10-11%2014%3A10%3A16&version=1.0&biz_content=%7B%22body%22%3A%22%E5%B0%8F%E5%AD%A640%E8%AF%BE%E6%97%B6%22%2C%22subject%22%3A%22%E5%B0%8F%E5%AD%A640%E8%AF%BE%E6%97%B6%22%2C%22out_trade_no%22%3A%2220171011141016100040%22%2C%22total_amount%22%3A7200%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%221440m%22%7D&notify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_SeHCaLizvH84uH0eHOGG44mH&sign=Aui9TOjIdda3iLV7NjEmGruIwqmqoWn6zFnc1eOpMuo80tMD4CPxKG3ooIa2CqxKe2UFTeSg%2Fh%2Fq%2FKxkh4%2BkdxPGDXlCDuWwi6nHQH%2BPSZ5OhcAvOTGQK69T4Q5JDSZ0RX2%2B6A8SvJeMuJXR9V5OVBrj03oTDN0HF%2FJ8Ab6I%2BiD%2BfnK%2BuyPUQRst3A3dwZ2ccqx6iIG0ZRB8kD6Ib2NQu2jJFp2tcNYoBkcHaID645R3vUqlj%2B1mR6bwla31CeWO3ZbV2ib9Xgird%2BW67pS6q2PqF2upCNa%2BXgJkg67DJ4Hnr1sF6Zbqt6B2kULkx78kzoO8IN%2FfL7auYSlEsJUwKg%3D%3D"}
         * object : credential
         */

        private AlipayBean alipay;
        private String object;

        public AlipayBean getAlipay() {
            return alipay;
        }

        public void setAlipay(AlipayBean alipay) {
            this.alipay = alipay;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public static class AlipayBean {
            /**
             * orderInfo : app_id=2017071007707270&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA&timestamp=2017-10-11%2014%3A10%3A16&version=1.0&biz_content=%7B%22body%22%3A%22%E5%B0%8F%E5%AD%A640%E8%AF%BE%E6%97%B6%22%2C%22subject%22%3A%22%E5%B0%8F%E5%AD%A640%E8%AF%BE%E6%97%B6%22%2C%22out_trade_no%22%3A%2220171011141016100040%22%2C%22total_amount%22%3A7200%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%221440m%22%7D&notify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_SeHCaLizvH84uH0eHOGG44mH&sign=Aui9TOjIdda3iLV7NjEmGruIwqmqoWn6zFnc1eOpMuo80tMD4CPxKG3ooIa2CqxKe2UFTeSg%2Fh%2Fq%2FKxkh4%2BkdxPGDXlCDuWwi6nHQH%2BPSZ5OhcAvOTGQK69T4Q5JDSZ0RX2%2B6A8SvJeMuJXR9V5OVBrj03oTDN0HF%2FJ8Ab6I%2BiD%2BfnK%2BuyPUQRst3A3dwZ2ccqx6iIG0ZRB8kD6Ib2NQu2jJFp2tcNYoBkcHaID645R3vUqlj%2B1mR6bwla31CeWO3ZbV2ib9Xgird%2BW67pS6q2PqF2upCNa%2BXgJkg67DJ4Hnr1sF6Zbqt6B2kULkx78kzoO8IN%2FfL7auYSlEsJUwKg%3D%3D
             */

            private String orderInfo;

            public String getOrderInfo() {
                return orderInfo;
            }

            public void setOrderInfo(String orderInfo) {
                this.orderInfo = orderInfo;
            }
        }
    }

    public static class ExtraBean {
    }

    public static class MetadataBean {
    }

    public static class RefundsBean {
        /**
         * data : []
         * hasMore : false
         * object : list
         * url : /v1/charges/ch_SeHCaLizvH84uH0eHOGG44mH/refunds
         */

        private boolean hasMore;
        private String object;
        private String url;
        private List<?> data;

        public boolean isHasMore() {
            return hasMore;
        }

        public void setHasMore(boolean hasMore) {
            this.hasMore = hasMore;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<?> getData() {
            return data;
        }

        public void setData(List<?> data) {
            this.data = data;
        }
    }
}

