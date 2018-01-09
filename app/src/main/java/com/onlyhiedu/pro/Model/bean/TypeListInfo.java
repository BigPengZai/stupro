package com.onlyhiedu.pro.Model.bean;

import java.io.Serializable;

/**
 * Created by pengpeng on 2017/7/27.
 */

public class TypeListInfo implements Serializable {
        /**
         * key : 课程优惠
         * value : 常规
         */

        private String key;
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
}
