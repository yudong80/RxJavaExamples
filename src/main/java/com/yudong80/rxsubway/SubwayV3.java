package com.yudong80.rxsubway;

import static com.yudong80.rxsubway.SubwayConstants.URL;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yudong80.common.CommonUtils;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SubwayV3 {
	private OkHttpClient client = new OkHttpClient();
	
	private static final String DEFAULT_MSG = "도착 정보가 없습니다"; 
	private String cache = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><realtimeStationArrival><RESULT><code>INFO-000</code><developerMessage></developerMessage><link></link><message>정상 처리되었습니다.</message><status>200</status><total>4</total></RESULT><row><rowNum>1</rowNum><selectedCount>4</selectedCount><totalCount>4</totalCount><subwayId>1005</subwayId><updnLine>상행</updnLine><trainLineNm>방화행 - 서대문방면</trainLineNm><subwayHeading>오른쪽</subwayHeading><statnFid>1005000532</statnFid><statnTid>1005000534</statnTid><statnId>1005000533</statnId><statnNm>광화문</statnNm><ordkey>01000방화0</ordkey><subwayList>1005</subwayList><statnList>1005000533</statnList><barvlDt>60</barvlDt><btrainNo>5648</btrainNo><bstatnId>24</bstatnId><bstatnNm>방화</bstatnNm><recptnDt>2018-08-20 19:13:12.0</recptnDt><arvlMsg2>광화문 도착</arvlMsg2><arvlMsg3>광화문</arvlMsg3><arvlCd>1</arvlCd></row><row><rowNum>2</rowNum><selectedCount>4</selectedCount><totalCount>4</totalCount><subwayId>1005</subwayId><updnLine>상행</updnLine><trainLineNm>방화행 - 서대문방면</trainLineNm><subwayHeading>오른쪽</subwayHeading><statnFid>1005000532</statnFid><statnTid>1005000534</statnTid><statnId>1005000533</statnId><statnNm>광화문</statnNm><ordkey>02002방화0</ordkey><subwayList>1005</subwayList><statnList>1005000533</statnList><barvlDt>240</barvlDt><btrainNo>5160</btrainNo><bstatnId>26</bstatnId><bstatnNm>방화</bstatnNm><recptnDt>2018-08-20 19:13:12.0</recptnDt><arvlMsg2>4분 후 (을지로4가)</arvlMsg2><arvlMsg3>을지로4가</arvlMsg3><arvlCd>99</arvlCd></row><row><rowNum>3</rowNum><selectedCount>4</selectedCount><totalCount>4</totalCount><subwayId>1005</subwayId><updnLine>하행</updnLine><trainLineNm>상일동행 - 종로3가방면</trainLineNm><subwayHeading>왼쪽</subwayHeading><statnFid>1005000532</statnFid><statnTid>1005000534</statnTid><statnId>1005000533</statnId><statnNm>광화문</statnNm><ordkey>11001상일동0</ordkey><subwayList>1005</subwayList><statnList>1005000533</statnList><barvlDt>180</barvlDt><btrainNo>5155</btrainNo><bstatnId>23</bstatnId><bstatnNm>상일동</bstatnNm><recptnDt>2018-08-20 19:13:22.0</recptnDt><arvlMsg2>전역 도착</arvlMsg2><arvlMsg3>서대문</arvlMsg3><arvlCd>5</arvlCd></row><row><rowNum>4</rowNum><selectedCount>4</selectedCount><totalCount>4</totalCount><subwayId>1005</subwayId><updnLine>하행</updnLine><trainLineNm>마천행 - 종로3가방면</trainLineNm><subwayHeading>왼쪽</subwayHeading><statnFid>1005000532</statnFid><statnTid>1005000534</statnTid><statnId>1005000533</statnId><statnNm>광화문</statnNm><ordkey>12003마천0</ordkey><subwayList>1005</subwayList><statnList>1005000533</statnList><barvlDt>360</barvlDt><btrainNo>5689</btrainNo><bstatnId>21</bstatnId><bstatnNm>마천</bstatnNm><recptnDt>2018-08-20 19:13:22.0</recptnDt><arvlMsg2>6분 후 (애오개)</arvlMsg2><arvlMsg3>애오개</arvlMsg3><arvlCd>99</arvlCd></row></realtimeStationArrival>\r\n" + 
			"";
	
	/**필요한 정보 
	 * <trainLineNm>방화행 - 서대문방면</trainLineNm>
	 * <arvlMsg2>광화문 도착</arvlMsg2>
	 */
	public void run() { 
		getSubwaySingle(URL)
			.subscribeOn(Schedulers.io()) //running background
			.onErrorReturnItem(cache)
			.map(this::valueOrCahced)
			.map(this::parseData)
			.observeOn(Schedulers.newThread()) //like foreground
			.subscribe(
				System.out::println,   //정상적인 결과는 System.out 으로 출력
				System.err::println);  //오류는 System.err에 출력
		
		CommonUtils.sleep(1000); //이건 왜 필요할까? 
	}
	
    public String valueOrCahced(String text) {
        if(is503Error(text)) {
            return cache;
        }

        cache = text;
        return text;
    }
    
    private static boolean is503Error(String text) {
        return text.indexOf("<title>503 Service Temporarily Unavailable</title>") >= 0;
    }	
	
	private String parseData(String text) { 
		return new StringBuilder()
				.append(parseTag(text, "trainLineNm"))
				.append(' ')
				.append(parseTag(text, "arvlMsg2"))
				.toString();
	}
	
	private String parseTag(String text, String tag) { 
		String regex = new StringBuilder()
				.append('<').append(tag).append('>')
				.append(".*?")
				.append("</").append(tag).append('>')
				.toString();
		Matcher matcher = Pattern.compile(regex).matcher(text);
		if (matcher.find()) { 
			return removeTag(matcher.group());
		}
		return "";
	}
	
    private static String removeTag(String element) {
        return element.substring(
                element.indexOf('>') + 1,
                element.lastIndexOf('<'));
    }	
	
	public Single<String> getSubwaySingle(String url) { 
		return Single.create(e -> {
			Request request = new Request.Builder()
			        .url(url)
			        .build();
			client.newCall(request).enqueue(new Callback() {
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					e.onSuccess(response.body().string());
				} 			
				
				@Override
				public void onFailure(Call call, IOException ioe) {
					e.onError(ioe);						
				}
			});
		});
	}	
	
	public static void main(String[] args) { 
		new SubwayV3().run();	
	}
}
