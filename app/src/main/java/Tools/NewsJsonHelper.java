package Tools;

import com.google.gson.Gson;

import Model.NewsModel;

/**
 * Created by Administrator on 2017/2/21.
 */
public class NewsJsonHelper {
    //解析push过来的json数据
    public static NewsModel pushJsonDecode(String newsStr){
        Gson gson = new Gson();
        NewsModel news=gson.fromJson(newsStr,NewsModel.class);
        return news;
    }
}
