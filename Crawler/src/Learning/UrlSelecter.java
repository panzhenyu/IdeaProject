package Learning;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class UrlSelecter
{
    public static void main(String[] args)  throws IOException
    {
        String url = "https://mooc1-2.chaoxing.com/mycourse/studentstudy?chapterId=115523274&courseId=201181511&clazzid=3484532&enc=a39072235511500f58ea4829fbd176a1";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.toString());
        Elements links = doc.select("a[herf]");
        for(Element link : links)
        {
            String linkHref = link.attr("href");
            System.out.println(linkHref);
        }
    }
}
