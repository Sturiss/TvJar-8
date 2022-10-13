package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.spider.merge.Hq;
import com.github.catvod.spider.merge.JD;
import com.github.catvod.spider.merge.Vf;
import com.github.catvod.spider.merge.ex;
import com.github.catvod.spider.merge.vR;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Juhuang extends Spider {
    protected JSONObject c;
    protected JSONObject j;
    protected Pattern KO = Pattern.compile("/type/(\\d+)_type.html");
    protected Pattern b = Pattern.compile("/play/(\\d+)_play_\\d+_\\d+.html");
    protected Pattern g = Pattern.compile("/play/(\\d+)_play_(\\d+)_(\\d+).html");
    protected Pattern v = Pattern.compile("/type/\\d+_type_(\\d+).html");

    protected static HashMap<String, String> j() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.62 Safari/537.36");
        hashMap.put("Host", "so.juhuang.tv");
        hashMap.put("Referer", "https://juhuang.tv/");
        return hashMap;
    }

    protected HashMap<String, String> c(String str) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("method", "GET");
        hashMap.put("Host", "juhuang.tv");
        hashMap.put("Referer", str);
        hashMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        return hashMap;
    }

    public String categoryContent(String str, String str2, boolean z, HashMap<String, String> hashMap) {
        int i;
        int i2;
        try {
            String str3 = "https://juhuang.tv/type/" + str + "_type.html";
            if (str2 != null && Integer.parseInt(str2) > 1) {
                str3 = "https://juhuang.tv" + String.format("/type/%s_type_%s.html", str, str2);
            }
            String h = Vf.h(str3, c(str3));
            vR ue = JD.ue(h);
            JSONObject jSONObject = new JSONObject();
            ex B1 = ue.B1("div.module-footer >div[id=page] > a");
            if (B1.size() == 0) {
                i2 = Integer.parseInt(str2);
                i = i2;
            } else {
                int i3 = 0;
                while (true) {
                    if (i3 >= B1.size()) {
                        break;
                    }
                    Hq O = B1.get(i3).O("a");
                    if (O != null && O.pE().equals("尾页")) {
                        Matcher matcher = this.v.matcher(O.Pd("href"));
                        if (matcher.find()) {
                            i2 = Integer.parseInt(matcher.group(1));
                            i = -1;
                        }
                    }
                    i3++;
                }
                i = -1;
                i2 = 1;
            }
            JSONArray jSONArray = new JSONArray();
            if (!h.contains("没有找到您想要的结果哦")) {
                ex B12 = ue.B1("div.module-items>div");
                for (int i4 = 0; i4 < B12.size(); i4++) {
                    Hq hq = B12.get(i4);
                    String Pd = hq.O("div.module-item-cover > div.module-item-pic > a").Pd("title");
                    String Pd2 = hq.O("div.module-item-cover > div.module-item-pic > img").Pd("data-src");
                    Matcher matcher2 = this.b.matcher(hq.O("div.module-item-cover > div.module-item-pic > a").Pd("href"));
                    if (matcher2.find()) {
                        String group = matcher2.group(1);
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("vod_id", group);
                        jSONObject2.put("vod_name", Pd);
                        jSONObject2.put("vod_pic", Pd2);
                        jSONArray.put(jSONObject2);
                    }
                }
            }
            jSONObject.put("page", i);
            jSONObject.put("pagecount", i2);
            jSONObject.put("limit", 48);
            jSONObject.put("total", i2 <= 1 ? jSONArray.length() : i2 * 48);
            jSONObject.put("list", jSONArray);
            return jSONObject.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public String detailContent(List<String> list) {
        try {
            String str = "https://juhuang.tv/vod/" + list.get(0) + "_vod.html";
            vR ue = JD.ue(Vf.h(str, c(str)));
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            String Pd = ue.O("div.module-item-pic > img").Pd("data-src");
            String pE = ue.O("div.video-info-header > h1.page-title").pE();
            String pE2 = ue.O("p.zkjj_a").pE();
            jSONObject2.put("vod_id", list.get(0));
            jSONObject2.put("vod_name", pE);
            jSONObject2.put("vod_pic", Pd);
            jSONObject2.put("vod_content", pE2);
            TreeMap treeMap = new TreeMap(new Comparator<String>() { // from class: com.github.catvod.spider.Juhuang.1
                @Override // java.util.Comparator
                public int compare(String str2, String str3) {
                    try {
                        int i = Juhuang.this.j.getJSONObject(str2).getInt("or");
                        int i2 = Juhuang.this.j.getJSONObject(str3).getInt("or");
                        return (i != i2 && i - i2 <= 0) ? -1 : 1;
                    } catch (JSONException e) {
                        SpiderDebug.log(e);
                        return 1;
                    }
                }
            });
            ex B1 = ue.B1("div.module");
            for (int i = 0; i < B1.size() - 1; i++) {
                Hq hq = B1.get(i);
                String jw = hq.B1("div.module-heading > h2.module-title").jw();
                ArrayList arrayList = new ArrayList();
                ex B12 = hq.B1("div.module-list > div.module-blocklist > div.scroll-content > a");
                for (int i2 = 0; i2 < B12.size(); i2++) {
                    Hq hq2 = B12.get(i2);
                    String Pd2 = hq2.Pd("href");
                    String substring = Pd2.substring(6, Pd2.indexOf(".html"));
                    arrayList.add(hq2.B1("span").jw() + "$" + substring);
                }
                treeMap.put(jw, arrayList.size() > 0 ? TextUtils.join("#", arrayList) : "");
            }
            if (treeMap.size() > 0) {
                String join = TextUtils.join("$$$", treeMap.keySet());
                String join2 = TextUtils.join("$$$", treeMap.values());
                jSONObject2.put("vod_play_from", join);
                jSONObject2.put("vod_play_url", join2);
            }
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
        int i;
        try {
            vR ue = JD.ue(Vf.h("https://juhuang.tv", c("https://juhuang.tv")));
            ex B1 = ue.B1("ul.nav-menu-items > li > a");
            JSONArray jSONArray = new JSONArray();
            Iterator<Hq> it = B1.iterator();
            while (true) {
                i = 0;
                if (!it.hasNext()) {
                    break;
                }
                Hq next = it.next();
                String pE = next.pE();
                if (pE.equals("Youtube精选") || pE.equals("电影") || pE.equals("剧集") || pE.equals("综艺") || pE.equals("动漫") || pE.equals("纪录片")) {
                    i = 1;
                }
                if (i != 0) {
                    Matcher matcher = this.KO.matcher(next.Pd("href"));
                    if (matcher.find()) {
                        String trim = matcher.group(1).trim();
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("type_id", trim);
                        jSONObject.put("type_name", pE);
                        jSONArray.put(jSONObject);
                    }
                }
            }
            JSONObject jSONObject2 = new JSONObject();
            if (z) {
                jSONObject2.put("filters", this.c);
            }
            jSONObject2.put("class", jSONArray);
            try {
                ex B12 = ue.B1("div.module-items>div");
                JSONArray jSONArray2 = new JSONArray();
                while (i < B12.size()) {
                    Hq hq = B12.get(i);
                    String Pd = hq.O("div.module-item-cover > div.module-item-pic > a").Pd("title");
                    String Pd2 = hq.O("div.module-item-cover > div.module-item-pic > img").Pd("data-src");
                    String pE2 = hq.O("div.module-item-text").pE();
                    Matcher matcher2 = this.b.matcher(hq.O("div.module-item-cover > div.module-item-pic > a").Pd("href"));
                    if (matcher2.find()) {
                        String group = matcher2.group(1);
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("vod_id", group);
                        jSONObject3.put("vod_name", Pd);
                        jSONObject3.put("vod_pic", Pd2);
                        jSONObject3.put("vod_remarks", pE2);
                        jSONArray2.put(jSONObject3);
                    }
                    i++;
                }
                jSONObject2.put("list", jSONArray2);
            } catch (Exception e) {
                SpiderDebug.log(e);
            }
            return jSONObject2.toString();
        } catch (Exception e2) {
            SpiderDebug.log(e2);
            return "";
        }
    }

    public void init(Context context) {
        super.init(context);
        try {
            this.j = new JSONObject("{\"xg\":{\"sh\":\"xg播放器\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"dplayer\":{\"sh\":\"dplayer播放器\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"videojs\":{\"sh\":\"videojs-H5播放器\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"iva\":{\"sh\":\"iva-H5播放器\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"iframe\":{\"sh\":\"iframe外链数据\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"link\":{\"sh\":\"外链数据\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"swf\":{\"sh\":\"Flash文件\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"flv\":{\"sh\":\"Flv文件\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"plyr\":{\"sh\":\"plyr\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"H5player\":{\"sh\":\"H5player\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"playerjs\":{\"sh\":\"playerjs\",\"or\":999,\"sn\":0,\"pu\":\"\"},\"aliplayer\":{\"sh\":\"阿里播放器\",\"or\":999,\"sn\":0,\"pu\":\"\"}}");
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
    }

    public String playerContent(String str, String str2, List<String> list) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("origin", " https://juhuang.tv");
            jSONObject.put("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
            jSONObject.put("Accept", " */*");
            jSONObject.put("Accept-Language", " zh-CN,zh;q=0.9,en-US;q=0.3,en;q=0.7");
            jSONObject.put("Accept-Encoding", " gzip, deflate");
            jSONObject.put("Referer", " https://juhuang.tv/");
            String str3 = "https://juhuang.tv/play/" + str2 + ".html";
            ex B1 = JD.ue(Vf.h(str3, c(str3))).B1("script");
            JSONObject jSONObject2 = new JSONObject();
            int i = 0;
            while (true) {
                if (i >= B1.size()) {
                    break;
                }
                String trim = B1.get(i).Wt().trim();
                if (trim.startsWith("var player_")) {
                    JSONObject jSONObject3 = new JSONObject(trim.substring(trim.indexOf(123), trim.lastIndexOf(125) + 1));
                    if (this.j.has(jSONObject3.getString("from"))) {
                        this.j.getJSONObject(jSONObject3.getString("from"));
                        String string = jSONObject3.getString("url");
                        if (jSONObject3.has("encrypt")) {
                            int i2 = jSONObject3.getInt("encrypt");
                            if (i2 == 1) {
                                string = URLDecoder.decode(string);
                            } else if (i2 == 2) {
                                string = URLEncoder.encode(new String(Base64.decode(URLDecoder.decode(new String(Base64.decode(string, 0))), 0)));
                            }
                        }
                        String str4 = "https://web-webapi-tsjqsvyzyx.cn-shenzhen.fcapp.run/?url=" + string;
                        HashMap hashMap = new HashMap();
                        hashMap.put("Referer", " https://juhuang.tv/");
                        hashMap.put("User-Agent", " Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
                        jSONObject2.put("url", new JSONObject(Vf.h(str4, hashMap)).getString("play_url"));
                        jSONObject2.put("parse", "0");
                        jSONObject2.put("playUrl", "");
                        jSONObject2.put("header", "");
                    }
                } else {
                    i++;
                }
            }
            return jSONObject2.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }

    public String searchContent(String str, boolean z) {
        if (z) {
            return "";
        }
        try {
            JSONObject jSONObject = new JSONObject(Vf.h("https://so.juhuang.tv/soapi.php?wd=" + URLEncoder.encode(str), j()));
            JSONObject jSONObject2 = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            if (jSONObject.getInt("count") > 0) {
                JSONArray jSONArray2 = new JSONArray(jSONObject.getString("list"));
                for (int i = 0; i < jSONArray2.length(); i++) {
                    JSONObject jSONObject3 = jSONArray2.getJSONObject(i);
                    String string = jSONObject3.getString("vod_id");
                    String string2 = jSONObject3.getString("vod_name");
                    String string3 = jSONObject3.getString("vod_pic");
                    JSONObject jSONObject4 = new JSONObject();
                    jSONObject4.put("vod_id", string);
                    jSONObject4.put("vod_name", string2);
                    jSONObject4.put("vod_pic", string3);
                    jSONObject4.put("vod_remarks", "");
                    jSONArray.put(jSONObject4);
                }
            }
            jSONObject2.put("list", jSONArray);
            return jSONObject2.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }
}
