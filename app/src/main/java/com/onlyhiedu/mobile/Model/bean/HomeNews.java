package com.onlyhiedu.mobile.Model.bean;

import com.onlyhiedu.mobile.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/6/2.
 */

public class HomeNews {
    public String title;
    public int res;
    public String url;

    public HomeNews() {
    }

    public HomeNews(String title, int res, String url) {
        this.title = title;
        this.res = res;
        this.url = url;
    }

    public static List<HomeNews> datas;

    static {
        datas = new ArrayList<>();
        HomeNews data = new HomeNews("如果想毁掉一个孩子，就给他一部手机!", R.mipmap.news1, "http://mp.weixin.qq.com/s/iUk_y604CTJQHYFSfzXIeg");
        datas.add(data);
        data = new HomeNews("你越骂，孩子越差劲！犯错后只需这8句就够了！", R.mipmap.news2, "http://mp.weixin.qq.com/s/NUdJ-eWpiVuWoQ01SUyJ_g");
        datas.add(data);
        data = new HomeNews("孩子：你现在偷的懒，将来都会变成打脸的巴掌！说的真好！", R.mipmap.news3, "http://mp.weixin.qq.com/s/6-aja1AA-6CJbyQbRpjX2Q");
        datas.add(data);
        data = new HomeNews("竟然惊动了教育部！一位教师父亲给女儿的9点建议（震撼无数中国父母）", R.mipmap.news4, "http://mp.weixin.qq.com/s/glAeaOnEzM54LPDDtaRVMg");
        datas.add(data);
        data = new HomeNews("孩子一回家就让他去写作业？大错特错！", R.mipmap.news5, "http://mp.weixin.qq.com/s/1MgbmG2eepi4bmMj-lCngw");
        datas.add(data);
        data = new HomeNews("你越吼，孩子越差劲！这么做，比你吼一千句都有用！", R.mipmap.news6, "http://mp.weixin.qq.com/s/GmaVF69fLzm1k6XrfxwQDA");
        datas.add(data);

    }
}
