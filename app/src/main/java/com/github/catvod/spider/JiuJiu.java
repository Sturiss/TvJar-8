package com.github.catvod.spider;

import android.content.Context;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.spider.merge.EH;
import com.github.catvod.spider.merge.Wv;
import com.github.catvod.spider.merge.cl;
import com.github.catvod.spider.merge.l;
import com.github.catvod.spider.merge.qv;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class JiuJiu extends Spider {
    private String f = "1";
    private static final Pattern fh = Pattern.compile("/([0-9]+).html");
    private static final Pattern Q = Pattern.compile("var vid=\"(.+?)\";");

    public String categoryContent(String str, String str2, boolean z, HashMap<String, String> hashMap) {
        try {
            JSONObject jSONObject = new JSONObject();
            Wv fh2 = EH.fh(l.Y("https://www.hyingku.com/type/" + str + "/" + str2 + ".html", fh()));
            cl ql = fh2.ql("div.stui-vodlist__box> a");
            JSONArray jSONArray = new JSONArray();
            Iterator<qv> it = ql.iterator();
            while (it.hasNext()) {
                qv next = it.next();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("vod_id", next.f("href"));
                jSONObject2.put("vod_name", next.f("title"));
                jSONObject2.put("vod_pic", next.f("data-original"));
                jSONObject2.put("vod_remarks", next.o("span").xk());
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("page", str2);
            jSONObject.put("limit", 12);
            jSONObject.put("list", jSONArray);
            qv o = fh2.o("li.active.visible-xs > span");
            if (o == null) {
                jSONObject.put("pagecount", Integer.MAX_VALUE);
                jSONObject.put("total", Integer.MAX_VALUE);
            } else {
                int parseInt = Integer.parseInt(o.xk().split("/")[1]);
                jSONObject.put("pagecount", parseInt);
                jSONObject.put("total", parseInt * 12);
            }
            return jSONObject.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public String detailContent(List<String> list) {
        try {
            JSONObject jSONObject = new JSONObject();
            Wv fh2 = EH.fh(l.Y("https://www.hyingku.com" + list.get(0), fh()));
            qv o = fh2.o("div.stui-content");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("vod_id", list.get(0));
            jSONObject2.put("vod_name", o.o("div.stui-content__detail > h3").xk());
            jSONObject2.put("vod_pic", o.o("div.stui-content__thumb > a >img").f("data-original"));
            jSONObject2.put("vod_remarks", o.o("div.stui-content__detail > div.remarks > div").xk());
            jSONObject2.put("type_name", o.o("div.stui-content__detail > p:nth-of-type(1) > span.text-muted:contains(类型) + a").xk());
            jSONObject2.put("vod_director", o.o("div.stui-content__detail > p:nth-of-type(2) > span.text-muted:contains(导演) + a").xk());
            jSONObject2.put("vod_actor", o.o("div.stui-content__detail > p:nth-of-type(3) > span.text-muted:contains(主演) + a").xk());
            jSONObject2.put("vod_year", o.o("div.stui-content__detail > p:nth-of-type(1) > span.text-muted:contains(年份) + a").xk());
            jSONObject2.put("vod_area", o.o("div.stui-content__detail > p:nth-of-type(1) > span.text-muted:contains(地区) + a").xk());
            jSONObject2.put("vod_content", fh2.o("span.detail-sketch").xk());
            cl ql = fh2.ql("ul.nav.nav-tabs > li > a");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ql.size(); i++) {
                sb.append(ql.get(i).xk());
                if (i < ql.size() - 1) {
                    sb.append("$$$");
                }
            }
            jSONObject2.put("vod_play_from", sb.toString());
            cl ql2 = fh2.ql("ul.stui-content__playlist");
            StringBuilder sb2 = new StringBuilder();
            for (int i2 = 0; i2 < ql2.size(); i2++) {
                cl ql3 = ql2.get(i2).ql("li > a");
                for (int i3 = 0; i3 < ql3.size(); i3++) {
                    qv qvVar = ql3.get(i3);
                    sb2.append(qvVar.xk());
                    sb2.append("$");
                    sb2.append(qvVar.f("href"));
                    if (i3 < ql3.size() - 1) {
                        sb2.append("#");
                    }
                }
                if (i2 < ql2.size() - 1) {
                    sb2.append("$$$");
                }
            }
            jSONObject2.put("vod_play_url", sb2.toString());
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(jSONObject2);
            jSONObject.put("list", jSONArray);
            return jSONObject.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    protected HashMap<String, String> fh() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 10; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Mobile Safari/537.36");
        return hashMap;
    }

    public String homeContent(boolean z) {
        cl ql;
        cl ql2;
        try {
            JSONObject jSONObject = new JSONObject();
            Wv fh2 = EH.fh(l.Y("https://www.hyingku.com", fh()));
            if (this.f.equals("6d616f7a65646f6e6773697869616e67")) {
                ql = fh2.ql("ul.stui-header__menu.type-slide > li:last-child > a");
                ql2 = fh2.ql("div.stui-pannel.stui-pannel-bg:nth-of-type(12) div.stui-vodlist__box> a");
            } else {
                ql = fh2.ql("ul.stui-header__menu.type-slide > li:nth-last-of-type(n+2) a[href$='.html']");
                ql2 = fh2.ql("div.stui-pannel.stui-pannel-bg:nth-last-of-type(n+4) div.stui-vodlist__box> a");
            }
            JSONArray jSONArray = new JSONArray();
            Iterator<qv> it = ql.iterator();
            while (it.hasNext()) {
                qv next = it.next();
                JSONObject jSONObject2 = new JSONObject();
                Matcher matcher = fh.matcher(next.f("href"));
                if (matcher.find()) {
                    jSONObject2.put("type_id", matcher.group(1));
                    jSONObject2.put("type_name", next.xk());
                    jSONArray.put(jSONObject2);
                }
            }
            jSONObject.put("class", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            Iterator<qv> it2 = ql2.iterator();
            while (it2.hasNext()) {
                qv next2 = it2.next();
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("vod_id", next2.f("href"));
                jSONObject3.put("vod_name", next2.f("title"));
                jSONObject3.put("vod_pic", next2.f("data-original"));
                jSONObject3.put("vod_remarks", next2.o("span").xk());
                jSONArray2.put(jSONObject3);
            }
            jSONObject.put("list", jSONArray2);
            return jSONObject.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public void init(Context context, String str) {
        this.f = str;
        super.init(context);
    }

    public String playerContent(String str, String str2, List<String> list) {
        try {
            JSONObject jSONObject = new JSONObject();
            String f = EH.fh(l.Y("https://www.hyingku.com" + str2, fh())).o("div.stui-player__video > script").f("src");
            if (!f.startsWith("http")) {
                f = "https://www.jiujiukanpian.com" + f;
            }
            String replace = l.Y(l.Y(f, fh()).split("src=")[1].split("frameborder")[0].trim().replace("\"", ""), fh()).split("\"source\":")[1].split(",")[0].trim().replace("\"", "");
            if (replace.contains(".mp4")) {
                jSONObject.put("url", replace);
            } else if (replace.contains(".m3u8")) {
                jSONObject.put("url", replace);
            }
            jSONObject.put("parse", "0");
            jSONObject.put("playUrl", "");
            jSONObject.put("header", "");
            return jSONObject.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public String searchContent(String str, boolean z) {
        try {
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            Iterator<qv> it = EH.fh(l.Y("https://www.hyingku.com/search?wd=" + URLEncoder.encode(str, "utf-8"), fh())).ql("li.active.clearfix").iterator();
            while (it.hasNext()) {
                JSONObject jSONObject2 = new JSONObject();
                qv o = it.next().o("div.thumb > a");
                jSONObject2.put("vod_id", o.f("href"));
                jSONObject2.put("vod_name", o.f("title"));
                jSONObject2.put("vod_pic", o.f("data-original"));
                jSONObject2.put("vod_remarks", "");
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("list", jSONArray);
            return jSONObject.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }
}
