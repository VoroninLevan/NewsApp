package comvoroninlevan.instagram.www.newsapp;

/**
 * Created by Леван on 02.10.2016.
 */
public class News {

    private String mWebTitle;
    private String mSectionName;
    private String mWebPublicationDate;
    private String mUrl;
    private String mFullName;

    public News(String webTitle, String sectionName, String webPublicationDate, String url, String fullName){
        mWebTitle = webTitle;
        mSectionName = sectionName;
        mWebPublicationDate = webPublicationDate;
        mUrl = url;
        mFullName = fullName;
    }

    public String getWebTitle(){
        return mWebTitle;
    }
    public String getSectionName(){
        return mSectionName;
    }
    public String getWebPublicationDate(){
        return mWebPublicationDate;
    }
    public String getUrl(){
        return mUrl;
    }
    public String getFullName(){
        return mFullName;
    }
}
