package com.isuo.yw2application.view.main.work.pay;

public class AlPaySuccessCallBack {

    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2018050302625061","auth_app_id":"2018050302625061","charset":"UTF-8","timestamp":"2018-11-19 22:30:54","out_trade_no":"BA019E78-64B2-4BC7-9AC8-D4E80BC69437","total_amount":"0.02","trade_no":"2018111922001402021009683940","seller_id":"2088131002839023"}
     * sign : RSXVrlCVgof2XXWqwDWfiP+SSYWZpfGT+HALJ4gP6NqlLA9WKfNubk1OFeyExrIW9FX06IBajDWtavgevG9ZGawICJXHtEP20Nx6UCJYu1WITuTJlo0gandoi2YAjSOgoJre4oso62WN9j5uHoN54uUj7HW++/yA4aMybKJyEuWPnhselIIy+3t5GeU+wyP99fZ2pmZZYXlZA17WrV6dZMMp8kmm/vzIrnRhpE3McKqBCv4W8lWmAwJ9faHv8V1/LlrDmajYEKpcQq5wuAAM8lmd8JRldsS2EStPPfRXvxZ5SMF8D/mroNvYhG6scW2hTk9zCXJd5FDWP/mRDrcb5A==
     * sign_type : RSA2
     */

    private AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;
    private String sign;
    private String sign_type;

    public AlipayTradeAppPayResponseBean getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(AlipayTradeAppPayResponseBean alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class AlipayTradeAppPayResponseBean {
        /**
         * code : 10000
         * msg : Success
         * app_id : 2018050302625061
         * auth_app_id : 2018050302625061
         * charset : UTF-8
         * timestamp : 2018-11-19 22:30:54
         * out_trade_no : BA019E78-64B2-4BC7-9AC8-D4E80BC69437
         * total_amount : 0.02
         * trade_no : 2018111922001402021009683940
         * seller_id : 2088131002839023
         */

        private String code;
        private String msg;
        private String app_id;
        private String auth_app_id;
        private String charset;
        private String timestamp;
        private String out_trade_no;
        private String total_amount;
        private String trade_no;
        private String seller_id;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getAuth_app_id() {
            return auth_app_id;
        }

        public void setAuth_app_id(String auth_app_id) {
            this.auth_app_id = auth_app_id;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }
    }
}
