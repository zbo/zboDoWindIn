/**
* Created by twer on 15-4-20.
*/
class Windin {
    String source_base="http://www.windin.com/home/stock/stock-mh/%s.%s.shtml";
    String image_base="http://chart.windin.com/HQchart/kline/10000000/d/%s.%s.jpg";
    String basic_base="http://www.windin.com/home/stock/html/%s.%s.shtml";
    String sina_day_img_base="http://image2.sinajs.cn/newchart/daily/n/%s%s.gif";
    String sina_week_img_base="http://image2.sinajs.cn/newchart/weekly/n/%s%s.gif";

    public Windin(String code, String market,String name) {
        this.code=code;
        this.market=market;
        this.name=name;
    }

    String code;
    String market;
    String name;

    public String getName() {
        return name;
    }

    public String getSource() {
        return String.format(source_base, code, market);
    }

    public String getImage(){
        return String.format(image_base, code, market);
    }

    public String getBaseInfo() {
        return String.format(basic_base,code,market);
    }

    public String getSinaDayImg() {
        return String.format(sina_day_img_base,market.toLowerCase(),code);
    }
    public String getSinaWeekImg(){
        return String.format(sina_week_img_base,market.toLowerCase(),code);
    }
}
