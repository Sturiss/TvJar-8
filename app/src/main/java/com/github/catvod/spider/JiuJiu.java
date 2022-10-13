package com.github.catvod.spider;

import android.content.Context;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.spider.merge.CU;
import com.github.catvod.spider.merge.P;
import com.github.catvod.spider.merge.Qh;
import com.github.catvod.spider.merge.hV;
import com.github.catvod.spider.merge.uN;
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
    private String H = "1";
    private static final Pattern Sh = Pattern.compile("/([0-9]+).html");
    private static final Pattern Ml = Pattern.compile("var vid=\"(.+?)\";");

    protected HashMap<String, String> Sh() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 10; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Mobile Safari/537.36");
        return hashMap;
    }

    public String categoryContent(String str, String str2, boolean z, HashMap<String, String> hashMap) {
        try {
            JSONObject jSONObject = new JSONObject();
            hV Sh2 = CU.Sh(P.IJ("https://www.hyingku.com/type/" + str + "/" + str2 + ".html", Sh()));
            Qh H8 = Sh2.H8("div.stui-vodlist__box> a");
            JSONArray jSONArray = new JSONArray();
            Iterator<uN> it = H8.iterator();
            while (it.hasNext()) {
                uN next = it.next();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("vod_id", next.H("href"));
                jSONObject2.put("vod_name", next.H("title"));
                jSONObject2.put("vod_pic", next.H("data-original"));
                jSONObject2.put("vod_remarks", next.Pt("span").fv());
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("page", str2);
            jSONObject.put("limit", 12);
            jSONObject.put("list", jSONArray);
            uN Pt = Sh2.Pt("li.active.visible-xs > span");
            if (Pt == null) {
                jSONObject.put("pagecount", Integer.MAX_VALUE);
                jSONObject.put("total", Integer.MAX_VALUE);
            } else {
                int parseInt = Integer.parseInt(Pt.fv().split("/")[1]);
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
            hV Sh2 = CU.Sh(P.IJ("https://www.hyingku.com" + list.get(0), Sh()));
            uN Pt = Sh2.Pt("div.stui-content");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("vod_id", list.get(0));
            jSONObject2.put("vod_name", Pt.Pt("div.stui-content__detail > h3").fv());
            jSONObject2.put("vod_pic", Pt.Pt("div.stui-content__thumb > a >img").H("data-original"));
            jSONObject2.put("vod_remarks", Pt.Pt("div.stui-content__detail > div.remarks > div").fv());
            jSONObject2.put("type_name", Pt.Pt("div.stui-content__detail > p:nth-of-type(1) > span.text-muted:contains(类型) + a").fv());
            jSONObject2.put("vod_director", Pt.Pt("div.stui-content__detail > p:nth-of-type(2) > span.text-muted:contains(导演) + a").fv());
            jSONObject2.put("vod_actor", Pt.Pt("div.stui-content__detail > p:nth-of-type(3) > span.text-muted:contains(主演) + a").fv());
            jSONObject2.put("vod_year", Pt.Pt("div.stui-content__detail > p:nth-of-type(1) > span.text-muted:contains(年份) + a").fv());
            jSONObject2.put("vod_area", Pt.Pt("div.stui-content__detail > p:nth-of-type(1) > span.text-muted:contains(地区) + a").fv());
            jSONObject2.put("vod_content", Sh2.Pt("span.detail-sketch").fv());
            Qh H8 = Sh2.H8("ul.nav.nav-tabs > li > a");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < H8.size(); i++) {
                sb.append(H8.get(i).fv());
                if (i < H8.size() - 1) {
                    sb.append("$$$");
                }
            }
            jSONObject2.put("vod_play_from", sb.toString());
            Qh H82 = Sh2.H8("ul.stui-content__playlist");
            StringBuilder sb2 = new StringBuilder();
            for (int i2 = 0; i2 < H82.size(); i2++) {
                Qh H83 = H82.get(i2).H8("li > a");
                for (int i3 = 0; i3 < H83.size(); i3++) {
                    uN uNVar = H83.get(i3);
                    sb2.append(uNVar.fv());
                    sb2.append("$");
                    sb2.append(uNVar.H("href"));
                    if (i3 < H83.size() - 1) {
                        sb2.append("#");
                    }
                }
                if (i2 < H82.size() - 1) {
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

    public String homeContent(boolean z) {
        Qh H8;
        Qh H82;
        try {
            JSONObject jSONObject = new JSONObject();
            hV Sh2 = CU.Sh(P.IJ("https://www.hyingku.com", Sh()));
            if (this.H.equals("6d616f7a65646f6e6773697869616e67")) {
                H8 = Sh2.H8("ul.stui-header__menu.type-slide > li:last-child > a");
                H82 = Sh2.H8("div.stui-pannel.stui-pannel-bg:nth-of-type(12) div.stui-vodlist__box> a");
            } else {
                H8 = Sh2.H8("ul.stui-header__menu.type-slide > li:nth-last-of-type(n+2) a[href$='.html']");
                H82 = Sh2.H8("div.stui-pannel.stui-pannel-bg:nth-last-of-type(n+4) div.stui-vodlist__box> a");
            }
            JSONArray jSONArray = new JSONArray();
            Iterator<uN> it = H8.iterator();
            while (it.hasNext()) {
                uN next = it.next();
                JSONObject jSONObject2 = new JSONObject();
                Matcher matcher = Sh.matcher(next.H("href"));
                if (matcher.find()) {
                    jSONObject2.put("type_id", matcher.group(1));
                    jSONObject2.put("type_name", next.fv());
                    jSONArray.put(jSONObject2);
                }
            }
            jSONObject.put("class", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            Iterator<uN> it2 = H82.iterator();
            while (it2.hasNext()) {
                uN next2 = it2.next();
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("vod_id", next2.H("href"));
                jSONObject3.put("vod_name", next2.H("title"));
                jSONObject3.put("vod_pic", next2.H("data-original"));
                jSONObject3.put("vod_remarks", next2.Pt("span").fv());
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
        this.H = str;
        super.init(context);
    }

    public String playerContent(String str, String str2, List<String> list) {
        try {
            JSONObject jSONObject = new JSONObject();
            String H = CU.Sh(P.IJ("https://www.hyingku.com" + str2, Sh())).Pt("div.stui-player__video > script").H("src");
            if (!H.startsWith("http")) {
                H = "https://www.jiujiukanpian.com" + H;
            }
            String replace = P.IJ(P.IJ(H, Sh()).split("src=")[1].split("frameborder")[0].trim().replace("\"", ""), Sh()).split("\"source\":")[1].split(",")[0].trim().replace("\"", "");
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
            Iterator<uN> it = CU.Sh(P.IJ("https://www.hyingku.com/search?wd=" + URLEncoder.encode(str, "utf-8"), Sh())).H8("li.active.clearfix").iterator();
            while (it.hasNext()) {
                JSONObject jSONObject2 = new JSONObject();
                uN Pt = it.next().Pt("div.thumb > a");
                jSONObject2.put("vod_id", Pt.H("href"));
                jSONObject2.put("vod_name", Pt.H("title"));
                jSONObject2.put("vod_pic", Pt.H("data-original"));
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
