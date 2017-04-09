package Model;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/21.
 */
public class NewsModel extends BaseModel{
    public News data;
    public class News{
        public int id;
        public String title;
        public String desc;
        public String url;
        public String runtime;
        public int isRead;
        public int isCollect;
        public News(Map newsinfo) {
            this.id=Integer.valueOf(newsinfo.get("id").toString());
            this.title=newsinfo.get("title").toString();
            this.desc=newsinfo.get("desc").toString();
            this.url=newsinfo.get("url").toString();
            this.runtime=newsinfo.get("runtime").toString();
            this.isRead=Integer.valueOf(newsinfo.get("isRead").toString());
            this.isCollect=Integer.valueOf(newsinfo.get("isCollect").toString());

        }
        public News(){

        }
    }
}


