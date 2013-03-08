
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;

public class RequestManager {
	
	private String lang = "en";
	private String cookie = "shit";
	long four = Math.round(Math.random()*9999);
	long nine = Math.round(Math.random()*999999999);
	long time = System.currentTimeMillis();
	String subTime = String.valueOf(time).substring(0, 9);
	long counter = 1;
	private String rand1="";
	private String rand2="";
	
	public RequestManager() {
	}
	
	public RequestManager(String cookie) {
		this.cookie = cookie;
	}
	
	public String getLanguage() {
		return lang;
	}
	
	public void setLanguage(String lan) {
		//String lan = Locale.getDefault().toString().toLowerCase();
		lan = lan.toLowerCase();
		if (lan.equals("zh_tw") || lan.equals("zh_hk")) 
			lang = "zh-tw";
		else if (lan.equals("ko_kr")) 
			lang = "ko-kr";
		else if (lan.equals("zh_cn")) 
			lang = "zh-cn";
		else if (lan.equals("in_id")) 
			lang = "id-id";
		else if (lan.equals("vi_vn"))
			lang = "vi-vn";
		else if (lan.equals("th_th")) 
			lang = "th-th";
		else if (lan.substring(0, 2).equals("fr"))
			lang = "fr-fr";
		else if (lan.substring(0, 2).equals("pt"))
			lang = "pt-pt";
		else if (lan.equals("ru_ru"))
			lang = "ru-ru";
		else if (lan.equals("ja_jp"))
			lang = "ja-jp";
		else if (lan.substring(0, 2).equals("de"))
			lang = "de-de";
		else if (lan.equals("es_es")) 
			lang = "es-es";
		cookie = cookie.replace("lang=en", "lang="+lang);
	}

	/**
	 * 访问http://sports.sbobet.com, 获取cookie值
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getCookie(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException{
		//setLanguage();
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://www.sbobet.com/euro");
		this.setHeader(httpget);
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget);
		String eu = EntityUtils.toString(response.getEntity(), "UTF-8");
		//String eu = EntityUtils.toString(response.getEntity(), "UTF-8");
		//System.out.println(eu);
		int int1 = eu.indexOf("euro-static.js?");
		System.out.println(int1);
		rand1 = eu.substring(int1+15,int1+23);
		int int2 = eu.indexOf("euro-dynamic.js?");
		System.out.println(int2);
		rand2 = eu.substring(int2+16,int2+24);
		cookie = "";
		for (Header str : response.getHeaders("Set-Cookie")) {
			cookie = cookie + str.getValue().split(";")[0] + ";";
		}
		//cookie = cookie + "AWSUSER=id"+time+"r"+four+";"+"AWSSESSION=id"+time+"r"+four+";";
		cookie = cookie + "__utma="+"1."+nine+"."+subTime+"."+subTime+"."+subTime+".1"+";"+"__utmb="+"1.1.10."+subTime+";"+"__utmc="+"1"+";"+"__utmz="+"1."+subTime+".1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
		return cookie;
	}
	
	/**
	 * 访问euro-static.js?aef78775, 获取静态数据
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getEuroStaticJS(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://www.sbobet.com/en/resource/e/euro-static.js?"+rand1);
		this.setJSHeader(httpget);
		httpget.setHeader("Cookie", cookie);
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget);
		String eu = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(eu);
	}
	
	/**
	 * 访问euro-dynamic.js?5659f7dd, 获取动态数据
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getEuroDynamicJS(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://www.sbobet.com/en/resource/e/euro-dynamic.js?"+rand2);
		this.setJSHeader(httpget);
		httpget.setHeader("Cookie", cookie);
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget);
		String eu = EntityUtils.toString(response.getEntity(), "UTF-8");
		System.out.println(eu);
	}
	
	/**
	 * 访问sports.sbobet.com/web_root/public/odds/main.aspx, 获取相关赛事信息
	 * @param httpclient
	 * @throws URISyntaxExceptionin
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getMain(DefaultHttpClient httpclient, int sport, int page, int mode) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://sports.sbobet.com/web_root/public/odds/main.aspx?sport="+sport+"&page="+page+"&mode="+mode);
		this.setHeader(httpget);
		httpget.setHeader("Cookie", cookie);
		httpget.setHeader("Referer", "http://sports.sbobet.com/?lang=" + lang);
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget);
		return EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * 访问sports.sbobet.com/web_root/public/odds/frame-data.aspx, 获取比赛data
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postFrameData(DefaultHttpClient httpclient, String a, String b, String c, String d, String e, String f, int sport, int page, int mode) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("oddsp", a));
		formparams.add(new BasicNameValuePair("synids", b));
		formparams.add(new BasicNameValuePair("reload", c));
		formparams.add(new BasicNameValuePair("index", d));
		formparams.add(new BasicNameValuePair("frame", e));
		formparams.add(new BasicNameValuePair("lids", f));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		HttpPost httppost = new HttpPost("http://sports.sbobet.com/web_root/public/odds/frame-data.aspx");
		httppost.setEntity(entity);
		this.setHeader(httppost);
//		httppost.setHeader("Content-Length", String.valueOf(entity.getContentLength()));
		httppost.setHeader("Referer", "http://sports.sbobet.com/web_root/public/odds/main.aspx?sport="+sport+"&page="+page+"&mode="+mode);
		httppost.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httppost);
		return EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * 访问sports.sbobet.com/menu/menuFrame.aspx, 获取最新赛事场次
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String postMenuFrame(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("action", "getEC"));
		formparams.add(new BasicNameValuePair("counter", String.valueOf(counter)));
		counter++;
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		HttpPost httppost = new HttpPost("http://sports.sbobet.com/menu/menuFrame.aspx");
		httppost.setEntity(entity);
		this.setHeader(httppost);
		//httppost.setHeader("Content-Length", String.valueOf(entity.getContentLength()));
		httppost.setHeader("Referer", "http://sports.sbobet.com/HomeLeft.aspx");
		httppost.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httppost);
		String eu = EntityUtils.toString(response.getEntity());
		int _b = eu.indexOf("eventCount");
		if (_b == -1) return "";
		else {
			int _d = eu.indexOf(";", _b);
			return eu.substring(_b, _d+1);
		}
	}
	
	/**
	 * 设置固定GET请求头
	 */
	private void setHeader(HttpGet httpget) {
		httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpget.setHeader("Accept-Charset", "UTF-8,*;q=0.5");
//		httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		httpget.setHeader("Cache-Control", "max-age=0");
		httpget.setHeader("Connection", "keep-alive");
		httpget.setHeader("Host", "www.sbobet.com");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");
	}
	
	/**
	 * 设置固定JS请求头
	 */
	private void setJSHeader(HttpGet httpget) {
		httpget.setHeader("Accept", "*/*");
		httpget.setHeader("Accept-Charset", "UTF-8,*;q=0.5");
//		httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		httpget.setHeader("Cache-Control", "max-age=0");
		httpget.setHeader("Connection", "keep-alive");
		httpget.setHeader("Host", "www.sbobet.com");
		//httpget.setHeader("If-Modified-Since", "Sat, 02 Mar 2013 15:35:30 GMT");
		httpget.setHeader("Referer", "http://www.sbobet.com/euro");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");
	}
	
	/**
	 * 设置固定Post请求头
	 */
	private void setHeader(HttpPost httppost) {
		httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httppost.setHeader("Accept-Charset", "UTF-8,*;q=0.5");
//		httppost.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		httppost.setHeader("Cache-Control", "max-age=0");
		httppost.setHeader("Connection", "keep-alive");
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setHeader("Cookie", cookie);
		httppost.setHeader("Host", "sports.sbobet.com");
		httppost.setHeader("Origin", "http://sports.sbobet.com");
		httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4");
	}
	
	/**
	 * 虚假请求www.sbobet.com/en/resource/captcha/asia-sign-in.jpg, 获取服务器信任
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getAsiaSignInJpg(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://www.sbobet.com/en/resource/captcha/asia-sign-in.jpg?634849258744924000=");
		httpget.setHeader("Accept", "*/*");
		httpget.setHeader("Accept-Charset", "UTF-8,*;q=0.5");
		httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		httpget.setHeader("Connection", "keep-alive");
		httpget.setHeader("Cookie", "last_visit=asia; lang=; __utma=1.....1; __utmb=1.1.10.; __utmc=1; __utmz=1..1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
		httpget.setHeader("Host", "www.sbobet.com");
		httpget.setHeader("Referer", "http://sports.sbobet.com/hometop.aspx?ip=&p=");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4");
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.execute(httpget);
	}
	
	/**
	 * 虚假请求sports.sbobet.com/js/awstats_misc_tracker.js, 获取服务器信任
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getAwstatsMiscTrackerJs(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://sports.sbobet.com/js/awstats_misc_tracker.js?screen=1366x768&win=1366x81&cdi=32&java=true&shk=n&svg=y&fla=y&rp=n&mov=n&wma=n&pdf=y&uid=awsuser_id1357005399261r5988&sid=awssession_id1357005399261r5988");
		httpget.setHeader("Accept", "*/*");
		httpget.setHeader("Accept-Charset", "UTF-8,*;q=0.5");
		httpget.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		httpget.setHeader("Connection", "keep-alive");
		httpget.setHeader("Cookie", "ASP.NET_SessionId=; last_visit=asia; lang=; Siteseq-oldcupid-b-http=; __utma=1.....1; __utmb=1.1.10.; __utmc=1; __utmz=1..1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); AWSUSER_ID=awsuser_idr; AWSSESSION_ID=awssession_idr");
		httpget.setHeader("Host", "sports.sbobet.com");
		httpget.setHeader("Referer", "http://sports.sbobet.com/hometop.aspx?ip=&p=");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4");
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		httpclient.execute(httpget);
	}
	
	/**
	 * 虚假访问LiveChat.aspx, 获取服务器信任
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getLiveChat(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://sports.sbobet.com/LiveChat.aspx");
		this.setHeader(httpget);
		httpget.setHeader("Cookie", "ASP.NET_SessionId=; last_visit=asia; lang=; Siteseq-oldcupid-b-http=; __utma=1.....1; __utmb=1.2.10.; __utmc=1; __utmz=1..1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); AWSUSER_ID=awsuser_idr; AWSSESSION_ID=awssession_idr");
		httpget.setHeader("Referer", "http://sports.sbobet.com/?lang=en");
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget);
		EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * 虚假访问HomeMain.aspx, 获取服务器信任
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getHomeMain(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		HttpGet httpget = new HttpGet("http://sports.sbobet.com/LiveChat.aspx");
		this.setHeader(httpget);
		httpget.setHeader("Cookie", "ASP.NET_SessionId=; last_visit=asia; lang=; Siteseq-oldcupid-b-http=; __utma=1.....1; __utmb=1.3.10.; __utmc=1; __utmz=1..1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); AWSUSER_ID=awsuser_idr; AWSSESSION_ID=awssession_idr");
		httpget.setHeader("Referer", "http://sports.sbobet.com/?lang=en");
		httpget.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httpget);
		EntityUtils.toString(response.getEntity());
	}
	
	/**
	 * 访问sports.sbobet.com/web_root/public/odds/template-data.aspx, 获取服务器信任
	 * @param httpclient
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void postTemplateData(DefaultHttpClient httpclient) throws URISyntaxException, ClientProtocolException, IOException {
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2965);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("oddsp", ""));
		formparams.add(new BasicNameValuePair("names", "common,more,main-double-live,main-double-soccer"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
		HttpPost httppost = new HttpPost("http://sports.sbobet.com/web_root/public/odds/template-data.aspx");
		httppost.setEntity(entity);
		this.setHeader(httppost);
		httppost.setHeader("Content-Length", String.valueOf(entity.getContentLength()));
		httppost.setHeader("Referer", "http://sports.sbobet.com/web_root/public/odds/main.aspx?sport=&page=&mode=");
		httppost.getParams().setParameter(
				ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		HttpResponse response = httpclient.execute(httppost);
		EntityUtils.toString(response.getEntity());
	}
	
	//创建HttpClient实例  
	private DefaultHttpClient createHttpClient() {  
		HttpParams params = new BasicHttpParams();  
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);   
		HttpProtocolParams.setUseExpectContinue(params, true);  

		SchemeRegistry schReg = new SchemeRegistry();  
		schReg.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));  
		schReg.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));  

		ClientConnectionManager connMgr = new PoolingClientConnectionManager(schReg);  

		return new DefaultHttpClient(connMgr, params);  
	}
	
	public static void main(String[] args) {
		RequestManager rm = new RequestManager();
		try {
			System.out.println(rm.getCookie(rm.createHttpClient()));
			rm.getEuroDynamicJS(rm.createHttpClient());
			rm.getEuroStaticJS(rm.createHttpClient());
		} catch(IOException ex) {
		} catch(URISyntaxException ex2) {
		}
	}
	
	
	
}
